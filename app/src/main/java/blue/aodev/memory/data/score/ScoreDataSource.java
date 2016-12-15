package blue.aodev.memory.data.score;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * A data source for scores.
 */
public interface ScoreDataSource {

    @NonNull List<Integer> getScores();

    void addScore(int score);

    void close();
}
