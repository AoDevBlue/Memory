package blue.aodev.memory.activities.game;

import android.support.annotation.NonNull;

public class GamePresenter implements GameContract.Presenter {

    private GameContract.View view;

    public GamePresenter(@NonNull GameContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        //TODO Load data
    }

    @Override
    public void selectCard(Card card) {

    }
}
