package com.cannan.android.moviebrowser.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: GalleryRecyclerView
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 20:29
 */
public class CustomRecyclerView extends RecyclerView implements CustomItemDecoration.OnItemSizeMeasuredListener {

    private int mInitPos = -1;

    private ScrollManager mScrollManager;

    private CustomItemDecoration mDecoration;

    public CustomItemDecoration getDecoration() {
        return mDecoration;
    }

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        attachDecoration();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }


    public void initListener(OnScrollListener listener) {
        mScrollManager = new ScrollManager(this);
        mScrollManager.initScrollListener(listener);
    }


    private void attachDecoration() {
        mDecoration = new CustomItemDecoration();
        mDecoration.setOnItemSizeMeasuredListener(this);
        addItemDecoration(mDecoration);
    }

    public int getScrolledPosition() {
        if (mScrollManager == null) {
            return 0;
        } else {
            return mScrollManager.getPosition();
        }
    }

    /**
     * @param i int
     * @return GalleryRecyclerView
     */
    public CustomRecyclerView initPosition(@IntRange(from = 0) int i) {
        if (i >= getAdapter().getItemCount()) {
            i = getAdapter().getItemCount() - 1;
        } else if (i < 0) {
            i = 0;
        }
        mInitPos = i;
        return this;
    }

    /**
     * 装载
     *
     * @return GalleryRecyclerView
     */
    public CustomRecyclerView setUp() {
        if (getAdapter().getItemCount() <= 0) {
            return this;
        }

        smoothScrollToPosition(0);
        mScrollManager.updateConsume();

        return this;
    }

    @Override
    public void onItemSizeMeasured(int size) {
        if (mInitPos < 0) {
            return;
        }
        if (mInitPos == 0) {
            scrollToPosition(0);
        } else {
            smoothScrollBy(mInitPos * size, 0);
        }
        mInitPos = -1;
    }

    /**
     * 设置页面参数，单位dp
     *
     * @param pageMargin
     * @param leftPageVisibleWidth
     * @return GalleryRecyclerView
     */
    public CustomRecyclerView initPageParams(int pageMargin, int leftPageVisibleWidth) {
        mDecoration.mPageMargin = pageMargin;
        mDecoration.mLeftPageVisibleWidth = leftPageVisibleWidth;
        return this;
    }
}