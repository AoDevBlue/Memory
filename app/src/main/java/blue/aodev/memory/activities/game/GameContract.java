package blue.aodev.memory.activities.game;

import android.support.annotation.NonNull;

import blue.aodev.memory.BasePresenter;
import blue.aodev.memory.BaseView;

public interface GameContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void showLoadingError();

        void showGame();

        float getBoardAspectRatio();

        void setBoardSize(int rowCount, int columnCount);

        void hideCard(int row, int column, @NonNull Card.Type type);

        void showCard(int row, int column, @NonNull Card.Type type, @NonNull String text);

        void setFlipCount(int flipCount);

        void setTime(int time);

        void showScore(int score);

        void showRetry();

        void showScores();
    }

    interface Presenter extends BasePresenter {
        void selectCard(int row, int column);

        void newGame();

        void retryLoading();

        // loadState and saveState would be here if we had state to save
    }
}
