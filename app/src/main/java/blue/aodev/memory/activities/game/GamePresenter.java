package blue.aodev.memory.activities.game;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import blue.aodev.memory.R;
import blue.aodev.memory.data.goal.GoalService;
import blue.aodev.memory.data.goal.GoalItem;
import blue.aodev.memory.data.goal.Goal;
import blue.aodev.memory.data.score.ScoreDataSource;
import blue.aodev.memory.memory.Card;
import blue.aodev.memory.memory.Game;
import blue.aodev.memory.util.Timer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Presenter of the game activity.
 */
public class GamePresenter implements GameContract.Presenter, Game.FlipListener {

    /**
     * Bundle extras used to save and load state.
     */
    private static final String GOAL_DATA_EXTRA = "goal_data";
    private static final String GAME_EXTRA = "game";
    private static final String TIMER_EXTRA = "timer";

    /** The goal to request from iKnow's API, currently static. **/
    private static final int GOAL_ID = 470272;

    private GameContract.View view;

    private ScoreDataSource scoreDataSource;

    private GoalService goalService;

    /** The data of the GOAL_ID goal from the iKnow API **/
    private Goal data;

    /** The memory game **/
    private Game game;

    /** Timer used to compute the score **/
    private final Timer timer;

    /**
     * Map of runnables for flipping cards back.
     * The map is used to cancel those runnables.
     */
    private HashMap<Point, Runnable> flipBackRunnables;

    /** Handler used for flipping cards back. **/
    private Handler flipBackHandler;

    private final int ROW_COUNT;
    private final int COLUMN_COUNT;

    private boolean firstStart = true;

    public GamePresenter(@NonNull Context context,
                         @NonNull GameContract.View view,
                         @NonNull GoalService goalService,
                         @NonNull ScoreDataSource scoreDataSource) {
        this.view = view;
        view.setPresenter(this);
        this.goalService = goalService;
        this.scoreDataSource = scoreDataSource;

        COLUMN_COUNT = context.getResources().getInteger(R.integer.game_column_count);
        ROW_COUNT = context.getResources().getInteger(R.integer.game_row_count);

        timer = getTimer();
        flipBackHandler = new Handler();
        flipBackRunnables = new HashMap<>();
    }

    private Timer getTimer() {
        return new Timer(200, new Timer.Listener() {
            @Override
            public void onUpdate(long elapsedTimeMs) {
                int elaspedTime = (int) (elapsedTimeMs/1000);
                view.setTime(elaspedTime);
                int flipCount = game != null ? game.getFlipCount() : 0;
                view.setScore(computeScore(elaspedTime, flipCount));
            }
        });
    }

    @Override
    public void start() {
        if (game == null) {
            if (data == null) {
                loadData();
            } else {
                newGame();
            }
        } else {
            timer.start();
            if (firstStart) {
                displayInitialState();
            }
        }
        firstStart = false;
    }

    @Override
    public void stop() {
        timer.stop();
    }

    @Override
    public void retryLoading() {
        loadData();
    }

    /**
     * Load the data needed to create a new game and creates one
     */
    private void loadData() {
        view.showLoading();

        Call<Goal> call = goalService.getGoal(GOAL_ID);
        call.enqueue(new Callback<Goal>() {
            @Override
            public void onResponse(Call<Goal> call, Response<Goal> response) {
                data = response.body();
                if (view.isActive()) {
                    newGame();
                }
            }

            @Override
            public void onFailure(Call<Goal> call, Throwable t) {
                view.showLoadingError();
            }
        });
    }

    /**
     * Create a new game by selecting cards and displaying them.
     */
    @Override
    public void newGame() {
        createGame();
        displayInitialState();
        timer.start();
    }

    private void displayInitialState() {
        view.showGame();
        view.setBoardSize(ROW_COUNT, COLUMN_COUNT);
        displayBoard();
        view.setFlipCount(game.getFlipCount());
        view.setTime(0);
    }

