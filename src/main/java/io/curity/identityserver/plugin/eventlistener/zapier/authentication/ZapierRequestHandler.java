package io.curity.identityserver.plugin.eventlistener.zapier.authentication;

import io.curity.identityserver.plugin.eventlistener.zapier.config.AuthenticatorConfiguration;
import se.curity.identityserver.sdk.authentication.AuthenticationResult;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.web.Request;
import se.curity.identityserver.sdk.web.Response;

import java.util.Map;
import java.util.Optional;

public class ZapierRequestHandler implements AuthenticatorRequestHandler<Request>
{

    private final AuthenticatorConfiguration _configuration;

    ZapierRequestHandler(AuthenticatorConfiguration configuration)
    {
        _configuration = configuration;
    }

    @Override
    public Optional<AuthenticationResult> get(Request request, Response response)
    {
        return Optional.empty();
    }

    @Override
    public Optional<AuthenticationResult> post(Request request, Response response)
    {
        Map<String, Object> dataMap = _configuration.json().fromJson(request.getBodyAsString());
        if (dataMap.get("target_url") != null)
        {
            _configuration.bucketDataAccessProvider().storeAttributes("target_url", "zapier", dataMap);
        }
        return Optional.empty();
    }

    @Override
    public Request preProcess(Request request, Response response)
    {
        return request;
    }
}
