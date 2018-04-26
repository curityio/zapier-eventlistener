/*
 *  Copyright 2017 Curity AB
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

import io.curity.identityserver.plugin.eventlistener.zapier.authentication.ZapierRequestHandler;
import io.curity.identityserver.plugin.eventlistener.zapier.config.AuthenticatorConfiguration;
import se.curity.identityserver.sdk.authentication.AnonymousRequestHandler;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.plugin.descriptor.AuthenticatorPluginDescriptor;

import java.util.Collections;
import java.util.Map;

public final class ZapierAuthenticatorPluginDescriptor
        implements AuthenticatorPluginDescriptor<AuthenticatorConfiguration>
{
    @Override
    public String getPluginImplementationType()
    {
        return "zapier";
    }

    @Override
    public Class<? extends AuthenticatorConfiguration> getConfigurationType()
    {
        return AuthenticatorConfiguration.class;
    }

    @Override
    public Map<String, Class<? extends AuthenticatorRequestHandler<?>>> getAuthenticationRequestHandlerTypes()
    {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Class<? extends AnonymousRequestHandler<?>>> getAnonymousRequestHandlerTypes()
    {
        return Collections.unmodifiableMap(Collections.singletonMap("index", ZapierRequestHandler.class));
    }
}
