/*
 *  Copyright 2018 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.eventlistener.zapier;

import io.curity.identityserver.plugin.eventlistener.zapier.config.EventListenerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.data.events.AuditableEvent;
import se.curity.identityserver.sdk.errors.ErrorCode;
import se.curity.identityserver.sdk.event.EventListener;
import se.curity.identityserver.sdk.http.HttpRequest;
import se.curity.identityserver.sdk.http.HttpResponse;
import se.curity.identityserver.sdk.service.HttpClient;
import se.curity.identityserver.sdk.service.WebServiceClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public class ZapierEventListener implements EventListener<AuditableEvent>
{
    private static final Logger _logger = LoggerFactory.getLogger(ZapierEventListener.class);
    private final EventListenerConfiguration _config;

    public ZapierEventListener(EventListenerConfiguration config)
    {
        _config = config;
    }

    @Override
    public Class<AuditableEvent> getEventType()
    {
        return AuditableEvent.class;
    }

    @Override
    public void handle(AuditableEvent event)
    {
        _logger.trace("Handling event {} : {}", event.getAuditData().getType(), event.getAuditData().asMap());
        Map<String, Object> dataMap = _config.getBucket().getAttributes("target_url", "zapier");
        URI targetUrl;
        try
        {
            targetUrl = new URI(dataMap.get("target_url").toString());
        }
        catch (URISyntaxException e)
        {
            throw new IllegalArgumentException("Invalid target url found");
        }

        HttpResponse tokenResponse = getWebServiceClient(targetUrl.getHost())
                .withPath(targetUrl.getPath())
                .request()
                .contentType("application/json")
                .body(HttpRequest.fromByteArray(_config.json().toJson(event.getAuditData().asMap()).getBytes()))
                .method("POST")
                .response();
        int statusCode = tokenResponse.statusCode();

        if (statusCode != 200)
        {
            if (_logger.isInfoEnabled())
            {
                _logger.info("Got error response while sending event to zapier: error = {}, {}", statusCode,
                        tokenResponse.body(HttpResponse.asString()));
            }

            throw _config.getExceptionFactory().internalServerException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
    }

    private WebServiceClient getWebServiceClient(String host)
    {
        Optional<HttpClient> httpClient = _config.getHttpClient();

        if (httpClient.isPresent())
        {
            return _config.getWebServiceClientFactory().create(httpClient.get()).withHost(host);
        }
        else
        {
            return _config.getWebServiceClientFactory().create(URI.create("https://" + host));
        }
    }
}
