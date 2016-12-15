package blue.aodev.memory.data.score;

import android.support.annotation.NonNull;

import java.util.List;

public interface ScoreDataSource {

    @NonNull List<Integer> getScores();

    void addScore(int score);

    void close();
}
