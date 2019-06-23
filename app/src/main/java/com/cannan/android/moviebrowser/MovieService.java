package com.cannan.android.moviebrowser;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @ClassName: MovieService
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 16:22
 */
public interface MovieService {

    @GET("pictures")
    Call<List<Movie>> listMoives();
}
