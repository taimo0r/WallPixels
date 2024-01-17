package com.taimoor.wallpixels.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.taimoor.wallpixels.Models.Hit;

import java.util.List;

@Dao
public interface HitDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToFavourites(Hit hit);

    @Query("SELECT * FROM favourites")
    LiveData<List<Hit>> getAllFavorites();

    @Query("SELECT COUNT(id) FROM favourites WHERE id = :wallpaperId")
    LiveData<Integer> isFavorite(long wallpaperId);

    @Delete
    void delete(Hit favourites);

    @Query(" DELETE FROM favourites")
    void deleteAll();

    @Query("DELETE FROM favourites WHERE id = :wallpaperId")
    void deleteById(long wallpaperId);

}
