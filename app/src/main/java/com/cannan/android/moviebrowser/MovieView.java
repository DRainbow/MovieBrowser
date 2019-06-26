package com.cannan.android.moviebrowser;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.cannan.android.moviebrowser.net.Download;

import java.io.IOException;

/**
 * @ClassName: MovieView
 * @Description:
 * @author: Cannan
 * @date: 2019-06-25 20:06
 */
public class MovieView extends FrameLayout implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private Context mContext;

    private MediaPlayer mMediaPlayer;

    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;

    private String mUri;

    private int surfaceHeight, surfaceWidth;

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mMediaPlayer != null) {
                mMediaPlayer.setDisplay(holder);
                measureSize();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    public MovieView(Context context, String url) {
        super(context);

        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.item_movie, this);
        mSurfaceView = findViewById(R.id.sv_item);

        holder = mSurfaceView.getHolder();
        holder.addCallback(mSurfaceCallback);

        mUri = url;
    }

    public void initMedia() {
        mMediaPlayer = new MediaPlayer();

        try {
            String path = Download.getLocalFileIfExist(mUri);
            if (TextUtils.isEmpty(path)) {
                System.out.println("---- MovieView Play [" + mUri + "] with net ----");
                mMediaPlayer.setDataSource(mUri);
                Download.download(mUri);
            } else {
                System.out.println("---- MovieView Play [" + path + "] with local ----");
                mMediaPlayer.setDataSource(mContext, Uri.parse(path));
            }
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            measureSize();
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            measureSize();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
            if (!mMediaPlayer.isPlaying()) {
                Log.e("player", "input url=" + mUri);
                mMediaPlayer.start();
            }
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void measureSize() {
        int mVideoWidth = mMediaPlayer.getVideoWidth();
        int mVideoHeight = mMediaPlayer.getVideoHeight();

        if ((mVideoWidth > 0) && (mVideoHeight > 0)) {
            boolean isPlay = false;
            if (mMediaPlayer.isPlaying()) {
                isPlay = true;
                mMediaPlayer.pause();
            }

            getSurfaceRect();

            System.out.println("---- Video width = " + mVideoWidth + ", height: " + mVideoHeight + " ----");
            System.out.println("---- Surface width = " + surfaceWidth + ", height: " + surfaceHeight + " ----");

            int finalwidth, finalheight = 0;
            if (mVideoWidth * surfaceHeight > surfaceWidth * mVideoHeight) {
                finalheight = surfaceWidth * mVideoHeight / mVideoWidth;
                finalwidth = surfaceWidth;
            } else if (mVideoWidth * surfaceHeight < surfaceWidth * mVideoHeight) {
                finalwidth = surfaceHeight * mVideoWidth / mVideoHeight;
                finalheight = surfaceHeight;
            } else {
                finalheight = surfaceHeight;
                finalwidth = surfaceWidth;
            }

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
            layoutParams.height = finalheight;
            layoutParams.width = finalwidth;
            mSurfaceView.setLayoutParams(layoutParams);
            holder.setFixedSize(mVideoWidth, mVideoHeight);

            mSurfaceView.getHolder().setSizeFromLayout();

            if (isPlay) {
                mMediaPlayer.start();
            }
        }
    }

    private void getSurfaceRect() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        surfaceHeight = screenHeight;
        surfaceWidth = screenWidth;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        System.out.println("---- MovieView onPrepared ----");

        mMediaPlayer.setDisplay(holder);

        measureSize();

        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("---- MovieView onCompletion ----");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("---- MovieView onError ----");

        mMediaPlayer.reset();
        try {
            String path = Download.getLocalFileIfExist(mUri);
            if (TextUtils.isEmpty(path)) {
                System.out.println("---- MovieView Play [" + mUri + "] with net ----");
                mMediaPlayer.setDataSource(mUri);
            } else {
                System.out.println("---- MovieView Play [" + path + "] with local ----");
                mMediaPlayer.setDataSource(mContext, Uri.parse(path));
            }
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