    /**
     * Prepare the board by selecting cards.
     */
    private void createGame() {
        // Select as many items from the data as possible
        int targetSize = ROW_COUNT*COLUMN_COUNT;
        int itemCount = Math.min(data.getGoalItems().length, targetSize/2);

        // Create the corresponding cards
        List<Card> cards = new ArrayList<>(itemCount*2);
        for (int i = 0; i < itemCount; i++) {
            GoalItem.Item item = data.getGoalItems()[i].getItem();
            cards.add(new Card(item.getId(), item.getCue().getText(), Card.Type.CUE));
            cards.add(new Card(item.getId(), item.getResponse().getText(), Card.Type.RESPONSE));
        }

        // Shuffle the cards
        Collections.shuffle(cards);

        // Create the game
        game = new Game(ROW_COUNT, COLUMN_COUNT, cards);
        game.setFlipListener(this);
    }

    /**
     * Display all the cards of the board.
     */
    private void displayBoard() {
        for (int i = 0; i < game.getRowCount(); i++) {
            for (int j = 0; j < game.getColumnCount(); j++) {
                displayCard(i, j);
            }
        }
    }

    /**
     * Display a card.
     */
    private void displayCard(int row, int column) {
        Card card = game.getCard(row, column);
        if (card == null) {
            return;
        }
        GameContract.CardInfo cardInfo =
                new GameContract.CardInfo(card.getType(), card.getText(), card.isFlipped());
        view.showCard(row, column, cardInfo);
    }

    @Override
    public void displayHighScores() {
        view.showScores();
    }

    @Override
    public void selectCard(int row, int column) {
        game.flip(row, column);
        view.setFlipCount(game.getFlipCount());

        if (game.isOver()) {
            endGame();
        }
    }

    @Override
    public void onFlip(final int row, final int column, @NonNull Card card) {
        if (card.isFlipped()) {
            displayCard(row, column);

            // We have to cancel the flip back if it had not occurred yet
            Runnable runnable = flipBackRunnables.remove(new Point(row, column));
            if (runnable != null) {
                flipBackHandler.removeCallbacks(runnable);
            }
        } else {
            // We want to flip back the card with a delay so that
            // users can still see it.
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (view.isActive()) {
                        displayCard(row, column);
                    }
                }
            };
            flipBackRunnables.put(new Point(row, column), runnable);
            flipBackHandler.postDelayed(runnable, 1000);
        }
    }

    private void endGame() {
        timer.stop();
        int totalTime = (int) (timer.getTotalTime()/1000);
        int finalScore = computeScore(totalTime, game.getFlipCount());

        scoreDataSource.addScore(finalScore);

        view.setScore(finalScore);
        view.showEndGame();
    }

    /**
     * Compute the score.
     * @param time the time spent, in seconds.
     * @param flips the number of flips.
     * @return the score.
     */
    private int computeScore(int time, int flips) {
        int timeDecrement = 1;
        int flipDecrement = 10;
        int bestFlipCount = game.getCardsCount(); // The lowest possible number of flips

        int score = bestFlipCount*2*flipDecrement;
        score -= time*timeDecrement;
        score -= (flips - bestFlipCount)*flipDecrement;
        score *= 100; // People like big numbers

        // Don't go below 100 and use another formula if the user
        // takes too many flips or time
        int minScore = Math.max(500 - time, 100);

        return Math.max(score, minScore);
    }

    @Override
    public void saveState(@Nullable Bundle outState) {
        if (outState != null) {
            if (data != null) {
                Gson gson = new Gson();
                outState.putString(GOAL_DATA_EXTRA, gson.toJson(data));
            }
            if (game != null) {
                outState.putParcelable(GAME_EXTRA, game);
            }
            outState.putLong(TIMER_EXTRA, timer.getTotalTime());
        }
    }

    @Override
    public void loadState(@Nullable Bundle savedState) {
        if (savedState != null) {
            if (savedState.containsKey(GOAL_DATA_EXTRA)) {
                Gson gson = new Gson();
                data = gson.fromJson(savedState.getString(GOAL_DATA_EXTRA), Goal.class);
            }
            if (savedState.containsKey(GAME_EXTRA)) {
                game = savedState.getParcelable(GAME_EXTRA);
                game.setFlipListener(this);
            }
            long timerOffset = savedState.getLong(TIMER_EXTRA);
            timer.offset(timerOffset);
        }
    }
}
