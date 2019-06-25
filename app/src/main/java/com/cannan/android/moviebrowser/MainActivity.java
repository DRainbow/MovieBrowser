package com.cannan.android.moviebrowser;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.cannan.android.moviebrowser.data.Movie;
import com.cannan.android.moviebrowser.viewmodels.MovieViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;

    private PagerAdapter mPagerAdapter;

    private List<Movie> mMovieList = new ArrayList<>();

    private MovieViewModel mMovieViewModel;

    private List<View> mViews = new ArrayList<>();
    /**
     * 视频控件合集
     */
    private List<VideoView> mVideoViewList = new ArrayList<>();

    private List<MovieView> mCacheView = new ArrayList<>();

//    private MoviePlayer mPlayer = new MoviePlayer();

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

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(final List<Movie> movies) {
                System.out.println("load movies:\n" + new Gson().toJson(movies));

                if (movies == null || movies.size() == 0) {
                    return;
                }

                initialData(movies);
                initAdapter();

                mViewPager.setCurrentItem(0);
                mCacheView.get(0).setFirstStart();

//                mPagerAdapter.notifyDataSetChanged();
//                mViewPager.setCurrentItem(0);

//                if (mVideoViewList.get(0) != null) {
//                    mVideoViewList.get(0).start();
//                }
            }
        });
    }

    private void assignViews() {
        mViewPager = findViewById(R.id.vp_main_top);
        mRecyclerView = findViewById(R.id.rv_main_bottom);
    }

    private void initAdapter() {
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mMovieList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
//                View root = mViews.get(position);
//                if (root.getParent() != null) {
//                    ((ViewGroup) root.getParent()).removeView(root);
//                }
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(200, 200);
//                root.setLayoutParams(lp);
//                container.addView(root, 0);

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
        };

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initialData(List<Movie> movies) {
        mMovieList.clear();
        mMovieList.addAll(movies);

        for (Movie movie : movies) {
//            View root = View.inflate(MainActivity.this, R.layout.page_layout, null);
//            MovieView videoView = root.findViewById(R.id.mVideoView);
//            videoView.setUri(movie.getVideoUrl());
//            mCacheView.add(videoView);
//            mViews.add(root);
            MovieView view = new MovieView(MainActivity.this, movie.getVideoUrl());
            mCacheView.add(view);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        System.out.println("---- onPageScrolled ----");
    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("---- onPageSelected ----");
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
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        System.out.println("---- onPageScrollStateChanged ----");

//        if (state == 0) {
//        } else if (state == 1) {
//            mCacheView.get(CurrentPage % MaxCount).pause();
//            Log.e("scrolled", "page num=" + CurrentPage);
//        } else if (state == 2) {
//            //SCROLL BACK RESUME
//            if (CurrentPage == SwipePage) {
//                mediaViewList.get(CurrentPage % MaxCount).start();
//            }
//        }
    }
}
