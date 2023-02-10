package client;

import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import music.api.v1.Music;

public class PlayStreamHandler implements ClientResponseObserver<Music.SongDescription, Music.Song> {
    private final Player player;
    private final String songName;
    private ClientCallStreamObserver requestStream;

    public PlayStreamHandler(Player player, String songName) {
        this.player = player;
        this.songName = songName;
    }

    @Override
    public void beforeStart(ClientCallStreamObserver requestStream) {
        this.requestStream = requestStream;
        requestStream.disableAutoRequestWithInitial(10);
    }

    @Override
    public void onNext(Music.Song value) {
        try {
            Thread.sleep(10);
            player.setFrame(value.getFrame());
            player.play();
            System.out.println("Get frame " + value.getFrame() + " for song " + songName);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("invoke on Error");
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("Playing completed: " + songName);
    }

    public void requestData(int cnt) {
             requestStream.request(cnt);
    }

    public void close() {
        requestStream.onError(null);
        requestStream.cancel("CANCEL", null);
    }
}
