package com.cannan.android.moviebrowser.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * @ClassName: MovieRepository
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 16:04
 */
public class MovieRepository {

    private MovieDao mMovieDao;

    private LiveData<List<Movie>> mAllMovies;

    public MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    public void insert(Movie movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
