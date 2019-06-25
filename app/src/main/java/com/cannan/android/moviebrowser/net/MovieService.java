package com.cannan.android.moviebrowser.net;


import com.cannan.android.moviebrowser.data.Movie;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @ClassName: MovieService
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 16:22
 */
public interface MovieService {

    @GET("pictures")
    Call<List<Movie>> listMoives();

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String fileUrl);
}
