package blue.aodev.memory.data.score;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Helper for the score database.
 */
public class ScoreDbOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "ScoreDbOpenHelper";
    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 1;

    private Dao<Score, Integer> scoreDao;

    public ScoreDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Score.class);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to create score table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // Nothing to to here for the moment
    }

    public @Nullable Dao<Score, Integer> getUserDao() {
        if (scoreDao == null) {
            try {
                scoreDao = getDao(Score.class);
            } catch (SQLException e) {
                Log.e(TAG, "Failed to create score dao", e);
            }
        }

        return scoreDao;
    }

    @Override
    public void close() {
        scoreDao = null;
        super.close();
    }
}
