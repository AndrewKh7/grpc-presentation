package client;

import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;

public class SongClientStreamObserver implements ClientResponseObserver {
    @Override
    public void beforeStart(ClientCallStreamObserver requestStream) {

    }

    @Override
    public void onNext(Object value) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }
}
