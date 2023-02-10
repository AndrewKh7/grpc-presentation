package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import javax.swing.*;
import java.lang.reflect.ParameterizedType;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final Client client = new Client();
    private static final Player player = new Player();
    public static void main(String[] args) throws InterruptedException {
        var ex = Executors.newFixedThreadPool(1);
        var channel = client.start();
        var stub = client.createStub(channel);
        var handler = client.playSong("Song100", stub);
        ex.submit(() -> {
            while (true) {
                if (player.busySpase() < 1) {
                    handler.requestData();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        new Scanner(System.in).next();
    }

    private ManagedChannel start() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    private MusicServiceGrpc.MusicServiceStub createStub(ManagedChannel channel) {
        return MusicServiceGrpc.newStub(channel);
    }

    private PlayStreamHandler playSong(String name, MusicServiceGrpc.MusicServiceStub stub) {
        var description = Music.SongDescription.newBuilder().setName(name).build();
        var obs = new PlayStreamHandler(player, name);
        stub.play(description, obs);
        return obs;
    }
}
