package com.cannan.android.moviebrowser.net;

import android.os.Environment;
import android.text.TextUtils;

import com.cannan.android.moviebrowser.common.ThreadPoolFactory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
public class Download {

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/download";

    public static void download(String url) {
        String path = DOWNLOAD_PATH + getFileNameByUrl(url);
        System.out.println("---- Download file paht = " + path + " ----");

        if (TextUtils.isEmpty(path)) {
            return;
        }

        final File file = new File(path);
        if (file.exists()) {
            return;
        }

        MovieService service = NetHelper.getInstance()
                .buildRetrofit("http://private-04a55-videoplayer1.apiary-mock.com/")
                .create(MovieService.class);
        Call<ResponseBody> call = service.download(url);
        call.enqueue(new Callback<ResponseBody>() {
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

    public static String getLocalFileIfExist(String url) {
        String path = DOWNLOAD_PATH + getFileNameByUrl(url);
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        return "";
    }

    public static String getFileNameByUrl(String url) {
        int firstHalfLength = url.length() / 2;
        String localFilename = String.valueOf(url.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(url.substring(firstHalfLength).hashCode());
        System.out.println("Download get file name [" + localFilename + "] by url [" + url + "]");
        return localFilename;
    }
}
