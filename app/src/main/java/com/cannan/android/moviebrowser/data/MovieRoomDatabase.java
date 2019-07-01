package com.cannan.android.moviebrowser.data;

import android.content.Context;
import android.os.AsyncTask;

import com.cannan.android.moviebrowser.di.component.DaggerNetComponent;
import com.cannan.android.moviebrowser.di.module.NetModule;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import retrofit2.Call;

/**
 * @ClassName: MovieRoomDatabase
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 16:08
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class MovieRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "db_movie")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MovieDao mDao;

        @Inject
        Call<List<Movie>> mCall;

        PopulateDbAsync(MovieRoomDatabase db) {
            mDao = db.movieDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                DaggerNetComponent.builder().netModule(new NetModule("")).build().inject(this);
                List<Movie> listMovies = mCall.execute().body();
                System.out.println("Retrofit execute has success!");
                mDao.insert(listMovies);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
