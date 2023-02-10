package client;

import io.grpc.Context;
import io.grpc.ManagedChannel;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Player {
    private final Executor executor = Executors.newFixedThreadPool(1);
    private final Queue<Integer> buff = new ArrayBlockingQueue<>(50);
    private final MusicServiceGrpc.MusicServiceStub asyncStub;
    private PlayStreamHandler handler;
    private ManagedChannel channel;
    private Context.CancellableContext context;
    private boolean playingFlag;

    public Player(ManagedChannel channel) {
        this.channel = channel;
        this.asyncStub = MusicServiceGrpc.newStub(channel);
    }

    public void playSong(String name) {
        var description = Music.SongDescription.newBuilder().setName(name).build();
        this.handler = new PlayStreamHandler(this, name);
        this.context = Context.current().withCancellation();
        context.run(() -> asyncStub.play(description, handler));
    }
    public void setFrame(int frame) {
        if (buff.size() == 50) while (!buff.isEmpty() || handler == null) {};
        buff.offer(frame);
    }
    public List<Music.SongDescription> getPlayList() {
        return MusicServiceGrpc
                .newBlockingStub(channel)
                .withDeadlineAfter(1, TimeUnit.SECONDS)
                .getPlaylist(Music.PlayListRequest.newBuilder().build()).getSongList();
    }

    public void play() {
        if (playingFlag || handler == null) return;
        playingFlag = true;
        executor.execute( () -> {
            try {
                while (playingFlag) {
                    Thread.sleep(200);
                    System.out.println("playing: " + buff.poll());
                    if (buff.isEmpty()) {
                        if (handler != null) handler.requestData(100);
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
        context.cancel(null);
        buff.clear();
    }
}
