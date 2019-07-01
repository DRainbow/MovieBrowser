package com.cannan.android.moviebrowser.viewmodels;

import android.app.Application;

import com.cannan.android.moviebrowser.data.Movie;
import com.cannan.android.moviebrowser.data.MovieRepository;
import com.cannan.android.moviebrowser.di.component.DaggerRepositoryComponent;
import com.cannan.android.moviebrowser.di.module.DatabaseModule;

import java.util.List;

import javax.inject.Inject;

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

    @Inject
    public MovieRepository mRepository;

    private LiveData<List<Movie>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        DaggerRepositoryComponent.builder()
                .databaseModule(new DatabaseModule(application))
                .build()
                .inject(this);
        mAllMovies = mRepository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }
}
