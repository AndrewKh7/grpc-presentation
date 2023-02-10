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
        System.out.println(player.getPlayList());


        new Scanner(System.in).next();
    }

    private ManagedChannel start() {
        return ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
    }

}
