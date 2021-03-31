package com.divesh.spicyfood.Utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ZoomCardLayoutManager extends LinearLayoutManager {

    // Shrink the cards around the center up to 10%
    private final float mShrinkAmount = 0.1f;
    // The cards will be at 50% when they are 95% of the way between the
    // center and the edge.
    private final float mShrinkDistance = 0.95f;

    public ZoomCardLayoutManager(Context context) {
        super(context);
    }

    public ZoomCardLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ZoomCardLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state){

        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

        float point = getWidth() / 3.f;
        float d0 = 0.f;
        float d1 = mShrinkDistance * point;
        float s0 = 1.f;
        float s1 = 1.f - mShrinkAmount;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidpoint =
                    (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            float d = Math.min(d1, Math.abs(point - childMidpoint));
            float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }

        return scrolled;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollHorizontallyBy(0, recycler, state);
    }
}
