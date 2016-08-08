package com.juns.wechat.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.juns.wechat.common.BaseFragment;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.view.fragment.Fragment_Dicover;
import com.juns.wechat.view.fragment.Fragment_Friends;
import com.juns.wechat.view.fragment.Fragment_Msg;
import com.juns.wechat.view.fragment.Fragment_Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/10.
 */
public class MainAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public MainAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    private void initFragments(){
        fragments = new ArrayList<>();
        fragments.add(new Fragment_Msg());
        fragments.add(new Fragment_Friends());
        fragments.add(new Fragment_Dicover());
        fragments.add(new Fragment_Profile());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }


}
