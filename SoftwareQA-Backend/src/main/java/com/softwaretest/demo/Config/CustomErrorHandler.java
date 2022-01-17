package com.softwaretest.demo.Config;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomErrorHandler  implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getRawStatusCode() != 200;
    }

    @Override
    public void handleError(ClientHttpResponse response)  {

    }
}
