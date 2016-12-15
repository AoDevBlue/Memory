package blue.aodev.memory.activities.game;

import android.support.annotation.NonNull;

import blue.aodev.memory.BasePresenter;
import blue.aodev.memory.BaseView;

public interface GameContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showLoading();

        void showLoadingError();

        void showGame();

        void setBoardSize(int rowCount, int columnCount);

        void hideCard(int row, int column);

        void showCard(int row, int column, @NonNull String text);

        void setCardType(int row, int column, @NonNull Card.Type type);

        void setFlipCount(int flipCount);

        void setTime(int time);

        void setScore(int score);

        void showEndGame();

        void showScores();
    }

    interface Presenter extends BasePresenter {
        void stop();

        void selectCard(int row, int column);

        void newGame();

        void retryLoading();

        void displayHighScores();

        // loadState and saveState would be here if we had state to save
    }
}
