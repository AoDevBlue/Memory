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

    private enum State {
        STOPPED,
        PAUSED,
        STARTED;
    }

    private Handler handler;
    private Runnable runnable;
    private State state = State.STOPPED;

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

                if (state == State.STARTED) {
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
        if (state != State.STOPPED) {
            stop();
        }
        elapsedTime = 0;
        lastTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, updateRateMs);
        state = State.STARTED;
    }

    public void pause() {
        if (state != State.STARTED) {
            return;
        }
        handler.removeCallbacks(runnable);
        state = State.PAUSED;
    }

    public void resume() {
        if (state != State.PAUSED) {
            return;
        }
        lastTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, updateRateMs);
        state = State.STARTED;
    }

    public void stop() {
        if (state == State.STOPPED) {
            return;
        }
        handler.removeCallbacks(runnable);
        state = State.STOPPED;
    }
}
