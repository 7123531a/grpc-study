package io.grpc.example.helloworld;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloWorldClient {

    private GreeterGrpc.GreeterBlockingStub blockingStub;
    private GreeterGrpc.GreeterFutureStub greeterFutureStub;

    public HelloWorldClient(Channel channel) {
        blockingStub = GreeterGrpc.newBlockingStub(channel);
        greeterFutureStub = GreeterGrpc.newFutureStub(channel);
    }

    public void sayHello() {
        HelloRequest request = HelloRequest.newBuilder().setName("hi eric").build();
        HelloReply reply = blockingStub.sayHello(request);
        System.out.println(reply.getMessage());
    }

    public void asyncSayhello() {
        HelloRequest request = HelloRequest.newBuilder().setName("hi eric").build();
        Futures.addCallback(greeterFutureStub.sayHello(request), new FutureCallback<HelloReply>() {
            @Override
            public void onSuccess(@NullableDecl HelloReply result) {
                System.out.println("ASYC:" + result.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, Executors.newSingleThreadExecutor());
    }

    public static void main(String[] args) throws InterruptedException {
        String target = "localhost:9000";

//        System.setProperty("networkaddress.cache.ttl", "0");

        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .defaultLoadBalancingPolicy("round_robin")
                .build();

        HelloWorldClient helloWorldClient = new HelloWorldClient(channel);

//        helloWorldClient.asyncSayhello();


        int i = 0;
        while (i++ < 500) {
            helloWorldClient.sayHello();
        }

        channel.shutdownNow().awaitTermination(5, TimeUnit.MINUTES);
    }
}
