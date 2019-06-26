package com.cannan.android.moviebrowser.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * @ClassName: MovieDao
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 15:57
 */
@Dao
public interface MovieDao {

    /**
     *
     * @param movie
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie movie);

    /**
     *
     * @param movies
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Movie> movies);

    /**
     *
     */
    @Query("DELETE FROM t_movie")
    void deleteAll();

    /**
     *
     * @return
     */
    @Query("SELECT * from t_movie ORDER BY id ASC")
    LiveData<List<Movie>> getMovies();
}
