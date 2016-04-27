package net.dell.ui_viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dell on 2016/4/26.
 */
public class TabPagerAdapter extends PagerAdapter {

    private List<View> layoutList;

    public TabPagerAdapter(List<View> layoutList) {
        this.layoutList = layoutList;
    }

    @Override
    public int getCount() {
        return layoutList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(layoutList.get(position));
        return layoutList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(layoutList.get(position));
    }

}
