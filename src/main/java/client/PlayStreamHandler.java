package client;

import io.grpc.stub.StreamObserver;
import music.api.v1.Music;

public class PlayStreamHandler implements StreamObserver<Music.Song> {
    private final Player player;
    private final String songName;

    public PlayStreamHandler(Player player, String songName) {
        this.player = player;
        this.songName = songName;
    }

    @Override
    public void onNext(Music.Song value) {
        try {
            Thread.sleep(100);
            System.out.println("Get frame " + value.getFrame() + " for song " + songName);
            player.playFrame(value.getFrame());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {
        System.out.println("Playing completed: " + songName);
    }
}
