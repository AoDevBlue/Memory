package blue.aodev.memory.activities.game;

import android.os.Handler;
import android.support.annotation.NonNull;

public class GamePresenter implements GameContract.Presenter {

    private GameContract.View view;

    public GamePresenter(@NonNull GameContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        //TODO Load data

        //Go to the scores activity after 3s to test navigation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.showScores();
            }
        }, 3000);
    }

    @Override
    public void selectCard(Card card) {

    }

    @Override
    public void retry() {

    }
}
