package blue.aodev.memory.activities.game;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blue.aodev.memory.R;
import blue.aodev.memory.data.goal.GoalService;
import blue.aodev.memory.data.goal.GoalItem;
import blue.aodev.memory.data.goal.Goal;
import blue.aodev.memory.data.score.ScoreDataSource;
import blue.aodev.memory.util.Timer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamePresenter implements GameContract.Presenter {

    private static final int GOAL_ID = 470272;

    private GameContract.View view;

    private ScoreDataSource scoreDataSource;

    private GoalService goalService;

    /** The data of the GOAL_ID goal from the iKnow API **/
    private Goal data;

    /** The state of the board **/
    private Card[][] board;

    /** The number of items the game has **/
    private int itemCount;

    /** The number of items that were matched **/
    private int matchedItemCount;

    /** The index of the card flipped first during a two cards flip **/
    private Point firstFlippedPos;

    /** Timer used to compute the score **/
    private Timer timer;

    /** Number of flips made **/
    private int flipCount;

    private final int ROW_COUNT;
    private final int COLUMN_COUNT;

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
    }

    private Timer getTimer() {
        return new Timer(200, new Timer.Listener() {
            @Override
            public void onUpdate(long elapsedTimeMs) {
                int elaspedTime = (int) (elapsedTimeMs/1000);
                view.setTime(elaspedTime);
                view.setScore(computeScore(elaspedTime, flipCount));
            }
        });
    }

    @Override
    public void start() {
        if (board == null) {
            if (data == null) {
                loadData();
            } else {
                newGame();
            }
        }
        if (timer != null) {
            timer.resume();
        }
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.pause();
        }
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
        createBoard();
        flipCount = 0;
        matchedItemCount = 0;

        // Display the initial state
        view.showGame();
        displayBoard();
        view.setFlipCount(flipCount);
        view.setTime(0);

        // Start the timer
        timer.start();
    }

    /**
     * Prepare the board by selecting cards.
     */
    private void createBoard() {
        // Create the board structure
        board = new Card[ROW_COUNT][COLUMN_COUNT];
        view.setBoardSize(ROW_COUNT, COLUMN_COUNT);

        // Select as many items from the data as possible
        int targetSize = ROW_COUNT*COLUMN_COUNT;
        itemCount = Math.min(data.getGoalItems().length, targetSize/2);

        // Create the corresponding cards
        List<Card> cards = new ArrayList<>(itemCount*2);
        for (int i = 0; i < itemCount; i++) {
            GoalItem.Item item = data.getGoalItems()[i].getItem();
            cards.add(new Card(item.getId(), item.getCue().getText(), Card.Type.CUE));
            cards.add(new Card(item.getId(), item.getResponse().getText(), Card.Type.RESPONSE));
        }

        // Shuffle the cards
        Collections.shuffle(cards);

        // Fill the board
        for (int i = 0; i < itemCount*2; i++) {
            board[i/COLUMN_COUNT][i%COLUMN_COUNT] = cards.get(i);
        }
    }

    /**
     * Display all the cards of the board.
     */
    private void displayBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                displayCard(i, j);
            }
        }
    }

    /**
     * Display a card.
     */
    private void displayCard(int row, int column) {
        Card card = board[row][column];
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
        Card flipping = board[row][column];

        if (flipping.isFlipped()) {
            // We can't flip cards that are already flipped
            return;
        }

        // We flip the card to show it to the user
        revealCard(row, column);

        if (firstFlippedPos == null) {
            // It's the first card we flip, just store it
            firstFlippedPos = new Point(row, column);
        } else {
            Card firstFlipped = board[firstFlippedPos.x][firstFlippedPos.y];

            if (firstFlipped.getItemId() == flipping.getItemId()) {
                // We have a match! We keep the two cards flipped
                firstFlippedPos = null;

                // We check for the game's end
                matchedItemCount++;
                if (matchedItemCount == itemCount) {
                    endGame();
                }
            } else {
                // We flip back the two cards after some time
                flipBack(firstFlippedPos, new Point(row, column));
                firstFlippedPos = null;
            }
        }
    }

    /**
     * Flip a card that was not flipped and update the view.
     */
    private void revealCard(int row, int column) {
        Card card = board[row][column];

        // Flip the card
        card.flip();
        displayCard(row, column);

        // Update the flip count
        flipCount++;
        view.setFlipCount(flipCount);
    }

    /**
     * Flip back two cards that did not match.
     */
    private void flipBack(@NonNull final Point firstPos, @NonNull final Point secondPos) {
        // The cards are instantly available for a new flip
        board[firstPos.x][firstPos.y].flip();
        board[secondPos.x][secondPos.y].flip();

        // But are only visually flipped after some delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.isActive()) {
                    // No need to display them if they have been flipped since
                    if (!board[firstPos.x][firstPos.y].isFlipped()) {
                        displayCard(firstPos.x, firstPos.y);
                    }
                    if (!board[secondPos.x][secondPos.y].isFlipped()) {
                        displayCard(secondPos.x, secondPos.y);
                    }
                }
            }
        }, 1000);
    }

    private void endGame() {
        timer.stop();
        int totalTime = (int) (timer.getTotalTime()/1000);
        int finalScore = computeScore(totalTime, flipCount);

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
        int bestFlipCount = itemCount*2; // The lowest possible number of flips

        int score = bestFlipCount*2*flipDecrement;
        score -= time*timeDecrement;
        score -= Math.max(0, flips - bestFlipCount)*flipDecrement;
        score *= 100; // People like big numbers

        // Don't go below 100 and use another formula if the user
        // takes too many flips or time
        int minScore = Math.max(500 - time, 100);

        return Math.max(score, minScore);
    }
}
