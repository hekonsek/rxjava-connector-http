/**
 * Licensed to the RxJava Connector Kafka under one or more
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

import io.reactivex.Observable;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerRequest;

public class HttpSourceFactory {

    private final HttpServer server;

    private final Observable<HttpServerRequest> requests;

    public HttpSourceFactory(Vertx vertx) {
        server = vertx.createHttpServer();
        requests = server.requestStream().toObservable().publish().autoConnect();
    }

    public HttpSource build(String uri) {
        return new HttpSource(requests.filter(request -> request.uri().equals(uri)));
    }

    public void listen(int port) {
        server.listen(port);
    }

    public void listen() {
        server.listen(8080);
    }

}