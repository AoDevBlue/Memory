package blue.aodev.memory.util;

import android.os.Handler;
import android.support.annotation.NonNull;

public class Timer {

    public interface Listener {
        void onUpdate(long elapsedTimeMs);
    }

    private Handler handler;
    private Runnable runnable;
    private long elapsedTime;
    private long lastTime;
    private final int updateRateMs;
    private Listener listener;

    public Timer(int updateRateMs, @NonNull final Listener listener) {
        this.updateRateMs = updateRateMs;
        this.listener = listener;

        handler = new Handler();
    }

    public void start() {
        stop();
        elapsedTime = 0;
        lastTime = System.currentTimeMillis();

        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                elapsedTime += currentTime - lastTime;
                lastTime = currentTime;
                listener.onUpdate(elapsedTime);

                handler.postDelayed(this, updateRateMs);
            }
        };
        handler.postDelayed(runnable, updateRateMs);
    }

    public void stop() {
        if (handler == null || runnable == null) {
            return;
        }
        handler.removeCallbacks(runnable);
        runnable = null;
    }

    public long getTotalTime() {
        return elapsedTime;
    }
}
