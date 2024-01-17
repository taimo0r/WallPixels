package com.taimoor.wallpixels.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.taimoor.wallpixels.Models.Videos;

public class VideosConverter {

    @TypeConverter
    public static Videos fromString(String value) {
        return new Gson().fromJson(value, Videos.class);
    }

    @TypeConverter
    public static String fromVideos(Videos videos) {
        return new Gson().toJson(videos);
    }
}