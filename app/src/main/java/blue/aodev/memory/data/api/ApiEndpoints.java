package blue.aodev.memory.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiEndpoints {

    String BASE_URL = "https://iknow.jp/api/v2/";

    @GET("goals/{id}")
    Call<GoalResponse> getGoal(@Path("id") int id);
}
