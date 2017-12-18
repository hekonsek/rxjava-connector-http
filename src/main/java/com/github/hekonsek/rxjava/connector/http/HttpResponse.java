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

import io.vertx.reactivex.core.buffer.Buffer;

import static io.vertx.core.json.Json.encodeToBuffer;

public class HttpResponse {

    private final int code;

    private final Buffer body;

    public HttpResponse(int code, Buffer body) {
        this.code = code;
        this.body = body;
    }

    public static HttpResponse httpResponse(Object response) {
        Buffer body = response == null ? Buffer.buffer() : new Buffer(encodeToBuffer(response));
        return new HttpResponse(200, body);
    }

    public int code() {
        return code;
    }

    public Buffer body() {
        return body;
    }

}