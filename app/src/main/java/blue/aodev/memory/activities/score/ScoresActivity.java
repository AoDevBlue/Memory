package blue.aodev.memory.activities.score;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.data.score.Score;
import blue.aodev.memory.data.score.ScoreDataSource;
import blue.aodev.memory.data.score.ScoreOrmLiteSource;

/**
 * Activity displaying the scores of the user.
 */
//FIXME Is it worth it to use MVP for such a simple activity?
public class ScoresActivity extends AppCompatActivity {

    @BindView(R.id.list) RecyclerView recyclerView;

    private ScoreDataSource scoreDataSource;

    private ScoreAdapter adapter;

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

        // Initialize the scores activity with an empty list
        adapter = new ScoreAdapter(Collections.<Score>emptyList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup the loader for the scores
        scoreDataSource = new ScoreOrmLiteSource(this);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<List<Score>>() {
            @Override
            public Loader<List<Score>> onCreateLoader(int id, Bundle args) {
                return scoreDataSource.getScoresLoader(ScoresActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<List<Score>> loader, List<Score> data) {
                adapter.replaceData(data);
            }

            @Override
            public void onLoaderReset(Loader<List<Score>> loader) {
                adapter.replaceData(Collections.<Score>emptyList());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scoreDataSource.close();
    }
}
