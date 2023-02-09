package server;

import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MusicServiceImpl extends MusicServiceGrpc.MusicServiceImplBase {
    private  final Map<String, int[]> songs = new ConcurrentHashMap<>();

    public MusicServiceImpl() {
        songs.put("10", new int[] {1,2,3,4,5,6,7,8,9});
        songs.put("100", new int[] {10,20,30,40,50,60,70,80,90});
    }

    @Override
    public void getPlaylist(music.api.v1.Music.PlayListRequest request,
                            io.grpc.stub.StreamObserver<music.api.v1.Music.PlayList> responseObserver) {
        System.out.println("Get playlist");
        var list = songs.keySet().stream()
                .map(k -> Music.SongDescription.newBuilder().setName(k).build())
                .collect(Collectors.toList());
        var playList = Music.PlayList.newBuilder().addAllSong(list).build();
        responseObserver.onNext(playList);
        responseObserver.onCompleted();
    }
}
