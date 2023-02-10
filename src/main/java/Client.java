import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        var cdl  = new CountDownLatch(100);
        var channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        var stub = MusicServiceGrpc.newStub(channel);
        var start = System.currentTimeMillis();
        System.out.println("Get playlist: ");
        for (int i = 0; i <= 100; i++)
            stub.getPlaylist(Music.PlayListRequest.newBuilder().build(), new StreamObserver<>() {

                @Override
                public void onNext(Music.PlayList value) {
                }

                @Override
                public void onError(Throwable t) {
                }

                @Override
                public void onCompleted() {
                    cdl.countDown();
                }
            });
        cdl.await();
        var end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start));
    }
}
