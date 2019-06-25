package com.cannan.android.moviebrowser.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: CardLinearSnapHelper
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 01:01
 */
public class CardLinearSnapHelper extends LinearSnapHelper {

    public boolean mNoNeedToScroll = false;

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        if (mNoNeedToScroll) {
            return new int[]{0, 0};
        } else {
            return super.calculateDistanceToFinalSnap(layoutManager, targetView);
        }
    }

}
