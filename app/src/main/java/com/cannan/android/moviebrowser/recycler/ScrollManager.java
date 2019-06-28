package com.cannan.android.moviebrowser.recycler;

import android.view.View;

import com.cannan.android.moviebrowser.common.DisplayUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * @ClassName: ScrollManager
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 20:25
 */
public class ScrollManager {

    private CustomRecyclerView mCustomRecyclerView;

    private SnapHelper mHelper;

    private int mPosition = 0;

    private int mConsumeX = 0;

    private RecyclerView.OnScrollListener mListener;

    ScrollManager(CustomRecyclerView recyclerView, SnapHelper helper) {
        mCustomRecyclerView = recyclerView;
        mHelper = helper;
    }

    void initScrollListener(RecyclerView.OnScrollListener listener) {
        mListener = listener;

        CustomScrollerListener mScrollerListener = new CustomScrollerListener();
        mCustomRecyclerView.addOnScrollListener(mScrollerListener);
    }

    class CustomScrollerListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mListener != null) {
                mListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            mConsumeX += dx;
            onScrolledChangedCallback();

            if (mListener != null) {
                mListener.onScrolled(recyclerView, dx, dy);
            }
        }
    }

    /**
     * view 大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        int mOnePageWidth = mCustomRecyclerView.getDecoration().mItemConsumeX;

        if (mOnePageWidth <= 0) {
            return;
        }

        RecyclerView.LayoutManager manager = mCustomRecyclerView.getLayoutManager();
        View snapView = mHelper.findSnapView(manager);
        if (snapView == null) {
            return;
        }
        mPosition = manager.getPosition(snapView);

        int offset = mConsumeX - mPosition * mOnePageWidth;
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);

        View leftView = null;
        View currentView;
        View rightView = null;
        if (mPosition > 0) {
            leftView = mCustomRecyclerView.getLayoutManager().findViewByPosition(mPosition - 1);
        }
        currentView = mCustomRecyclerView.getLayoutManager().findViewByPosition(mPosition);
        if (mPosition < mCustomRecyclerView.getAdapter().getItemCount() - 1) {
            rightView = mCustomRecyclerView.getLayoutManager().findViewByPosition(mPosition + 1);
        }

        /**
         * 缩放比
         */
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

    public void updateConsume() {
        mConsumeX += DisplayUtil.dp2px(mCustomRecyclerView.getDecoration().mHalfWidth + mCustomRecyclerView.getDecoration().mPageMargin * 2);
    }


    public int getPosition() {
        return mPosition;
    }
}
