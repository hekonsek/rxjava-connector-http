/**
 * Licensed to the RxJava Connector HTTP under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.rxjava.connector.http;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.reactivex.core.Vertx;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

import static com.github.hekonsek.rxjava.event.Headers.requiredReplyHandler;
import static io.vertx.core.json.Json.decodeValue;
import static io.vertx.reactivex.core.Vertx.vertx;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.Timeout.seconds;

@RunWith(VertxUnitRunner.class)
public class HttpSourceTest {

    @Rule
    public Timeout timeout = seconds(5);

    Vertx vertx = vertx();

    @Test
    public void shouldConsumePayload(TestContext context) {
        Async async = context.async();
        HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
        httpSourceFactory.build("/foo").build().subscribe(event -> async.complete());
        httpSourceFactory.listen(freePort()).subscribe(server ->
                vertx.createHttpClient().post(server.actualPort(), "localhost", "/foo").handler(response -> {
                }).end(new JsonObject().put("foo", "bar").toString()));
    }

    @Test
    public void shouldRouteRequest(TestContext context) {
        Async async = context.async(2);
        HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
        httpSourceFactory.build("/foo").build().subscribe(event -> async.countDown());
        httpSourceFactory.build("/bar").build().subscribe(event -> async.countDown());
        httpSourceFactory.listen(freePort()).subscribe(server -> {
            vertx.createHttpClient().post(server.actualPort(), "localhost", "/foo").handler(response -> {
                if (async.count() == 0) {
                    async.complete();
                }
            }).end(new JsonObject().put("foo", "bar").toString());
            vertx.createHttpClient().post(server.actualPort(), "localhost", "/bar").handler(response -> {
                if (async.count() == 0) {
                    async.complete();
                }
            }).end(new JsonObject().put("foo", "bar").toString());
        });
    }

    @Test
    public void shouldRespond(TestContext context) {
        Async async = context.async();
        HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
        httpSourceFactory.build("/foo").build().subscribe(event -> {
            requiredReplyHandler(event).reply(event.payload());
        });
        httpSourceFactory.listen(freePort()).subscribe(server ->
                vertx.createHttpClient().post(server.actualPort(), "localhost", "/foo").handler(response -> {
                    response.bodyHandler(body -> {
                        assertThat(decodeValue(body.getDelegate(), Map.class)).containsEntry("foo", "bar");
                        async.complete();
                    });
                }).end(new JsonObject().put("foo", "bar").toString()));
    }

    private int freePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}