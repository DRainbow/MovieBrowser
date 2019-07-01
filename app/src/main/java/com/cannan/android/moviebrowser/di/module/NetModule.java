package com.cannan.android.moviebrowser.di.module;

import com.cannan.android.moviebrowser.data.Movie;
import com.cannan.android.moviebrowser.net.MovieService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ClassName: NetModule
 * @Description:
 * @author: Cannan
 * @date: 2019-07-02 00:11
 */
@Module
public class NetModule {

    private String url;

    @Inject
    public NetModule(String url) {
        this.url = url;
    }

    @Singleton
    @Provides
    MovieService provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(MovieService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService.class);
    }

    @Provides
    Call<List<Movie>> provideListMovie(MovieService service) {
        return service.listMoives();
    }

    @Provides
    Call<ResponseBody> provideDownload(MovieService service) {
        return service.download(url);
    }
}
