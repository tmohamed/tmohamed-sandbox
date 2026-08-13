# Spring Boot 4.1 gRPC Demo

gRPC has always been a bit of a pain to set up in Java. Proto files, code generation, build plugins, server wiring, client channels. A lot of busywork before you ever reach "hello world."

Spring Boot 4.1 fixes most of that. If you've built a REST app with Spring Boot, you already know the moves: define a contract, drop in a `@GrpcService` bean, inject a stub on the client. We'll build a gRPC server and client from scratch so you can see how little is left.

## What we're building

A `greeter-server` that exposes a `Greeter` service on port 9090, and a `greeter-client` that calls it through an injected stub. Then a test that runs the whole round trip in memory, no network and no second process.

The theme to watch for: gRPC in Boot 4.1 follows the same patterns you already use. Beans, properties, test slices. Nothing new to learn, just a new transport.

## Before you start

You'll need Java 17+ (Boot 4 requires it) and Maven. I'll note the Gradle differences as we go.

Grab [grpcurl](https://github.com/fullstorydev/grpcurl) too (`brew install grpcurl` on a Mac). It's like `curl` for gRPC, and it lets us hit the server before we've written any client code. That's the part that usually gets a reaction.
1
## Step 0: Generate the projects

Go to [start.spring.io](https://start.spring.io) and create two projects. Add the **gRPC Server** dependency to `greeter-server` and **gRPC Client** to `greeter-client`. Java 17+, Maven, defaults for everything else.

Leave `spring-boot-starter-parent` as the parent (it's already the default). The parent pins the `protoc` and `grpc-java` versions and configures the build plugin, which saves you the version juggling that eats the first twenty minutes of most gRPC tutorials.

Two projects keeps things clear, especially side by side in a recording. A multi-module setup works too if you'd rather.

## Step 1: The build plugin is already there

A `.proto` file becomes Java through a plugin that runs `protoc`. When you picked the gRPC dependency, Initializr already added it. Nothing to do here, but it's worth knowing where it lives. Open `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.ascopes</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

No version, no config, no execution block. That all comes from the parent.

On Gradle it's the same story. The generated `build.gradle` already applies the protobuf plugin next to the Boot plugin:

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.1.0'
    id 'io.spring.dependency-management' version '1.1.7'
    id 'com.google.protobuf' version '0.9.6'
}
```

## Step 2: Define the contract

The `.proto` file is the single source of truth. It describes the service and its messages, and it's language neutral, so the same file could generate a Go client or a Python server.

Add this at `src/main/proto/greeter.proto`:

```protobuf
syntax = "proto3";

option java_package = "dev.danvega.greeter";
option java_multiple_files = true;

service Greeter {
    rpc SayHello (HelloRequest) returns (HelloReply) {}
}

message HelloRequest {
    string name = 1;
}

message HelloReply {
    string message = 1;
}
```

One `Greeter` service with a single `SayHello` method. It takes a name and returns a message. The two `option` lines only shape the generated Java; everything else is portable.

Now build once to generate the code:

```shell
./mvnw clean compile      # Maven
./gradlew build           # Gradle
```

## Step 3: Yes, you import from the target folder

This one trips everybody up, so let's get it out of the way before we write any code.

After that build, look in `target/generated-sources/protobuf/`. You'll see a package matching your `java_package` (`dev/danvega/greeter/`) holding `GreeterGrpc`, `HelloRequest`, `HelloReply`, and a couple of `OrBuilder` helpers. We're about to `import` these, and yes, they live under `target/`. That feels wrong the first time.

It works because the plugin doesn't just write those files, it also registers `target/generated-sources/protobuf/java` as a source root. So `javac` compiles your code in `src/main/java` and the generated code together as one set. By the time your class compiles, `HelloRequest` already exists on the source path.

The way to think about it: the proto is your source, the generated Java is build output like `.class` files, and you never edit or commit it. It regenerates on every build. Same idea as MapStruct, Lombok, or Avro.

Two things that save headaches:

`./mvnw clean` wipes the generated code and the next build recreates it, so never hand-edit those files.

If the IDE flags those imports red even though the build passes, it's an indexing lag, not a real error. The classes exist, they compiled, the app runs. IntelliJ just hasn't registered the source root yet. Open the Maven panel and hit reload. If it sticks, right-click `target/generated-sources/protobuf` and choose Mark Directory As, Generated Sources Root. The rule of thumb: a red squiggle on a generated class is almost never the build, it's the editor catching up.

## Step 4: Implement the server

This is the whole point, and it's almost too easy. Extend the generated base class and mark it `@GrpcService`:

```java
package dev.danvega.greeter;

import com.mohamed.tamer.fileupload.GreeterGrpc;
import com.mohamed.tamer.fileupload.HelloReply;
import com.mohamed.tamer.fileupload.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String message = "Hello '%s'".formatted(request.getName());
        HelloReply reply = HelloReply.newBuilder()
                .setMessage(message)
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
```

That's the service. It's a plain Spring bean, so component scanning picks it up, and Spring gRPC exposes any bean that implements `BindableService`. Every generated `ImplBase` already does, so adding the bean is all it takes. No registration step.

And since it's an ordinary component, it behaves like one. Need a repository or another service? Constructor-inject it like always.

Run it:

```shell
./mvnw spring-boot:run
```

Netty starts on port 9090 with zero configuration from you.

## Step 5: Call it before writing a client

Here's the fun bit. The server is up and we haven't written a client. We don't need one yet:

```shell
grpcurl -d '{"name":"Spring"}' -plaintext localhost:9090 Greeter.SayHello
```

```json
{
  "message": "Hello 'Spring'"
}
```

Want to go further? Add the gRPC services library to the server `pom.xml`:

```xml
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-services</artifactId>
</dependency>
```

Restart, and now grpcurl can discover the services on its own, no proto needed:

```shell
grpcurl -plaintext localhost:9090 list
```

You didn't enable reflection. Boot auto-configured it the moment that jar landed on the classpath. (Turn it off with `spring.grpc.server.reflection.enabled=false` if you ever need to.)

## Step 6: Build the client

Now switch to `greeter-client`. Before the code, one thing that surprises people: the client knows nothing about the server's Java classes.

So how does it compile against `GreeterGrpc`? It generates its own copy. Drop the same `greeter.proto` into the client's `src/main/proto`, and its own protobuf plugin produces its own `GreeterGrpc`, `HelloRequest`, and `HelloReply`. The two projects never share Java. They share the contract.

That's the heart of gRPC. The proto is the only thing both sides agree on, and Protocol Buffers is a binary format that doesn't care about class names. The field numbers in the proto (`name = 1`) are what line up on the wire. This is exactly why a Python client can talk to a Java server. Your Java-to-Java case is the same mechanism; it just looks redundant because both ends are the same language.

The catch worth saying out loud: two copies of the proto means two things to keep in sync. Fine for a demo. In production, teams usually keep the proto in one place, a shared module or a registry like Buf, so it can't drift.

With that settled, the client is two steps. First, tell Spring which stub you want:

```java
package dev.danvega.greeterclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

import com.mohamed.tamer.fileupload.GreeterGrpc;

@SpringBootApplication
@ImportGrpcClients(target = "greeter", types = GreeterGrpc.GreeterBlockingStub.class)
public class GreeterClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreeterClientApplication.class, args);
    }
}
```

Second, point the `greeter` channel at the server in `application.yaml`:

```yaml
spring:
  grpc:
    client:
      channel:
        greeter:
          target: "static://localhost:9090"
