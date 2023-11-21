package com.taimoor.wallpixels.Listeners;

import com.taimoor.wallpixels.Models.VideoApiResponse;

public interface VideoResponseListener {

    void onFetch(VideoApiResponse response, String message);
    void onError(String message);

}
