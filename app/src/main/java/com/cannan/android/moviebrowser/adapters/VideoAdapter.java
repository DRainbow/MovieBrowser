package com.cannan.android.moviebrowser.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.cannan.android.moviebrowser.MovieView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @ClassName: VideoAdapter
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 18:39
 */
public class VideoAdapter extends PagerAdapter {

    private List<MovieView> mCacheView = new ArrayList<>();

    public VideoAdapter(List<MovieView> cacheView) {
        mCacheView = cacheView;
    }

    @Override
    public int getCount() {
        return mCacheView.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        MovieView view = mCacheView.get(position);
        view.initMedia();
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        MovieView temp = mCacheView.get(position);
        container.removeView(temp);
        temp.release();
    }
}
