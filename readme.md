# RxJava HTTP connector

[![Version](https://img.shields.io/badge/RxJava%20Connector%20HTTP-0.1-blue.svg)](https://github.com/hekonsek/rxjava-connector-http/releases)
[![Build](https://api.travis-ci.org/hekonsek/rxjava-connector-http.svg)](https://travis-ci.org/hekonsek/rxjava-connector-http)
[![Coverage](https://sonarcloud.io/api/badges/measure?key=com.github.hekonsek%3Arxjava-connector-http&metric=coverage)](https://sonarcloud.io/component_measures?id=com.github.hekonsek%3Arxjava-connector-http&metric=coverage)

Connector for RxJava bridging HTTP endpoint with [RxJava events](https://github.com/hekonsek/rxjava-event).

## Installation

In order to start using Vert.x Pipes add the following dependency to your Maven project:

    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>rxjava-connector-http</artifactId>
      <version>0.1</version>
    </dependency>

## Usage

This is how you can start embedded Vert.x-based HTTP server and consume incoming requests:

```
HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
httpSourceFactory.build("/foo").build().subscribe(event ->
  System.out.println("POSTed JSON: " + event.payload())
);
httpSourceFactory.listen().subscribe();
```

If you would like to send a response back to the client, you need to obtain a response callback from
the incoming request event:

```
import com.github.hekonsek.rxjava.connector.http.HttpSourceFactory;
import static com.github.hekonsek.rxjava.event.Headers.requiredReplyHandler;

...

HttpSourceFactory httpSourceFactory = new HttpSourceFactory(vertx);
httpSourceFactory.build("/foo").build().subscribe(event ->
  requiredReplyHandler(event).reply(ImmutableMap.of("hello", ""world));
);
httpSourceFactory.listen().subscribe();
```

By default response objects are automatically converted to JSON.

## License

This project is distributed under Apache 2.0 license.
