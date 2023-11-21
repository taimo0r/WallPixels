package com.taimoor.wallpixels;

import android.content.Context;
import android.widget.Toast;

import com.taimoor.wallpixels.Listeners.CuratedResponseListener;
import com.taimoor.wallpixels.Listeners.SearchResponseListener;
import com.taimoor.wallpixels.Listeners.SearchVideoResponseListener;
import com.taimoor.wallpixels.Listeners.VideoResponseListener;
import com.taimoor.wallpixels.Models.CuratedApiResponse;
import com.taimoor.wallpixels.Models.SearchApiResponse;
import com.taimoor.wallpixels.Models.SearchVideoResponse;
import com.taimoor.wallpixels.Models.VideoApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager {

    Context context;

    Retrofit retrofitVideo = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/videos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getCuratedWallpapers(CuratedResponseListener listener, String page) {
        CallWallpaperList callWallpaperList = retrofit.create(CallWallpaperList.class);
        Call<CuratedApiResponse> call = callWallpaperList.getWallpapers(page, "20");

        call.enqueue(new Callback<CuratedApiResponse>() {
            @Override
            public void onResponse(Call<CuratedApiResponse> call, Response<CuratedApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<CuratedApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }


    public void searchCuratedWallpapers(SearchResponseListener listener, String page, String query) {
        CallWallpaperListSearch callWallpaperListSearch = retrofit.create(CallWallpaperListSearch.class);
        Call<SearchApiResponse> call = callWallpaperListSearch.searchWallpapers(query, page, "20");

        call.enqueue(new Callback<SearchApiResponse>() {
            @Override
            public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SearchApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void getPopularVideos(VideoResponseListener listener, String page) {

        CallVideoList callVideoList = retrofitVideo.create(CallVideoList.class);
        Call<VideoApiResponse> call = callVideoList.getVideos(page, "15");

        call.enqueue(new Callback<VideoApiResponse>() {
            @Override
            public void onResponse(Call<VideoApiResponse> call, Response<VideoApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<VideoApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


    public void getSearchPopularVideos(SearchVideoResponseListener listener, String page, String query){
        CallVideoListSearch videoListSearch = retrofitVideo.create(CallVideoListSearch.class);
        Call<SearchVideoResponse> call = videoListSearch.getSearchVideos(query,page,"15");

        call.enqueue(new Callback<SearchVideoResponse>() {
            @Override
            public void onResponse(Call<SearchVideoResponse> call, Response<SearchVideoResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());

            }

            @Override
            public void onFailure(Call<SearchVideoResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    private interface CallWallpaperList {
        @Headers({
                "Accept: application/json",
                "Authorization: 563492ad6f9170000100000182596d90bb6e49f58775d39d91c4c603"
        })
        @GET("curated/")
        Call<CuratedApiResponse> getWallpapers(
                @Query("page") String page,
                @Query("per_page") String per_page);
    }


    private interface CallWallpaperListSearch {
        @Headers({
                "Accept: application/json",
                "Authorization: 563492ad6f9170000100000182596d90bb6e49f58775d39d91c4c603"
        })
        @GET("search")
        Call<SearchApiResponse> searchWallpapers(
                @Query("query") String query,
                @Query("page") String page,
                @Query("per_page") String per_page);
    }


    private interface CallVideoList {
        @Headers({
                "Accept: application/json",
                "Authorization: 563492ad6f9170000100000182596d90bb6e49f58775d39d91c4c603"
        })
        @GET("popular")
        Call<VideoApiResponse> getVideos(
                @Query("page") String page,
                @Query("per_page") String per_page);
    }

    private interface CallVideoListSearch {
        @Headers({
                "Accept: application/json",
                "Authorization: 563492ad6f9170000100000182596d90bb6e49f58775d39d91c4c603"
        })
        @GET("search")
        Call<SearchVideoResponse> getSearchVideos(
                @Query("query") String query,
                @Query("page") String page,
                @Query("per_page") String per_page);
    }


}
