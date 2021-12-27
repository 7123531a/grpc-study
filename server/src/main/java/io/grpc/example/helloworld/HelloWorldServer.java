package io.grpc.example.helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

public class HelloWorldServer {

    private Server server;

    public void start(int port) throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if(server != null){
            server.awaitTermination();
        }
    }

    private static String determineHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        // Strange. Well, let's make an identifier for ourselves.
        return "generated-" + new Random().nextInt();
    }

    public static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply helloReply = HelloReply.newBuilder().setMessage(request.getName() + " from " + determineHostname()).build();
            responseObserver.onNext(helloReply);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer helloWorldServer = new HelloWorldServer();
        helloWorldServer.start(9000);
        helloWorldServer.blockUntilShutdown();
    }
}
