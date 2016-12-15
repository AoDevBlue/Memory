package blue.aodev.memory.activities.score;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.data.score.ScoreDataSource;
import blue.aodev.memory.data.score.ScoreOrmLiteSource;

/**
 * Activity displaying the scores of the user.
 */
//FIXME Is it worth it to use MVP for such a simple activity?
public class ScoresActivity extends AppCompatActivity {

    @BindView(R.id.list) RecyclerView recyclerView;

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ScoresActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.activity_scores_title));
        }

        ScoreDataSource scoreDataSource = new ScoreOrmLiteSource(this);
        List<Integer> scores = scoreDataSource.getScores();
        ScoreAdapter adapter = new ScoreAdapter(scores);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
