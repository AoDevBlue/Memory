package blue.aodev.memory.activities.game;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import blue.aodev.memory.data.api.ApiEndpoints;
import blue.aodev.memory.data.api.GoalItem;
import blue.aodev.memory.data.api.GoalResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GamePresenter implements GameContract.Presenter {

    private static final int GOAL_ID = 470272;

    private GameContract.View view;

    /** The data of the GOAL_ID goal from the iKnow API **/
    private GoalResponse data;

    /** The state of the board **/
    private Card[][] board;

    public GamePresenter(@NonNull GameContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        if (board == null) {
            loadData();
        }
    }

    @Override
    public void retryLoading() {
        loadData();
    }

    private void loadData() {
        view.showLoading();

        //FIXME this logic should be in a separate class
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiService = retrofit.create(ApiEndpoints.class);
        Call<GoalResponse> call = apiService.getGoal(GOAL_ID);
        call.enqueue(new Callback<GoalResponse>() {
            @Override
            public void onResponse(Call<GoalResponse> call, Response<GoalResponse> response) {
                data = response.body();
                newGame();
            }

            @Override
            public void onFailure(Call<GoalResponse> call, Throwable t) {
                view.showLoadingError();
            }
        });
    }

    @Override
    public void newGame() {
        // 6 by 2 for now
        int rowCount = 6;
        int columnCount = 2;

        int targetSize = columnCount*rowCount;
        int size = Math.min(data.getGoalItems().length, targetSize/2);
        view.setBoardSize(rowCount, columnCount);

        board = new Card[6][4];

        for (int i = 0; i < size; i++) {
            GoalItem.Item item = data.getGoalItems()[i].getItem();
            Card cueCard = new Card(item.getId(), item.getCue().getText(), Card.Type.CUE);
            Card responseCard =
                    new Card(item.getId(), item.getResponse().getText(), Card.Type.RESPONSE);

            int cueIndex = 2*i;
            int responseIndex = 2*i+1;
            board[cueIndex/columnCount][cueIndex%columnCount] = cueCard;
            board[responseIndex/columnCount][responseIndex%columnCount] = responseCard;
        }

        view.showGame();
        displayBoard();
    }

    private void displayBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Card card = board[i][j];
                if (card == null) {
                    continue;
                }
                if (card.isFlipped()) {
                    view.showCard(i, j, card.getType(), card.getText());
                } else {
                    view.hideCard(i, j, card.getType());
                }
            }
        }
    }

    @Override
    public void selectCard(int row, int column) {

    }
}
