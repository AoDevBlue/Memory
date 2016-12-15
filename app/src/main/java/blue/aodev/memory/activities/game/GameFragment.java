package blue.aodev.memory.activities.game;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.activities.score.ScoresActivity;

public class GameFragment extends Fragment implements GameContract.View {

    private GameContract.Presenter presenter;

    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;

    @BindView(R.id.retry_loading_button) Button retryLoadingButton;
    @BindView(R.id.error_layout) View errorLayout;

    @BindView(R.id.game_board_layout) GridLayout gameBoardLayout;
    @BindView(R.id.game_status_layout) View gameStatusLayout;
    @BindView(R.id.time_count) TextView timeCountView;
    @BindView(R.id.flip_count) TextView flipCountView;

    private BottomSheetBehavior bottomSheetBehavior;


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

        bottomSheetBehavior = BottomSheetBehavior.from(gameStatusLayout);
        int peekHeight = (int) getResources().getDimension(R.dimen.collapsed_game_control_height);
        bottomSheetBehavior.setPeekHeight(peekHeight);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stop();
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoading() {
        hideAll();
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingError() {
        hideAll();
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGame() {
        hideAll();
        gameBoardLayout.setVisibility(View.VISIBLE);
        gameStatusLayout.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
        gameBoardLayout.setVisibility(View.INVISIBLE);
        gameStatusLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setBoardSize(int rowCount, final int columnCount) {
        gameBoardLayout.removeAllViews();
        gameBoardLayout.setColumnCount(columnCount);
        gameBoardLayout.setRowCount(rowCount);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < rowCount*columnCount; i++) {
            inflater.inflate(R.layout.game_card, gameBoardLayout, true);

            final int row = i/columnCount;
            final int column = i%columnCount;
            gameBoardLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.selectCard(row, column);
                }
            });
        }
    }

    @Override
    public void hideCard(int row, int column) {
        CardView cardView = getCardView(row, column);
        cardView.setText("");
    }

    @Override
    public void showCard(int row, int column, @NonNull String text) {
        CardView cardView = getCardView(row, column);
        cardView.setText(text);
    }

    @Override
    public void setCardType(int row, int column, @NonNull Card.Type type) {
        CardView cardView = getCardView(row, column);
        cardView.setType(type);
    }

    private CardView getCardView(int row, int column) {
        int childIndex = row * gameBoardLayout.getColumnCount() + column;
        return (CardView) gameBoardLayout.getChildAt(childIndex);
    }

    @Override
    public void setFlipCount(int flipCount) {
        flipCountView.setText(getResources().getString(R.string.activity_game_flips, flipCount));
    }

    @Override
    public void setTime(int time) {
        timeCountView.setText(getResources().getString(R.string.activity_game_time, time));
    }

    @Override
    public void showEndGame(int score) {

    }

    @Override
    public void showScores() {
        startActivity(ScoresActivity.createIntent(getContext()));
        getActivity().finish();
    }
}
