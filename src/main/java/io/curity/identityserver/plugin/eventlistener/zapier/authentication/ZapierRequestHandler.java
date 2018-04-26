package io.curity.identityserver.plugin.eventlistener.zapier.authentication;

import io.curity.identityserver.plugin.eventlistener.zapier.config.AuthenticatorConfiguration;
import se.curity.identityserver.sdk.authentication.AnonymousRequestHandler;
import se.curity.identityserver.sdk.web.Request;
import se.curity.identityserver.sdk.web.Response;
import se.curity.identityserver.sdk.web.ResponseModel;

import java.util.HashMap;
import java.util.Map;

public class ZapierRequestHandler implements AnonymousRequestHandler<Request>
{

    private final AuthenticatorConfiguration _configuration;

    public ZapierRequestHandler(AuthenticatorConfiguration configuration)
    {
        _configuration = configuration;
    }


    @Override
    public Request preProcess(Request request, Response response)
    {
        return request;
    }

    @Override
    public Void get(Request request, Response response)
    {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("type", "AccountCreatedEvent");
        dataMap.put("data", "Event Data");
        response.setResponseModel(ResponseModel.mapResponseModel(dataMap),
                Response.ResponseModelScope.ANY);

        return null;
    }

    @Override
    public Void post(Request request, Response response)
    {
        Map<String, Object> dataMap = _configuration.json().fromJson(request.getBodyAsString());
        if (dataMap.get("target_url") != null)
        {
            _configuration.bucket().storeAttributes("target_url", "zapier", dataMap);
        }
        else if ((Boolean) dataMap.get("unsubscribe"))
        {
            _configuration.bucket().clearBucket("target_url", "zapier");
        }

        return null;
    }
}
