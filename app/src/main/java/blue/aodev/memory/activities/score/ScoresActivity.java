package blue.aodev.memory.activities.score;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import blue.aodev.memory.R;

/**
 * Activity displaying the scores of the user.
 */
//FIXME Is it worth it to use MVP for such a simple activity?
public class ScoresActivity extends AppCompatActivity {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ScoresActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.activity_scores_title));
        }
    }
}
