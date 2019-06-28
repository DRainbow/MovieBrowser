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
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private CustomRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SnapHelper mHelper;
    private int mSnapPosition = 0;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        private int mConsumeX = 0;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                if (isRecyclerScroll) {
                    mViewPager.setCurrentItem(mSnapPosition, true);
                }
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mConsumeX += dx;
            onScrolledChangedCallback();
        }

        /**
         * view 大小随位移事件变化
         */
        private void onScrolledChangedCallback() {
            int mOnePageWidth = mRecyclerView.getDecoration().mItemConsumeX;

            if (mOnePageWidth <= 0) {
                return;
            }

            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            View snapView = mHelper.findSnapView(manager);
            if (snapView == null) {
                return;
            }
            mSnapPosition = manager.getPosition(snapView);

            int offset = mConsumeX - mSnapPosition * mOnePageWidth;
            float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);

            View leftView = null;
            View currentView;
            View rightView = null;
            if (mSnapPosition > 0) {
                leftView = mRecyclerView.getLayoutManager().findViewByPosition(mSnapPosition - 1);
            }
            currentView = mRecyclerView.getLayoutManager().findViewByPosition(mSnapPosition);
            if (mSnapPosition < mRecyclerView.getAdapter().getItemCount() - 1) {
                rightView = mRecyclerView.getLayoutManager().findViewByPosition(mSnapPosition + 1);
            }

            // 缩放比
            float scale = 0.4f;
            if (leftView != null) {
                leftView.setScaleY((1 - scale) + percent * scale);
                leftView.setScaleY((1 - scale) + percent * scale);
            }
            if (currentView != null) {
                currentView.setScaleX(1 - percent * scale);
                currentView.setScaleY(1 - percent * scale);
            }
            if (rightView != null) {
                rightView.setScaleX((1 - scale) + percent * scale);
                rightView.setScaleY((1 - scale) + percent * scale);
            }
        }
    };

    /**
     * 数据源
     */
    private List<Movie> mMovieList = new ArrayList<>();

    private MovieViewModel mMovieViewModel;

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
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ImageAdapter(MainActivity.this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);

        mHelper = new LinearSnapHelper();
        mHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.initPageParams(0, DisplayUtil.px2dp(this, DisplayUtil.getScreenWidth(this) / 4)).setUp();

        mRecyclerView.setOnTouchListener(this);
        mRecyclerView.addOnScrollListener(mScrollListener);

        mPagerAdapter = new VideoAdapter(this, mMovieList);
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
    }

    private void initialData(List<Movie> movies) {
        mMovieList.clear();
        mMovieList.addAll(movies);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("---- [ViewPager] onPageSelected, selected position [" + position + "] ----");
        if (!isRecyclerScroll) {
            mRecyclerView.smoothScrollToPosition(position);
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
