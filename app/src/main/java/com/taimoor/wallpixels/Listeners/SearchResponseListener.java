package com.taimoor.wallpixels.Listeners;

import com.taimoor.wallpixels.Models.SearchApiResponse;

public interface SearchResponseListener {

    void onFetch(SearchApiResponse response, String message);
    void onError( String message);

}
