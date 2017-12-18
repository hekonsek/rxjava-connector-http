package com.github.hekonsek.rxjava.connector.http;

import com.github.hekonsek.rxjava.event.ReplyHandler;
import io.reactivex.Completable;
import io.vertx.reactivex.core.http.HttpServerRequest;

import static io.reactivex.Completable.complete;
import static io.vertx.core.json.Json.encode;

public class VertxHttpReplyHandler implements ReplyHandler {

    private final HttpServerRequest request;

    public VertxHttpReplyHandler(HttpServerRequest request) {
        this.request = request;
    }

    @Override public Completable reply(Object response) {
        request.response().end(encode(response));
        return complete();
    }

}