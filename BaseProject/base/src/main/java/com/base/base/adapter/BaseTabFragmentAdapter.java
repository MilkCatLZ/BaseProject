package com.base.base.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import com.base.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LZ on 2016/8/29.
 */
public class BaseTabFragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    protected final Context context;
    private final FragmentManager fm;

    public BaseTabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.fm = fm;
    }

    protected List<String> getFragmentTitles() {
        return mFragmentTitles;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    public View getTabView(int position) {return null;}

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment

        Fragment cacheFragment = null;
        try {
            cacheFragment = (Fragment) super.instantiateItem(container, position);
        } catch (Exception e) {
            Log.e("BaseTabFragmentAdapter", "Fragment 重复了！！请检查addFragment是否重复！");
        }
        String fragmentTag = cacheFragment.getTag();

        Fragment fragment = mFragments.get(position);


        if (cacheFragment != fragment) {

            //如果这个fragment需要更新


            FragmentTransaction ft = fm.beginTransaction();

            //移除旧的fragment

            ft.remove(cacheFragment);
//            ft.remove(fragment);


            //添加新fragment时必须用前面获得的tag

            ft.add(container.getId(), fragment, fragmentTag);

            ft.attach(fragment);

            try {
                ft.commit();
                fm.executePendingTransactions();
            } catch (Exception e) {

            }
        }
        return fragment;
    }

    public void clearFragment() {
        mFragments.clear();
        mFragmentTitles.clear();
        notifyDataSetChanged();
    }
}
