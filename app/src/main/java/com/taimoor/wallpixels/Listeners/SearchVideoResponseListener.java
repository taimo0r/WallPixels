package com.taimoor.wallpixels.Listeners;

import com.taimoor.wallpixels.Models.SearchVideoResponse;
import com.taimoor.wallpixels.Models.VideoApiResponse;

public interface SearchVideoResponseListener {

    void onFetch(SearchVideoResponse response, String message);
    void onError(String message);

}
