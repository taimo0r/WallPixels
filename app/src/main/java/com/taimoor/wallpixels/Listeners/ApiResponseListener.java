package com.taimoor.wallpixels.Listeners;

import com.taimoor.wallpixels.Models.ApiResponse;

public interface ApiResponseListener {

    void onFetch(ApiResponse response, String message);

    void onError(String message);
}
