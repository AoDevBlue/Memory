package blue.aodev.memory.util;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * A simple timer to get repeated updates and measure time from its start.
 * FIXME: This timer is not thread safe and lacking testing.
 */
public class Timer {

    public interface Listener {
        void onUpdate(long elapsedTimeMs);
    }

    private Handler handler;
    private Runnable runnable;
    private boolean running;

    private long elapsedTime;
    private long lastTime;

    private final int updateRateMs;


    public Timer(final int updateRateMs, @NonNull final Listener listener) {
        this.updateRateMs = updateRateMs;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = SystemClock.uptimeMillis();
                elapsedTime += currentTime - lastTime;
                lastTime = currentTime;
                listener.onUpdate(elapsedTime);

                if (running) {
                    handler.postDelayed(this, updateRateMs);
                }
            }
        };
    }

    public long getTotalTime() {
        return elapsedTime;
    }

    public void offset(long offset) {
        elapsedTime += offset;
    }

    public void start() {
        if (running) {
            return;
        }
        lastTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, updateRateMs);
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        handler.removeCallbacks(runnable);
        running = false;
    }

    public void reset() {
        elapsedTime = 0;
    }
}
