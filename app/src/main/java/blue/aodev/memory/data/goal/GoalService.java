package blue.aodev.memory.data.goal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GoalService {

    String BASE_URL = "https://iknow.jp/api/v2/";

    @GET("goals/{id}")
    Call<Goal> getGoal(@Path("id") int id);
}
