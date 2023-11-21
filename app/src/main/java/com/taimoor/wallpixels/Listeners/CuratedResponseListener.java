package com.taimoor.wallpixels.Listeners;

import com.taimoor.wallpixels.Models.CuratedApiResponse;

public interface CuratedResponseListener {
    void onFetch(CuratedApiResponse response, String message);
    void onError(String message);


}
