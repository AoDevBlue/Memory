package blue.aodev.memory.activities.game;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.activities.score.ScoresActivity;

public class GameFragment extends Fragment implements GameContract.View {

    private GameContract.Presenter presenter;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.retry_loading_button) Button retryLoadingButton;
    @BindView(R.id.error_layout) View errorLayout;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);

        retryLoadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.retryLoading();
            }
        });

        return view;
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
        loadingIndicator.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingError() {
        loadingIndicator.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void showCards(List<Card> cards) {
        loadingIndicator.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
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
