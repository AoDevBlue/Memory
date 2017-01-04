package blue.aodev.memory.data.score;

import android.content.Context;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLitePreparedQueryLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
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
    public List<Score> getScores() {
        Dao<Score, Integer> dao = helper.getUserDao();
        List<Score> scores = Collections.emptyList();
        PreparedQuery<Score> preparedQuery = getScoresQuery(dao);
        if (preparedQuery != null) {
            try {
                scores = dao.query(preparedQuery);
            } catch (SQLException e) {
                Log.e(TAG, "Failed to query scores", e);
            }
        }
        return scores;
    }

    @Nullable
    @Override
    public Loader<List<Score>> getScoresLoader(@NonNull Context context) {
        Dao<Score, Integer> dao = helper.getUserDao();
        PreparedQuery<Score> preparedQuery = getScoresQuery(dao);
        if (preparedQuery != null) {
            return new OrmLitePreparedQueryLoader<>(context, dao, preparedQuery);
        } else {
            return null;
        }
    }

    @Nullable
    private PreparedQuery<Score> getScoresQuery(@Nullable Dao<Score, Integer> dao) {
        PreparedQuery<Score> preparedQuery = null;
        if (dao != null) {
            try {
                preparedQuery = dao.queryBuilder()
                        .orderBy(Score.SCORE_FIELD, false)
                        .prepare();
            } catch (SQLException e) {
                Log.e(TAG, "Failed to query scores", e);
            }
        }
        return preparedQuery;
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
