package blue.aodev.memory.data.score;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A score, stored using ORMLite.
 */
@DatabaseTable(tableName = "score")
public class Score {

    public static final String SCORE_FIELD = "score";

    // We don't need an id for our usage of scores,
    // but we would need to include one later.
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = SCORE_FIELD)
    private int score;

    public Score() {
        // Needed empty constructor
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
