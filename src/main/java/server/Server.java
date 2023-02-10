package server;

import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder
                .forPort(9090)
                .addService(new MusicServiceImpl())
                .build()
                .start();
        System.out.println("Server started!");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down.");
            if(server != null) {
                server.shutdown();
            }
        }));
        server.awaitTermination();
    }
}
