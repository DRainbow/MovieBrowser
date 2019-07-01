package com.cannan.android.moviebrowser.di.module;

import android.content.Context;

import com.cannan.android.moviebrowser.data.MovieDao;
import com.cannan.android.moviebrowser.data.MovieRoomDatabase;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * @ClassName: DatabaseModule
 * @Description:
 * @author: Cannan
 * @date: 2019-07-01 22:37
 */
@Module
public class DatabaseModule {

    private MovieRoomDatabase db;

    @Inject
    public DatabaseModule(Context context) {
        db = MovieRoomDatabase.getDatabase(context);
    }

    @Provides
    MovieDao provideMovieDao() {
        return db.movieDao();
    }
}
