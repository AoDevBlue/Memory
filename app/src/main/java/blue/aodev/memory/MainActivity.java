package blue.aodev.memory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import blue.aodev.memory.data.api.ApiEndpoints;
import blue.aodev.memory.data.api.GoalResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiService = retrofit.create(ApiEndpoints.class);
        Call<GoalResponse> call = apiService.getGoal(470272);
        call.enqueue(new Callback<GoalResponse>() {
            @Override
            public void onResponse(Call<GoalResponse> call, Response<GoalResponse> response) {
                Log.d(TAG, "Success");
            }

            @Override
            public void onFailure(Call<GoalResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });

    }
}
