// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.base.Ascii;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
class EtagFilter implements ClientRequestFilter, ClientResponseFilter {
    private final Cache<String, CachedRequest> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .build();

    @Override
    public void filter(ClientRequestContext requestContext) {
        CachedRequest request = cache.getIfPresent(requestContext.getUri().toString());
        if (request != null) {
            requestContext.getHeaders().putSingle(HttpHeaders.IF_NONE_MATCH, request.getTag());
            log.trace("Adding {} header to request to {}", HttpHeaders.IF_NONE_MATCH, Ascii.truncate(requestContext.getUri().toString(), 50, "..."));
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        String tag = responseContext.getHeaderString(HttpHeaders.ETAG);
        String uri = requestContext.getUri().toString();
        if (responseContext.getStatus() == Response.Status.NOT_MODIFIED.getStatusCode()) {
            CachedRequest cachedRequest = cache.getIfPresent(uri);
            byte[] body = cachedRequest.getBody();
            responseContext.setEntityStream(new ByteArrayInputStream(body));
            responseContext.setStatusInfo(cachedRequest.getStatusInfo());
            replaceHeaders(responseContext.getHeaders(), cachedRequest.getHeaders());
            log.trace("Got response for {} from ETag cache", Ascii.truncate(uri, 50, "..."));
        }
        else if (tag != null) {
            byte[] body = ByteStreams.toByteArray(responseContext.getEntityStream());
            Closeables.closeQuietly(responseContext.getEntityStream());
            cache.put(uri, new CachedRequest(tag, responseContext.getStatusInfo(), body, save(responseContext.getHeaders())));
            responseContext.setEntityStream(new ByteArrayInputStream(body));
            log.trace("Added response for {} to ETag cache", Ascii.truncate(uri, 50, "..."));
        }
    }

    private Multimap<String, String> save(MultivaluedMap<String, String> headers) {
        ImmutableMultimap.Builder<String, String> builder = ImmutableMultimap.builder();
        for (String header : headers.keySet()) {
            builder.putAll(header, headers.get(header));
        }
        return builder.build();
    }

    private void replaceHeaders(MultivaluedMap<String, String> target, Multimap<String, String> cached) {
        target.clear();
        for (String header : cached.keySet()) {
            target.put(header, Lists.newArrayList(cached.get(header)));
        }
    }

    @Value
    private static class CachedRequest {
        String tag;
        Response.StatusType statusInfo;
        byte[] body;
        Multimap<String, String> headers;
    }
}
