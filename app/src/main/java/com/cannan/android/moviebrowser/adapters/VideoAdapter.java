package com.cannan.android.moviebrowser.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cannan.android.moviebrowser.MovieView;
import com.cannan.android.moviebrowser.data.Movie;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @ClassName: VideoAdapter
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 18:39
 */
public class VideoAdapter extends PagerAdapter {

    private Context mContext;

    /**
     * 数据源
     */
    private List<Movie> mData;

    /**
     * 缓存的View
     */
    private Queue<MovieView> mCacheView = new LinkedList<>();

    public VideoAdapter(Context context, List<Movie> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String url = mData.get(position).getVideoUrl();

        MovieView view;
        if(mCacheView.size() > 0){
            view = mCacheView.poll();
        }else{
            view = new MovieView(mContext, url);
        }
        view.setUri(url);
        view.initMedia();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        MovieView movieView = (MovieView) object;
        movieView.stop();
        container.removeView(movieView);
        mCacheView.offer(movieView);
    }
}
