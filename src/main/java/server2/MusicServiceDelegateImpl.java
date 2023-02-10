package server2;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ServerCallStreamObserver;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import javax.sound.midi.Soundbank;


public class MusicServiceDelegateImpl extends MusicServiceGrpc.MusicServiceImplBase{
    private MusicServiceGrpc.MusicServiceStub stub;

    public MusicServiceDelegateImpl() {
        var channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stub = MusicServiceGrpc.newStub(channel);
    }

    @Override
    public void getPlaylist(music.api.v1.Music.PlayListRequest request,
                            io.grpc.stub.StreamObserver<music.api.v1.Music.PlayList> responseObserver) {
        System.out.println("Get playList");
        stub.getPlaylist(request, responseObserver);
    }

    @Override
    public void play(music.api.v1.Music.SongDescription request,
                     io.grpc.stub.StreamObserver<music.api.v1.Music.Song> responseObserver) {

    }
}
