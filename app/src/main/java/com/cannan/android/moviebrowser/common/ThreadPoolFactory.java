package com.cannan.android.moviebrowser.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: ThreadPoolFactory
 * @Description:
 * @author: Cannan
 * @date: 2019-06-27 22:49
 */
public class ThreadPoolFactory {

    /**
     * 下载线程池
     */
    private static ExecutorService mDownLoadThreadPool;

    public static ExecutorService getDownLoadThreadPool() {
        if (mDownLoadThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mDownLoadThreadPool == null) {
                    mDownLoadThreadPool = Executors.newFixedThreadPool(5);
                }
            }
        }
        return mDownLoadThreadPool;
    }
}
