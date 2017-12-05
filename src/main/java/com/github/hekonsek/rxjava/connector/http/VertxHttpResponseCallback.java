package com.github.hekonsek.rxjava.connector.http;

import com.github.hekonsek.rxjava.event.ResponseCallback;
import io.vertx.reactivex.core.http.HttpServerRequest;

import static io.vertx.core.json.Json.encode;

public class VertxHttpResponseCallback implements ResponseCallback {

    private final HttpServerRequest request;

    public VertxHttpResponseCallback(HttpServerRequest request) {
        this.request = request;
    }

    @Override public void respond(Object response) {
        request.response().end(encode(response));
    }

}