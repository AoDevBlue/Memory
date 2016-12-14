package blue.aodev.memory.activities.game;

import android.support.annotation.NonNull;
import android.util.Log;

import blue.aodev.memory.data.api.ApiEndpoints;
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

    public GamePresenter(@NonNull GameContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showLoading();
        loadData();
    }

    @Override
    public void retryLoading() {
        view.showLoading();
        loadData();
    }

    private void loadData() {
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
        //TODO prepare the data

        view.showCards(null);
    }

    @Override
    public void selectCard(Card card) {

    }
}
