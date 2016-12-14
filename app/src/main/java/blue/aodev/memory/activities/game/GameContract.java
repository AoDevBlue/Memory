package blue.aodev.memory.activities.game;

import android.support.annotation.NonNull;

import java.util.List;

import blue.aodev.memory.BasePresenter;
import blue.aodev.memory.BaseView;

public interface GameContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void showLoadingError();

        void showCards(List<Card> cards);

        void flipCard(@NonNull Card card);

        void setFlipCount(int flipCount);

        void setTime(int time);

        void showScore(int score);

        void showRetry();

        void showScores();
    }

    interface Presenter extends BasePresenter {
        void selectCard(Card card);

        void retry();

        // loadState and saveState would be here if we had state to save
    }
}
