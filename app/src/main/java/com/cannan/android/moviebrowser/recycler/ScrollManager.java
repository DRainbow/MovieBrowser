package com.cannan.android.moviebrowser.recycler;

import android.content.Context;
import android.view.View;

import com.cannan.android.moviebrowser.common.DisplayUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: ScrollManager
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 20:25
 */
public class ScrollManager {

    private Context mContext;

    private CustomRecyclerView mCustomRecyclerView;

    private int mPosition = 0;

    private int mConsumeX = 0;

    private RecyclerView.OnScrollListener mListener;

    public ScrollManager(Context context, CustomRecyclerView mCustomRecyclerView) {
        mContext = context;
        this.mCustomRecyclerView = mCustomRecyclerView;
    }

    public void initScrollListener(RecyclerView.OnScrollListener listener) {
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
     * 缩放比
     */
    private float mScale = 0.4f;

    /**
     * view 大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        int mOnePageWidth = mCustomRecyclerView.getDecoration().mItemConsumeX;
        mPosition = (int) ((float) mConsumeX / (float) mOnePageWidth);

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

        System.out.println("---- [RecyclerView.onScrolled] mPosition [" + mPosition + "], mConsumeX [" + mConsumeX +
                "], offset [" + offset + "], percent [" + percent + "] ----");

        if (leftView != null) {
            leftView.setScaleY((1 - mScale) + percent * mScale);
            leftView.setScaleY((1 - mScale) + percent * mScale);
        }
        if (currentView != null) {
            currentView.setScaleX(1 - percent * mScale);
            currentView.setScaleY(1 - percent * mScale);
        }
        if (rightView != null) {
            rightView.setScaleX((1 - mScale) + percent * mScale);
            rightView.setScaleY((1 - mScale) + percent * mScale);
        }
    }

    public void updateConsume() {
        mConsumeX += DisplayUtil.dp2px(mCustomRecyclerView.getDecoration().mHalfWidth + mCustomRecyclerView.getDecoration().mPageMargin * 2);
    }


    public int getPosition() {
        return mPosition;
    }
}
