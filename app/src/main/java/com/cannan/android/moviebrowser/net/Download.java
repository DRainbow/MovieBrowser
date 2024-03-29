package com.cannan.android.moviebrowser.net;

import android.os.Environment;
import android.text.TextUtils;

import com.cannan.android.moviebrowser.common.ThreadPoolFactory;
import com.cannan.android.moviebrowser.di.component.DaggerNetComponent;
import com.cannan.android.moviebrowser.di.module.NetModule;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName: Download
 * @Description:
 * @author: Cannan
 * @date: 2019-06-25 22:57
 */
@Singleton
public class Download {

    private static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/download";

    @Inject
    Call<ResponseBody> mBodyCall;

    private static volatile Download INSTANCE;

    public static Download getInstance() {
        if (INSTANCE == null) {
            synchronized (Download.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Download();
                }
            }
        }
        return INSTANCE;
    }

    public void download(String url) {
        String path = DOWNLOAD_PATH + getFileNameByUrl(url);
        System.out.println("---- Download file paht = " + path + " ----");

        if (TextUtils.isEmpty(path)) {
            return;
        }

        final File file = new File(path);
        if (file.exists()) {
            return;
        }

        DaggerNetComponent.builder().netModule(new NetModule(url)).build().inject(this);

        mBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                System.out.println("Download success");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtils.copyInputStreamToFile(response.body().byteStream(), file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ThreadPoolFactory.getDownLoadThreadPool().execute(runnable);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Download failure");
            }
        });
    }

    public String getLocalFileIfExist(String url) {
        String path = DOWNLOAD_PATH + getFileNameByUrl(url);
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        return "";
    }

    private String getFileNameByUrl(String url) {
        int firstHalfLength = url.length() / 2;
        String localFilename = String.valueOf(url.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(url.substring(firstHalfLength).hashCode());
        System.out.println("Download get file name [" + localFilename + "] by url [" + url + "]");
        return localFilename;
    }
}
