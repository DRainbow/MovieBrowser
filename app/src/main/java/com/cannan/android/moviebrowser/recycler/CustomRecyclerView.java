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

    private Context mContext;

    /**
     * RecyclerView 初始展示位置
     */
    private int mInitPos = 0;

    private ScrollManager mScrollManager;

    private CustomItemDecoration mDecoration;

    public CustomItemDecoration getDecoration() {
        return mDecoration;
    }

    public CustomRecyclerView(Context context) {
        this(context, null);
        mContext = context;
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        attachDecoration();
    }

    public void initListener(OnScrollListener listener) {
        mScrollManager = new ScrollManager(mContext, this);
        mScrollManager.initScrollListener(listener);
    }


    private void attachDecoration() {
        mDecoration = new CustomItemDecoration(mContext);
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
     * 装载
     *
     * @return CustomRecyclerView
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