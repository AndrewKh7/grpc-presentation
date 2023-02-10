package client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Player {
    private final Executor executor = Executors.newFixedThreadPool(1);

    public void playFrame(int i) {
        executor.execute( () -> {
            try {
                Thread.sleep(1000);
                System.out.println("playing: " + i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
