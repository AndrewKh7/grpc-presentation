import io.grpc.ManagedChannelBuilder;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

public class Client {

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        var stub = MusicServiceGrpc.newBlockingStub(channel);
        System.out.println("Get playlist: ");
        var start = System.currentTimeMillis();
        for (int i = 0; i <= 100; i++)
            stub.getPlaylist(Music.PlayListRequest.newBuilder().build());
        var end = System.currentTimeMillis();
        System.out.println("TIme: " + (end - start));
    }
}
