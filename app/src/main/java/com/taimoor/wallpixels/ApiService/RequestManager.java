package com.taimoor.wallpixels.ApiService;

import android.content.Context;
import android.widget.Toast;

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
    public static final String ApiKey = "41320016-b0b02187972d32307aadaab86";
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

    public void getWallpapers(ApiResponseListener listener, String page) {

        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.getImages(ApiKey, page, "30");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


    public void getSearchedWallpapers(ApiResponseListener listener, String page, String query) {

        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.searchImages(ApiKey, query, page, "30");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public void getVideoWallpapers(ApiResponseListener listener, String page) {
        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.getVideos(ApiKey, page, "20");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public void getSearchedVideoWallpapers(ApiResponseListener listener, String page, String query) {

        Retrofit retrofit = RequestManager.getClient();
        PixabayApiService apiService = retrofit.create(PixabayApiService.class);
        Call<ApiResponse> call = apiService.getSearchedVideos(ApiKey, query, page, "20");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


    private interface PixabayApiService {
        @GET("api/")
        Call<ApiResponse> getImages(
                @Query("key") String apiKey,
                @Query("page") String page,
                @Query("per_page") String per_page);

        @GET("api/")
        Call<ApiResponse> searchImages(
                @Query("key") String apiKey,
                @Query("q") String query,
                @Query("page") String page,
                @Query("per_page") String per_page);

        @GET("api/videos/")
        Call<ApiResponse> getVideos(
                @Query("key") String apiKey,
                @Query("page") String page,
                @Query("per_page") String per_page);


        @GET("api/videos/")
        Call<ApiResponse> getSearchedVideos(
                @Query("key") String apiKey,
                @Query("q") String query,
                @Query("page") String page,
                @Query("per_page") String per_page);

    }

}