```

Naming the channel and setting its address in config is the recommended approach, the same way you'd name a datasource instead of hard-coding a URL everywhere.

The stub is now just a bean. Inject it and call it. Here it is in a `CommandLineRunner` that fires on startup:

```java
@Bean
CommandLineRunner runner(GreeterGrpc.GreeterBlockingStub greeterStub) {
    return args -> {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Dan Vega")
                .build();
        HelloReply reply = greeterStub.sayHello(request);
        System.out.println(reply.getMessage());
    };
}
```

Leave the server running, start the client, and you'll see the greeting print. A full round trip across two processes.

Want a more realistic shape? Inject that same stub into a `@RestController` and you've got REST in, gRPC out, a gateway in about ten lines.

## Step 7: Test it with a slice

Let's finish strong. `@AutoConfigureTestGrpcTransport` swaps the real channel for an in-process one. No port to bind, no separate server, fast and reliable on any CI box. If you've used `@WebMvcTest`, this feels the same.

This test goes in the **server** project. It needs the real `FileUploadService` on the classpath, and that only lives there. The test boots the server's context, wires an in-process channel, and calls the service through a stub, both ends inside one JVM. The `@ImportGrpcClients` here is just how you get a stub to call your own service; it isn't a second client app.

Add the test starter to the server:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-grpc-server-test</artifactId>
    <scope>test</scope>
</dependency>
```

