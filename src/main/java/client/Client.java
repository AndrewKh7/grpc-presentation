package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final Client client = new Client();
    private static final Player player = new Player();
    public static void main(String[] args) throws InterruptedException {
        var channel = client.start();
        var stub = client.createStub(channel);
        client.playSong("Song10", stub);
        channel.awaitTermination(5, TimeUnit.SECONDS);
    }

    private ManagedChannel start() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .intercept(new ClientInterceptor())
                .build();
    }

    private MusicServiceGrpc.MusicServiceStub createStub(ManagedChannel channel) {
        return MusicServiceGrpc.newStub(channel);
    }

    private void playSong(String name, MusicServiceGrpc.MusicServiceStub stub) {
        var description = Music.SongDescription.newBuilder().setName(name).build();
        stub.play(description, new PlayStreamHandler(player, name));
    }
}
