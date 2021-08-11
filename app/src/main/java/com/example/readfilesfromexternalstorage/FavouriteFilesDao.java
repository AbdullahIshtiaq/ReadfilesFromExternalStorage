package com.example.readfilesfromexternalstorage;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavouriteFilesDao {

    @Insert(onConflict = REPLACE)
    void insert(FavouriteFiles favouriteFiles);

    @Update
    void update(FavouriteFiles favouriteFiles);

    @Delete
    void delete(FavouriteFiles favouriteFiles);

    @Query("DELETE FROM favourite_files_table")
    void deleteAll();

    @Query("SELECT * FROM favourite_files_table ORDER BY id DESC")
    List<FavouriteFiles> getAllFavouriteFiles();
}
