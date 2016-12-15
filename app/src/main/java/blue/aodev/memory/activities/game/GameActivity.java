package blue.aodev.memory.activities.game;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import blue.aodev.memory.R;
import blue.aodev.memory.data.local.ScoreDataSource;
import blue.aodev.memory.data.local.ScoreOrmLiteSource;

public class GameActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    private ScoreDataSource scoreDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameFragment gameFragment =
                (GameFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (gameFragment == null) {
            // Create the fragment
            gameFragment = GameFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, gameFragment);
            transaction.commit();
        }

        // Create the presenter
        scoreDataSource = new ScoreOrmLiteSource(this);
        GamePresenter presenter = new GamePresenter(gameFragment, scoreDataSource);
    }

    @Override
    protected void onStop() {
        super.onStop();
        scoreDataSource.close();
    }
}
