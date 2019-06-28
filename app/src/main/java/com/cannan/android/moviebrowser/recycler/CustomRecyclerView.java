package com.cannan.android.moviebrowser.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: CustomRecyclerView
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 20:29
 */
public class CustomRecyclerView extends RecyclerView implements CustomItemDecoration.OnItemSizeMeasuredListener {

    /**
     * RecyclerView 初始展示位置
     */
    private int mInitPos = 0;

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

    private void attachDecoration() {
        mDecoration = new CustomItemDecoration();
        mDecoration.setOnItemSizeMeasuredListener(this);
        addItemDecoration(mDecoration);
    }

    /**
     * 装载
     *
     * @return CustomRecyclerView
     */
    public CustomRecyclerView setUp() {
        if (getAdapter().getItemCount() <= 0) {
            return this;
        }

        smoothScrollToPosition(0);
        return this;
    }

    @Override
    public void onItemSizeMeasured(int size) {
        if (0 > mInitPos) {
            return;
        }
        if (0 == mInitPos) {
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
     * @return CustomRecyclerView
     */
    public CustomRecyclerView initPageParams(int pageMargin, int leftPageVisibleWidth) {
        mDecoration.mPageMargin = pageMargin;
        mDecoration.mHalfWidth = leftPageVisibleWidth;
        return this;
    }
}