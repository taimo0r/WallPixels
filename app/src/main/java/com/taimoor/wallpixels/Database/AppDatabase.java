package com.taimoor.wallpixels.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.taimoor.wallpixels.Models.Hit;

@Database(entities = {Hit.class}, version = 1)
@TypeConverters({VideosConverter.class})
public abstract class AppDatabase extends RoomDatabase {


    public abstract HitDao hitDao();


    private static AppDatabase Instance;

    public static AppDatabase getInstance(Context context){
        if (Instance == null){
            Instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"Favourites")
                    .allowMainThreadQueries()
                    .build();
        }
        return Instance;
    }


}
