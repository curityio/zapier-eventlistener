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

package io.curity.identityserver.plugin.eventlistener.zapier.descriptor;

import io.curity.identityserver.plugin.eventlistener.zapier.ZapierEventListener;
import io.curity.identityserver.plugin.eventlistener.zapier.config.EventListenerConfiguration;
import se.curity.identityserver.sdk.event.EventListener;
import se.curity.identityserver.sdk.event.EventListenerCollection;
import se.curity.identityserver.sdk.plugin.descriptor.EventListenerPluginDescriptor;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public final class ZapierEventListenerPluginDescriptor implements EventListenerPluginDescriptor<EventListenerConfiguration>
{
    @Override
    public Class<? extends EventListenerCollection> getEventListenerCollection()
    {
        return ZapierEventListenerCollection.class;
    }

    @Override
    public String getPluginImplementationType()
    {
        return "zapier";
    }

    @Override
    public Class<? extends EventListenerConfiguration> getConfigurationType()
    {
        return EventListenerConfiguration.class;
    }

    public static class ZapierEventListenerCollection implements EventListenerCollection
    {
        private final Set<EventListener<?>> _listeners;

        public ZapierEventListenerCollection(EventListenerConfiguration configuration)
        {
            _listeners = new HashSet<>(1);
            _listeners.add(new ZapierEventListener(configuration));
        }

        @Override
        public Set<? extends EventListener<?>> getListeners()
        {
            return unmodifiableSet(_listeners);
        }
    }

}