package com.cannan.android.moviebrowser.recycler;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.cannan.android.moviebrowser.common.DisplayUtil;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: CustomItemDecoration
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 20:32
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    public int mPageMargin = 0;

    public int mLeftPageVisibleWidth = 0;

    public int mItemConsumeX = 0;

    private CustomItemDecoration.OnItemSizeMeasuredListener mOnItemSizeMeasuredListener;

    public CustomItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int position = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter().getItemCount();

        parent.post(new Runnable() {
            @Override
            public void run() {
                setParams(parent, view, position, itemCount);
            }
        });
    }

    /**
     * 设置水平滚动的参数
     *
     * @param parent    ViewGroup
     * @param itemView  View
     * @param position  int
     * @param itemCount int
     */
    private void setParams(ViewGroup parent, View itemView, int position, int itemCount) {
        int itemNewWidth = parent.getWidth() - DisplayUtil.dp2px(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
        int itemNewHeight = parent.getHeight();

        mItemConsumeX = itemNewWidth + DisplayUtil.dp2px(2 * mPageMargin);

        if (mOnItemSizeMeasuredListener != null) {
            mOnItemSizeMeasuredListener.onItemSizeMeasured(mItemConsumeX);
        }

        int leftMargin = position == 0 ? DisplayUtil.dp2px(mLeftPageVisibleWidth + 2 * mPageMargin) :
                DisplayUtil.dp2px(mPageMargin);
        int rightMargin = position == itemCount - 1 ? DisplayUtil.dp2px(mLeftPageVisibleWidth + 2 * mPageMargin) :
                DisplayUtil.dp2px(mPageMargin);

        setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth, itemNewHeight);
    }

    /**
     * 设置参数
     *
     * @param itemView   View
     * @param left       int
     * @param top        int
     * @param right      int
     * @param bottom     int
     * @param itemWidth  int
     * @param itemHeight int
     */
    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth,
                                 int itemHeight) {

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;
        boolean mHeightChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }
        if (lp.height != itemHeight) {
            lp.height = itemHeight;
            mHeightChange = true;

        }

        if (mWidthChange || mMarginChange || mHeightChange) {
            itemView.setLayoutParams(lp);
        }
    }

    public void setOnItemSizeMeasuredListener(CustomItemDecoration.OnItemSizeMeasuredListener itemSizeMeasuredListener) {
        this.mOnItemSizeMeasuredListener = itemSizeMeasuredListener;
    }

    public interface OnItemSizeMeasuredListener {
        /**
         * Item的大小测量完成
         *
         * @param size int
         */
        void onItemSizeMeasured(int size);
    }
}