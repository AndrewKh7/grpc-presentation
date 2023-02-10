package client;

import io.grpc.ManagedChannel;
import io.grpc.stub.ClientResponseObserver;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Player {
    private final Executor executor = Executors.newFixedThreadPool(1);
    private final Queue<Integer> buff = new ArrayBlockingQueue<>(50);
    private final MusicServiceGrpc.MusicServiceStub stub;
    private PlayStreamHandler handler;
    private boolean playingFlag;

    public Player(ManagedChannel channel) {
        this.stub = MusicServiceGrpc.newStub(channel);
    }

    public void playSong(String name) {
        var description = Music.SongDescription.newBuilder().setName(name).build();
        this.handler = new PlayStreamHandler(this, name);
        stub.play(description, handler);
    }
    public void setFrame(int frame) {
        if (buff.size() == 50) while (!buff.isEmpty()) {};
        buff.offer(frame);
    }

    public void play() {
        if (playingFlag) return;
        playingFlag = true;
        executor.execute( () -> {
            try {
                while (playingFlag) {
                    Thread.sleep(200);
                    System.out.println("playing: " + buff.poll());
                    if (buff.isEmpty()) {
                        handler.requestData(100);
                        stop();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void stop() {
        playingFlag = false;
    }

    public void close() {
        stop();
        buff.clear();
    }
}
