package com.example.a21__void.Modules;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;

public class PageTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private ViewPager viewPager;
    private SalonsFragementAdapter cardAdapter;
    private float lastOffset;
    private boolean scalingEnabled;

    public PageTransformer(ViewPager viewPager, SalonsFragementAdapter adapter) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        cardAdapter = adapter;
    }

    public void enableScaling(boolean enable) {
        if (scalingEnabled && !enable) {
            // shrink main card
            View currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
            if (currentCard != null) {
                currentCard.animate().scaleY(1);
                currentCard.animate().scaleX(1);
            }
        } else if (!scalingEnabled && enable) {
            // grow main card
            View currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
            if (currentCard != null) {
                //enlarge the current item
                currentCard.animate().scaleY(1.1f);
                currentCard.animate().scaleX(1.1f);
            }
        }
        scalingEnabled = enable;
    }

    @Override
    public void transformPage(View page, float position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = 8f;
        float realOffset;
        boolean goingLeft = lastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        if (nextPosition > cardAdapter.getCount() - 1
                || realCurrentPosition > cardAdapter.getCount() - 1) {
            return;
        }

        View currentCard = cardAdapter.getCardViewAt(realCurrentPosition);

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentCard != null) {
            if (scalingEnabled) {
                currentCard.setScaleX((float) (1 + 0.08 * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + 0.08 * (1 - realOffset)));
            }
            //currentCard.setCardElevation((baseElevation + baseElevation
                    //* (5 - 1) * (1 - realOffset)));
        }

        View nextCard = cardAdapter.getCardViewAt(nextPosition);

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextCard != null) {
            if (scalingEnabled) {
                nextCard.setScaleX((float) (1 + 0.08 * (realOffset)));
                nextCard.setScaleY((float) (1 + 0.08 * (realOffset)));
            }
            //nextCard.setCardElevation((baseElevation + baseElevation
                    //* (5 - 1) * (realOffset)));
        }

        lastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}