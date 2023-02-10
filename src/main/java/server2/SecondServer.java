package server2;

import io.grpc.ServerBuilder;
import server.MusicServiceImpl;

import java.io.IOException;

public class SecondServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        var server = ServerBuilder
                .forPort(9091)
                .addService(new MusicServiceDelegateImpl())
                .build()
                .start();
        System.out.println("Server 2 started!");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down.");
            if(server != null) {
                server.shutdown();
            }
        }));
        server.awaitTermination();
    }
}
