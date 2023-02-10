import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.ManagedChannelBuilder;
import music.api.v1.Music;
import music.api.v1.MusicServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        var cdl = new CountDownLatch(100);
        var channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        var stub = MusicServiceGrpc.newFutureStub(channel);
        var start = System.currentTimeMillis();
        System.out.println("Get playlist: ");
        for (int i = 0; i < 100; i++) {
            var res = stub.getPlaylist(Music.PlayListRequest.newBuilder().build());
             res.addListener(cdl::countDown, MoreExecutors.directExecutor());
            Futures.addCallback(res, new FutureCallback() {
                @Override
                public void onSuccess(Object result) {
                    //get results;
                }

                @Override
                public void onFailure(Throwable t) {
                }
            }, MoreExecutors.directExecutor());
        }
        cdl.await();
        var end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start));
        channel.shutdown();

    }
}
