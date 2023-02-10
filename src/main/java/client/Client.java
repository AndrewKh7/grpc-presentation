package client;

import io.grpc.ManagedChannelBuilder;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

public class Client {

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .intercept(new ClientRequestInterceptor())
                .build();
        var stub = MusicServiceGrpc.newBlockingStub(channel);
        System.out.println("Get playlist: ");
        System.out.println(stub.getPlaylist(Music.PlayListRequest.newBuilder().build()));
    }
}
