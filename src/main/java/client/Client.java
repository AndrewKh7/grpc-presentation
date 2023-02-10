package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.concurrent.Executors;


public class Client {
    private static Client client = new Client();
    private static Player player;
    public static void main(String[] args) throws InterruptedException {
        var channel = client.start();
        player = new Player(channel);
        player.playSong("Song100");
        Thread.sleep(5000);
        player.close();


        new Scanner(System.in).next();
    }

    private ManagedChannel start() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

}
