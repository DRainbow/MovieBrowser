package com.cannan.android.moviebrowser;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: MovieViewModel
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 15:13
 */
public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;

    private LiveData<List<Movie>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    void insert(Movie movie) {
        mRepository.insert(movie);
    }
}
