/**
 * Licensed to the RxJava Connector HTTP under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.rxjava.connector.http;

import com.github.hekonsek.rxjava.event.ReplyHandler;
import io.reactivex.Completable;
import io.vertx.reactivex.core.http.HttpServerRequest;

import static com.github.hekonsek.rxjava.connector.http.HttpResponse.httpResponse;
import static io.reactivex.Completable.complete;

public class VertxHttpReplyHandler implements ReplyHandler {

    private final HttpServerRequest request;

    public VertxHttpReplyHandler(HttpServerRequest request) {
        this.request = request;
    }

    @Override public Completable reply(Object response) {
        if(response instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) response;
            request.response().setStatusCode(httpResponse.code()).end(httpResponse.body());
            return complete();
        } else {
            HttpResponse httpResponse = httpResponse(response);
            request.response().setStatusCode(httpResponse.code()).end(httpResponse.body());
            return complete();
        }
    }

}