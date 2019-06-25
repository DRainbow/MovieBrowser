package com.cannan.android.moviebrowser.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ClassName: NetHelper
 * @Description:
 * @author: Cannan
 * @date: 2019-06-25 19:02
 */
public class NetHelper {

    private static volatile NetHelper instance;

    private Retrofit mRetrofit;

    private NetHelper() {
    }

    public static NetHelper getInstance() {
        if (instance == null) {
            synchronized (NetHelper.class) {
                if (instance == null) {
                    instance = new NetHelper();
                }
            }
        }
        return instance;
    }

    public NetHelper buildRetrofit(String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return this;
    }

    public <T> T create(Class<T> service) {
        if (mRetrofit == null) {
            return null;
        }
        return mRetrofit.create(service);
    }
}
