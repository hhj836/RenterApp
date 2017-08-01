package com.ickkey.dztenant.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.view.ScaleCircleNavigator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import butterknife.BindView;

/**
 * Created by hhj on 2017/7/27.
 */

public class HomeMainFragment extends BaseFragment {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;


    FragmentPagerAdapter  fragmentPagerAdapter;
    HomeFragment homeFragment;
    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fm_home_main;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.text_main_pager));
        btn_left_base.setVisibility(View.INVISIBLE);
        fragmentPagerAdapter=new PagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(fragmentPagerAdapter);
        initMagicIndicator();

    }

    private void initMagicIndicator() {
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(_mActivity);
        scaleCircleNavigator.setCircleCount(fragmentPagerAdapter.getCount());
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(Color.DKGRAY);
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
    public class PagerAdapter extends FragmentPagerAdapter{


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return HomeMainRoomLockFragment.newInstance(HomeMainRoomLockFragment.class);
            }else {
                return HomeMainRoomLockFragment.newInstance(HomeMainRoomLockFragment.class);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
