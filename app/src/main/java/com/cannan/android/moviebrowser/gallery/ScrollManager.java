package com.cannan.android.moviebrowser.gallery;

import android.view.View;

import com.cannan.android.moviebrowser.common.DisplayUtil;
import com.cannan.android.moviebrowser.GalleryRecyclerView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: ScrollManager
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 20:25
 */
public class ScrollManager {

    private GalleryRecyclerView mGalleryRecyclerView;

    private int mPosition = 0;

    private int mConsumeX = 0;

    private RecyclerView.OnScrollListener mListener;

    public ScrollManager(GalleryRecyclerView mGalleryRecyclerView) {
        this.mGalleryRecyclerView = mGalleryRecyclerView;
    }

    public void initScrollListener(RecyclerView.OnScrollListener listener) {
        mListener = listener;

        ScrollManager.GalleryScrollerListener mScrollerListener = new ScrollManager.GalleryScrollerListener();
        mGalleryRecyclerView.addOnScrollListener(mScrollerListener);
    }

    class GalleryScrollerListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            onHorizontalScroll(recyclerView, dx);
            if (mListener != null) {
                mListener.onScrolled(recyclerView, dx, dy);
            }
        }
    }

    public void updateConsume() {
        mConsumeX += DisplayUtil.dp2px(mGalleryRecyclerView.getDecoration().mLeftPageVisibleWidth + mGalleryRecyclerView.getDecoration().mPageMargin * 2);
    }

    private void onHorizontalScroll(final RecyclerView recyclerView, final int dx) {
        mConsumeX += dx;

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int shouldConsumeX = mGalleryRecyclerView.getDecoration().mItemConsumeX;
                float offset = (float) mConsumeX / (float) shouldConsumeX;
                float percent = offset - ((int) offset);
                System.out.println("---- 当前页移动的百分值 " + percent + " ----");

                mPosition = (int) offset;
                onScrollChanged(recyclerView, mPosition, percent);
            }
        });

    }

    public int getPosition() {
        return mPosition;
    }

    private float mAnimFactor = 0.2f;

    public void onScrollChanged(RecyclerView recyclerView, int position, float percent) {
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1);
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);
        View mRRView = recyclerView.getLayoutManager().findViewByPosition(position + 2);

        if (mLeftView != null) {
            mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
            mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
        }
        if (mCurView != null) {
            mCurView.setScaleX(1 - percent * mAnimFactor);
            mCurView.setScaleY(1 - percent * mAnimFactor);
        }
        if (mRightView != null) {
            mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
            mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
        }
        if (mRRView != null) {
            mRRView.setScaleX(1 - percent * mAnimFactor);
            mRRView.setScaleY(1 - percent * mAnimFactor);
        }
    }
}
