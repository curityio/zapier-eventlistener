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
import se.curity.identityserver.sdk.datasource.BucketDataAccessProvider;
import se.curity.identityserver.sdk.event.EventListener;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.Json;

public class ZapierEventListener implements EventListener<AuditableEvent>
{
    private static final Logger _logger = LoggerFactory.getLogger(ZapierEventListener.class);
    private final ExceptionFactory _exceptionFactory;
    private final BucketDataAccessProvider _bucketDataAccessProvider;
    private final Json _json;

    public ZapierEventListener(EventListenerConfiguration config)
    {
        _exceptionFactory = config.exceptionFactory();
        _bucketDataAccessProvider = config.bucketDataAccessProvider();
        _json = config.json();

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
    }
}
