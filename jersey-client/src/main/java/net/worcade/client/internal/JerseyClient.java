// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Code;
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
import java.util.function.Function;

@Slf4j
@ToString
public class JerseyClient extends WorcadeClient {
    private static final GenericType<Envelope> ENVELOPE_TYPE = new GenericType<Envelope>() {};

    @AutoService(WorcadeBuilder.class)
    @Accessors(fluent = true, chain = true)
    public static class JerseyClientBuilder implements WorcadeBuilder {
        @Setter String baseUrl = "https://worcade.net";
        boolean enableETagCache = true;

        @Override
        public WorcadeBuilder disableETagCache() {
            enableETagCache = false;
            return this;
        }

        public WorcadeClient buildWithoutVersionCheck() {
            ClientBuilder builder = ClientBuilder.newBuilder()
                    .register(JacksonJsonProvider.class)
                    .register(new LoggingFeature());
            if (enableETagCache) {
                builder.register(new EtagFilter());
            }
            return createInstance(baseUrl, builder.build());
        }

        protected WorcadeClient createInstance(String baseUrl, Client client) {
            return new JerseyClient(baseUrl, client);
        }

        @Override
        public Result<? extends Worcade> build() {
            try {
                return buildWithoutVersionCheck().checkServerVersion();
            }
            catch (ProcessingException e) {
                // TODO move these to around all get(), post(), etc. callls
                return Result.failed(ImmutableList.of(new Result.Message(Code.NO_CONNECTION, "Couldn't connect to Worcade")));
            }
        }
    }

    @Getter(AccessLevel.PROTECTED) private final Client client;

    JerseyClient(String baseUrl, Client client) {
        super(baseUrl);
        this.client = client;
    }

    @Override
    protected WorcadeClient copy() {
        return new JerseyClient(getBaseUrl(), client);
    }

    @Override
    protected Result<IncomingDto> get(String url, Header... additionalHeader) {
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("GET", url, request, Invocation.Builder::get).map(DTO_FUNCTION);
    }

    @Override
    protected Result<List<IncomingDto>> getList(String url, Header... additionalHeader) {
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("GET", url, request, Invocation.Builder::get).map(l -> {
            @SuppressWarnings("unchecked") List<Object> list = (List<Object>) l;
            return ImmutableList.copyOf(Lists.transform(list, DTO_FUNCTION::apply));
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
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("POST", url, request, b -> b.post(toEntity(data))).map(DTO_FUNCTION);
    }

    private Entity<?> toEntity(Object data) {
        if (data == null) return null;
        if (data instanceof Entity) return (Entity<?>) data;
        return Entity.entity(data, MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    protected Result<IncomingDto> postBinary(String url, InputStream data, String contentType, Header... additionalHeader) {
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("POST", url, request, b -> b.post(Entity.entity(data, contentType))).map(DTO_FUNCTION);
    }

    @Override
    protected Result<IncomingDto> put(String url, Object data, Header... additionalHeader) {
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("PUT", url, request, b -> b.put(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE))).map(DTO_FUNCTION);
    }

    @Override
    protected Result<IncomingDto> delete(String url, Header... additionalHeader) {
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("DELETE", url, request, Invocation.Builder::delete).map(DTO_FUNCTION);
    }

    @Override
    protected Result<IncomingDto> delete(String url, Object data, Header... additionalHeader) {
        Invocation.Builder request = target(getBaseUrl() + url, additionalHeader);
        return handle("DELETE", url, request, b -> b
                // Jersey thinks request bodies for DELETE are not allowed
                .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true)
                .method("DELETE", Entity.entity(data, MediaType.APPLICATION_JSON_TYPE)))
            .map(DTO_FUNCTION);
    }

    @Override
    public void close() {
        client.close();
    }

    protected Result<Object> handle(String method, String url, Invocation.Builder request, Function<Invocation.Builder, Response> handler) {
        Response response;
        try {
            response = handler.apply(request);
        }
        catch (ProcessingException e) {
            return Result.failed(ImmutableList.of(new Result.Message(Code.NO_CONNECTION, "Couldn't connect to Worcade")));
        }
        return handle(method, url, response, ENVELOPE_TYPE, Envelope::getData);
    }

    protected <T, V> Result<V> handle(String method, String url, Response response, GenericType<T> responseType, Function<T, V> mapper) {
        if (response.getStatus() == 429) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Too many requests")));
        }

        T responseEntity;
        ImmutableList.Builder<Result.Message> messages = ImmutableList.builder();
        if (response.getStatus() == 502 || response.getStatus() == 503 || response.getStatus() == 504) {
            messages.add(new Result.Message(Code.NO_CONNECTION, "Couldn't connect to Worcade"));
        }
        try {
            responseEntity = response.readEntity(responseType);
            if (responseEntity instanceof Envelope && ((Envelope) responseEntity).getMessages() != null) {
                for (Envelope.Message message : ((Envelope) responseEntity).getMessages()) {
                    messages.add(new Result.Message(Code.forCode(message.getCode()), message.getMessage()));
                }
            }
        }
        catch (ProcessingException | NullPointerException e) {
            log.debug("Error parsing response to {} {}{} with status {}", method, getBaseUrl(), url, response.getStatus(), e);
            messages.add(new Result.Message(null, "Exception while parsing response to " + method + " " + getBaseUrl() + url + ", with status " + response.getStatus()));
            return Result.failed(messages.build());
        }

        ImmutableList<Result.Message> messageList = messages.build();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            if (!messageList.isEmpty()) {
                log.debug("{} request to {} succeeded with status {} and messages {}", method, getBaseUrl() + url, response.getStatus(), messageList);
            }
            else {
                log.trace("{} request to {} succeeded with status {}", method, getBaseUrl() + url, response.getStatus());
            }
            return Result.ok(mapper.apply(responseEntity), messageList);
        }
        log.debug("{} request to {} failed with status {} and messages {}", method, getBaseUrl() + url, response.getStatus(), messageList);
        return Result.failed(messageList);
    }

    protected Invocation.Builder target(String url, Header... additionalHeaders) {
        Invocation.Builder builder = client.target(url).request();
        builder = addAuthHeaders(builder);
        for (Header header : additionalHeaders) {
            builder.header(header.getName(), header.getValue());
        }
        return builder;
    }

    protected Invocation.Builder addAuthHeaders(Invocation.Builder builder) {
        return builder.header("Worcade-User", getUserHeader())
                      .header("Worcade-Application", getApplicationHeader());
    }
}
