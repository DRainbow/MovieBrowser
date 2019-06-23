package com.cannan.android.moviebrowser;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MovieDao mDao;

        PopulateDbAsync(MovieRoomDatabase db) {
            mDao = db.movieDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://private-04a55-videoplayer1.apiary-mock.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieService service = retrofit.create(MovieService.class);
            Call<List<Movie>> call = service.listMoives();
            try {
                List<Movie> listMovies = call.execute().body();
                System.out.println("获取网络数据成功" + new Gson().toJson(listMovies));
                mDao.insert(listMovies);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
