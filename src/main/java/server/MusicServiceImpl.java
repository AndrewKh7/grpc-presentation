package server;

import io.grpc.ServerCall;
import io.grpc.stub.ServerCallStreamObserver;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MusicServiceImpl extends MusicServiceGrpc.MusicServiceImplBase {
    private  final Map<String, int[]> songs = new ConcurrentHashMap<>();

    public MusicServiceImpl() {
        songs.put("Song10", new int[] {1,2,3,4,5,6,7,8,9});
        songs.put("Song100", new int[] {10,20,30,40,50,60,70,80,90});
        var longSong = new int[10_000_000];
        for (int i = 0; i < 10_000_000; i++) longSong[i] = 1000 + i + 1;
        songs.put("Song100", longSong);
    }

    @Override
    public void getPlaylist(music.api.v1.Music.PlayListRequest request,
                            io.grpc.stub.StreamObserver<music.api.v1.Music.PlayList> responseObserver) {
        System.out.println("Get playlist");
        var list = songs.keySet().stream()
                .map(k -> Music.SongDescription.newBuilder().setName(k).build())
                .collect(Collectors.toList());
        var playList = Music.PlayList.newBuilder().addAllSong(list).build();
        work((ServerCallStreamObserver) responseObserver);
        responseObserver.onNext(playList);
        responseObserver.onCompleted();
    }

    @Override
    public void play(music.api.v1.Music.SongDescription request,
                     io.grpc.stub.StreamObserver<music.api.v1.Music.Song> responseObserver) {
        var observer = (ServerCallStreamObserver<Music.Song>) responseObserver;
        observer.disableAutoRequest();
        observer.setOnCancelHandler(() -> System.out.println("Cancel stream"));
        observer.setOnCloseHandler(() -> System.out.println("Close handler"));
        var song = songs.get(request.getName());
        for(Integer i: song) {
            if (i> 1050) work(observer);
            System.out.println(request.getName() + ": " + i);
            responseObserver.onNext(Music.Song.newBuilder().setFrame(i).build());

        };
        observer.onCompleted();
    }

    private void work(ServerCallStreamObserver obs) {
        for(int i = 0; i < 100; i++) {
            try {
                Thread.sleep(200);
                if (obs.isCancelled()) {
                    System.out.println("Canceled in work");
                    throw  new InterruptedException("Canceled work by client");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
