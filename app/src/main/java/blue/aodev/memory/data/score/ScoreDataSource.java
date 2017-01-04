package blue.aodev.memory.data.score;

import android.content.Context;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * A data source for scores.
 */
public interface ScoreDataSource {

    @NonNull List<Score> getScores();

    @Nullable Loader<List<Score>> getScoresLoader(@NonNull Context context);

    void addScore(int score);

    void close();
}
