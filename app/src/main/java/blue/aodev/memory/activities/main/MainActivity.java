package blue.aodev.memory.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.activities.game.GameActivity;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.new_game_button) Button newGameButton;
    @BindView(R.id.high_scores_button) Button highScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GameActivity.createIntent(MainActivity.this));
            }
        });

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Launch the score activity
                Toast.makeText(MainActivity.this, "TODO!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
