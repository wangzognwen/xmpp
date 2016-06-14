package com.juns.wechat.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.juns.wechat.view.fragment.Fragment_Dicover;
import com.juns.wechat.view.fragment.Fragment_Friends;
import com.juns.wechat.view.fragment.Fragment_Msg;
import com.juns.wechat.view.fragment.Fragment_Profile;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MainAdapter extends FragmentPagerAdapter {

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_Msg();
            case 1:
                return new Fragment_Friends();
            case 2:
                return new Fragment_Dicover();
            case 3:
                return new Fragment_Profile();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


}
