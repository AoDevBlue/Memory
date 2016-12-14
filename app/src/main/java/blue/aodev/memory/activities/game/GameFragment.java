package blue.aodev.memory.activities.game;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import blue.aodev.memory.R;
import blue.aodev.memory.activities.score.ScoresActivity;

public class GameFragment extends Fragment implements GameContract.View {

    private GameContract.Presenter presenter;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showCards(List<Card> cards) {

    }

    @Override
    public void flipCard(@NonNull Card card) {

    }

    @Override
    public void setFlipCount(int flipCount) {

    }

    @Override
    public void setTime(int time) {

    }

    @Override
    public void showScore(int score) {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void showScores() {
        startActivity(ScoresActivity.createIntent(getContext()));
    }
}
