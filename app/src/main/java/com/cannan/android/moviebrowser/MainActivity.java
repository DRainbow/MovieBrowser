package com.cannan.android.moviebrowser;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.cannan.android.moviebrowser.adapters.ImageAdapter;
import com.cannan.android.moviebrowser.adapters.VideoAdapter;
import com.cannan.android.moviebrowser.common.DisplayUtil;
import com.cannan.android.moviebrowser.data.Movie;
import com.cannan.android.moviebrowser.recycler.CustomRecyclerView;
import com.cannan.android.moviebrowser.viewmodels.MovieViewModel;
import com.cannan.android.moviebrowser.viewmodels.TaskViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private List<MovieView> mCacheView = new ArrayList<>();

    private CustomRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Movie> mMovieList = new ArrayList<>();

    private MovieViewModel mMovieViewModel;
    private TaskViewModel mTaskViewModel;

    /**
     * 是否 RecyclerView 发起的滑动
     */
    private boolean isRecyclerScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 199);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assignViews();
        initViews();

        observe();
    }

    private void assignViews() {
        mViewPager = findViewById(R.id.vp_main_top);
        mRecyclerView = findViewById(R.id.rv_main_bottom);
    }

    private void initViews() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ImageAdapter(MainActivity.this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);

        final LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.initPageParams(0, DisplayUtil.px2dp(this, DisplayUtil.getScreenWidth(this) / 4)).setUp();

        mRecyclerView.setOnTouchListener(this);
        mRecyclerView.initListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (isRecyclerScroll) {
                        int position = mRecyclerView.getScrolledPosition();
                        mTaskViewModel.imageScrollToPosition(position);

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        }, mLinearSnapHelper);

        mPagerAdapter = new VideoAdapter(mCacheView);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void observe() {
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(final List<Movie> movies) {
                System.out.println("load movies:\n" + new Gson().toJson(movies));

                if (movies == null || movies.size() == 0) {
                    return;
                }

                initialData(movies);

                mPagerAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mTaskViewModel.getVideoSlideTask().observe(this, new Observer<Event<Integer>>() {
            @Override
            public void onChanged(Event<Integer> integerEvent) {
                int position = integerEvent.getContentIfNotHandled();
                System.out.println("---- [RecyclerView] Video observe position [" + position + "]----");
                mRecyclerView.smoothScrollToPosition(position);
            }
        });
        mTaskViewModel.getImageSlideTask().observe(this, new Observer<Event<Integer>>() {
            @Override
            public void onChanged(Event<Integer> integerEvent) {
                int position = integerEvent.getContentIfNotHandled();
                System.out.println("---- [ViewPager] IMG observe position [" + position + "]----");
                mViewPager.setCurrentItem(position, true);
            }
        });
    }

    private void initialData(List<Movie> movies) {
        mMovieList.clear();
        mMovieList.addAll(movies);

        for (Movie movie : movies) {
            MovieView view = new MovieView(MainActivity.this, movie.getVideoUrl());
            mCacheView.add(view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("---- [ViewPager] onPageSelected, selected position [" + position + "] ----");
        for (int index = 0; index < mCacheView.size(); index++) {
            MovieView view = mCacheView.get(index);
            if (view == null) {
                return;
            }
            if (index == position) {
                view.start();
            } else {
                view.pause();
            }
        }

        if (!isRecyclerScroll) {
            mTaskViewModel.videoScrollToPosition(position);
        }

        isRecyclerScroll = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isRecyclerScroll = true;
        return false;
    }
}
