package cn.faury.android.library.viewer.image;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewLists;

    public ViewPagerAdapter(List<View> lists)
    {
        viewLists = lists;
    }

    //获得size
    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //销毁Item
    @Override
    public void destroyItem(ViewGroup view, int position, Object object)
    {

        view.removeView(viewLists.get(position));
    }

    //实例化Item
    @Override
    public Object instantiateItem(ViewGroup view, int position)
    {
        view.addView(viewLists.get(position), 0);
        return viewLists.get(position);
    }
}
