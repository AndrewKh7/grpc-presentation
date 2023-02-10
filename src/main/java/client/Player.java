package client;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Player {
    private final Executor executor = Executors.newFixedThreadPool(1);
    private final Queue<Integer> buff = new ArrayBlockingQueue<>(50);
    private boolean playingFlag;

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
                    if (buff.isEmpty()) stop();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int busySpase() {
        return buff.size();
    }

    public void stop() {
        playingFlag = false;
    }
}
