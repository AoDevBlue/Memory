package blue.aodev.memory.activities.game;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import blue.aodev.memory.R;

public class GameActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

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
        GamePresenter presenter = new GamePresenter(gameFragment);
    }
}
