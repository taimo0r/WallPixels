package com.taimoor.wallpixels.ApiService;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.taimoor.wallpixels.Listeners.ApiResponseListener;
import com.taimoor.wallpixels.Models.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {

    Context context;
    private static final String BASE_URL = "https://pixabay.com/";
    public static String ApiKey;
    private static Retrofit retrofit = null;

    public RequestManager(Context context) {
        this.context = context;
    }

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void initApiKey() {
        getKeyFromFirestore(key -> ApiKey = key);
    }

    public static void getKeyFromFirestore(FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Api Data")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String key = document.getString("key");
                            if (key != null) {
                                callback.onCallback(key);
                            }
                        }
                    } else {
                        Log.d("Key", "ERROR GETTING KEY");
                    }
                });
    }



    public void getWallpapers(ApiResponseListener listener, String page) {

        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Log.d("Key", "" + ApiKey);
        Call<ApiResponse> call = apiService.getImages(ApiKey, page, "200","vertical",true);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "An Error Occurred  \n Swipe to Refresh " , Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


    public void getSearchedWallpapers(ApiResponseListener listener, String page, String query) {

        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.searchImages(ApiKey, query, page, "200", "vertical",true);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public void getVideoWallpapers(ApiResponseListener listener, String page) {
        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.getVideos(ApiKey, page, "200", true);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public void getSearchedVideoWallpapers(ApiResponseListener listener, String page, String query) {

        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.getSearchedVideos(ApiKey, query, page, "200", true);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public interface FirestoreCallback {
        void onCallback(String key);
    }

    private interface PixabayApiService {
        @GET("api/")
        Call<ApiResponse> getImages(
                @Query("key") String apiKey,
                @Query("page") String page,
                @Query("per_page") String per_page,
                @Query("orientation") String orientation,
                @Query("safesearch") boolean value);


        @GET("api/")
        Call<ApiResponse> searchImages(
                @Query("key") String apiKey,
                @Query("q") String query,
                @Query("page") String page,
                @Query("per_page") String per_page,
                @Query("orientation") String orientation,
                @Query("safesearch") boolean value);

        @GET("api/videos/")
        Call<ApiResponse> getVideos(
                @Query("key") String apiKey,
                @Query("page") String page,
                @Query("per_page") String per_page,
                @Query("safesearch") boolean value);


        @GET("api/videos/")
        Call<ApiResponse> getSearchedVideos(
                @Query("key") String apiKey,
                @Query("q") String query,
                @Query("page") String page,
                @Query("per_page") String per_page,
                @Query("safesearch") boolean value);

    }

}
