package blue.aodev.memory.data.score;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link ScoreDataSource} using the {@link ScoreDbOpenHelper}.
 */
public class ScoreOrmLiteSource implements ScoreDataSource {

    private static final String TAG = "ScoreORMLiteSource";

    private ScoreDbOpenHelper helper;

    public ScoreOrmLiteSource(@NonNull Context context) {
        helper = new ScoreDbOpenHelper(context);
    }

    @Override
    @NonNull
    public List<Integer> getScores() {
        Dao<Score, Integer> dao = helper.getUserDao();
        List<Score> scores = Collections.emptyList();
        if (dao != null) {
            try {
                scores = dao.queryBuilder()
                        .orderBy(Score.SCORE_FIELD, false)
                        .query();
            } catch (SQLException e) {
                Log.e(TAG, "Failed to query scores", e);
            }
        }

        // Using a stream to transform the data would be nice here
        List<Integer> result = new ArrayList<>(scores.size());
        for (Score score : scores) {
            result.add(score.getScore());
        }
        return result;
    }

    @Override
    public void addScore(int score) {
        Score scoreObj = new Score();
        scoreObj.setScore(score);

        Dao<Score, Integer> dao = helper.getUserDao();
        if (dao != null) {
            try {
                dao.create(scoreObj);
            } catch (SQLException e) {
                Log.e(TAG, "Failed to store score", e);
            }
        }
    }

    @Override
    public void close() {
        helper.close();
    }
}
