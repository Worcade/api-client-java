// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.worcade.client.ErrorCode;
import net.worcade.client.Result;
import net.worcade.client.Worcade;
import net.worcade.client.WorcadeBuilder;
import net.worcade.client.get.BinaryData;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.*;

@Slf4j @ToString
class JerseyClient extends WorcadeClient {
    private static final GenericType<Envelope> ENVELOPE_TYPE = new GenericType<Envelope>() {};

    @Accessors(fluent = true, chain = true)
    public static class JerseyClientBuilder implements WorcadeBuilder {
        @Setter private String baseUrl = "https://worcade.net";
        @Setter private String apiKey;
        private boolean enableETagCache = true;

        @Override
        public WorcadeBuilder disableETagCache() {
            enableETagCache = false;
            return this;
        }

        @Override
        public Worcade build() {
            checkState(apiKey != null, "API key is mandatory");

            ClientBuilder builder = ClientBuilder.newBuilder()
                    .register(JacksonJsonProvider.class)
                    .register(new LoggingFeature());
            if (enableETagCache) {
                builder.register(new EtagFilter());
            }
            return new JerseyClient(baseUrl, apiKey, builder.build());
        }
    }

    private final Client client;

    private JerseyClient(String baseUrl, String apiKey, Client client) {
        super(baseUrl, apiKey);
        this.client = client;

        checkServerVersion();
    }

    @Override
    protected Result<IncomingDto> get(String url, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader).get();
        return handle("GET", url, response).map(JerseyClient.DTO_FUNCTION);
    }

    @Override
    protected Result<List<IncomingDto>> getList(String url, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader).get();
        return handle("GET", url, response).map(l -> {
            @SuppressWarnings("unchecked") List<Object> list = (List<Object>) l;
            return ImmutableList.copyOf(Lists.transform(list, JerseyClient.DTO_FUNCTION::apply));
        });
    }

    @Override
    protected Result<BinaryData> getBinary(String url, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader).get();
        log.debug("GET request to {} returned with status {}", getBaseUrl() + url, response.getStatus());

        if (response.getStatus() == 429) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Too many requests")));
        }
        return Result.ok(new BinaryData(response.getHeaderString(HttpHeaders.CONTENT_DISPOSITION),
                (InputStream) response.getEntity(), response.getMediaType().toString()));
    }

    @Override
    protected Result<IncomingDto> post(String url, Object data, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader)
                .post(data == null ? null : Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return handle("POST", url, response).map(JerseyClient.DTO_FUNCTION);
    }

    @Override
    protected Result<IncomingDto> postBinary(String url, InputStream data, String contentType, Header... additionalHeader) {
        return post(getBaseUrl() + url, Entity.entity(data, contentType), additionalHeader);
    }

    @Override
    protected Result<IncomingDto> put(String url, Object data, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader)
                .put(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return handle("PUT", url, response).map(JerseyClient.DTO_FUNCTION);
    }

    @Override
    protected Result<IncomingDto> delete(String url, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader).delete();
        return handle("DELETE", url, response).map(JerseyClient.DTO_FUNCTION);
    }

    @Override
    protected Result<IncomingDto> delete(String url, Object data, Header... additionalHeader) {
        Response response = target(getBaseUrl() + url, additionalHeader)
                // Jersey thinks request bodies for DELETE are not allowed
                .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true)
                .method("DELETE", Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        return handle("DELETE", url, response).map(JerseyClient.DTO_FUNCTION);
    }

    private Result<Object> handle(String method, String url, Response response) {
        if (response.getStatus() == 429) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Too many requests")));
        }

        Envelope envelope;
        ImmutableList.Builder<Result.Message> messages = ImmutableList.builder();
        try {
            envelope = response.readEntity(JerseyClient.ENVELOPE_TYPE);
            if (envelope.getMessages() != null) {
                for (Envelope.Message message : envelope.getMessages()) {
                    messages.add(new Result.Message(ErrorCode.forCode(message.getCode()), message.getMessage()));
                }
            }
        }
        catch (ProcessingException e) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Exception while parsing response to " + method + " " + getBaseUrl() + url + ", with status " + response.getStatus())));
        }

        ImmutableList<Result.Message> messageList = messages.build();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            if (!messageList.isEmpty()) {
                log.debug("{} request to {} succeeded with status {} and messages {}", method, getBaseUrl() + url, response.getStatus(), messageList);
            }
            else {
                log.trace("{} request to {} succeeded with status {}", method, getBaseUrl() + url, response.getStatus());
            }
            return Result.ok(envelope.getData(), messageList);
        }
        log.debug("{} request to {} failed with status {} and messages {}", method, getBaseUrl() + url, response.getStatus(), messageList);
        return Result.failed(messageList);
    }

    protected Invocation.Builder target(String url, Header... additionalHeaders) {
        Invocation.Builder builder = client.target(url).request()
                .header("Worcade-ApiKey", getApiKey())
                .header("Worcade-User", getUserHeader())
                .header("Worcade-Application", getApplicationHeader());
        for (Header header : additionalHeaders) {
            builder.header(header.getName(), header.getValue());
        }
        return builder;
    }
}