Then the test:

```java
package dev.danvega.greeter;

import com.mohamed.tamer.fileupload.GreeterGrpc;
import com.mohamed.tamer.fileupload.HelloReply;
import com.mohamed.tamer.fileupload.HelloRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureTestGrpcTransport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.grpc.client.ImportGrpcClients;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestGrpcTransport
@ImportGrpcClients(types = GreeterGrpc.GreeterBlockingStub.class)
class GreeterServiceTests {

    @Autowired
    private GreeterGrpc.GreeterBlockingStub greeterStub;

    @Test
    void sayHello() {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Spring")
                .build();
        HelloReply reply = greeterStub.sayHello(request);
        assertThat(reply.getMessage()).isEqualTo("Hello 'Spring'");
    }
}
```

You're testing the real serialization round trip, proto marshalling and all, without binding a port. Same payoff as `@WebMvcTest` giving you the web layer without a running Tomcat.

If you'd rather test against a real socket, set `spring.grpc.server.port=0` for a random port and read it back with `@LocalGrpcServerPort`.

## Where to go next

Each of these is a small change on top of what you've built.

**Share a port with REST.** Swap Netty for the servlet implementation (`grpc-servlet-jakarta` plus `spring-boot-starter-webmvc`, with `server.http2.enabled=true`) and gRPC rides on the same Tomcat port as your controllers. A nice story for adding gRPC to an existing app. Note that `spring.grpc.server.port` is ignored in servlet mode; `server.port` wins.

**Add security.** Boot auto-configures `GrpcSecurity`, so `@PreAuthorize` works right on service methods, and OAuth2 Resource Server configures the usual way. The security model you already know.

**Add health checks.** Drop in `spring-boot-health` (with `grpc-services`) and Spring gRPC bridges your Spring Boot health indicators to the standard gRPC health service — no Actuator endpoint required.

## When things go sideways

| Symptom | What's happening |
| --- | --- |
| Generated classes not found | Run a build and confirm the proto is under `src/main/proto`. Output lands in `target/generated-sources/protobuf/`. See Step 3. |
| Imports red but the app runs | IDE indexing lag. Reload the Maven panel, or mark the generated folder as a sources root. Not a build error. |
| `@ImportGrpcClients` can't find the stub | The generated package must match `java_package`. Use `basePackages` to import everything in a package. |
| Client can't connect | Check the server is on 9090 and the channel `target` matches. |
| Netty version clash | Exclude `io.grpc:grpc-netty` and add `io.grpc:grpc-netty-shaded`. Works on both starters. |

One last tip: do a clean run from freshly downloaded Initializr projects before you present this. The plugin and parent setup is smooth when it works and a time sink when it doesn't, so hit any snags in private.

## Links

- [Spring Boot 4.1 gRPC reference](https://docs.spring.io/spring-boot/reference/io/grpc.html)
- [Spring gRPC docs](https://docs.spring.io/spring-grpc/reference/)
- [grpcurl](https://github.com/fullstorydev/grpcurl)
- [Protocol Buffers (proto3)](https://protobuf.dev/programming-guides/proto3/)
