package blue.aodev.memory.activities.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import blue.aodev.memory.BasePresenter;
import blue.aodev.memory.BaseView;
import blue.aodev.memory.memory.Card;

public interface GameContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showLoading();

        void showLoadingError();

        void showGame();

        void setBoardSize(int rowCount, int columnCount);

        void showCard(int row, int column, @NonNull CardInfo cardInfo);

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

        void saveState(@Nullable Bundle outState);

        void loadState(@Nullable Bundle savedState);
    }

    /**
     * Value class used to pass data between the {@link Presenter} and the {@link View}.
     */
    class CardInfo {
        public final Card.Type type;
        public final String text;
        public final boolean flipped;

        public CardInfo(@NonNull Card.Type type, @NonNull String text, boolean flipped) {
            this.type = type;
            this.text = text;
            this.flipped = flipped;
        }
    }
}
