package com.ickkey.dztenant.fragment.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.base.BaseMainFragment;
import com.ickkey.dztenant.fragment.login.RegisterFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.ToastUtils;
import com.ickkey.dztenant.view.BottomBar;
import com.ickkey.dztenant.view.BottomBarTab;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hhj on 2017/7/27.
 */

public class HomeFragment extends BaseMainFragment {

    public static final int MAIN = 0;
    public static final int PERSONAL = 1;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;



    private SupportFragment[] mFragments = new SupportFragment[2];
    @Override
    public int getLayoutId() {
        return R.layout.fm_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseFragment firstFragment = findChildFragment(HomeMainFragment.class);
        if (firstFragment == null) {
            mFragments[MAIN] = HomeMainFragment.newInstance(HomeMainFragment.class);
            mFragments[PERSONAL] = HomePersonalFragment.newInstance(HomePersonalFragment.class);

            loadMultipleRootFragment(R.id.fl_tab_container, MAIN,
                    mFragments[MAIN],
                    mFragments[PERSONAL]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.findFragmentByTag自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[MAIN] = firstFragment;
            mFragments[PERSONAL] = findChildFragment(HomePersonalFragment.class);
        }
        HomeMainFragment homeMainFragment= (HomeMainFragment) mFragments[MAIN];
        homeMainFragment.setHomeFragment(HomeFragment.this);
        HomePersonalFragment homePersonalFragment= (HomePersonalFragment) mFragments[PERSONAL];
        homePersonalFragment.setHomeFragment(HomeFragment.this);

    }
    @Override
    public void initView() {
        setTitleGone();
      /*  btn_left_base.setVisibility(View.INVISIBLE);
        setTitle(getString(R.string.text_main_pager));*/
        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.mian_icon, getString(R.string.text_main_pager)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.user_icon, getString(R.string.text_personal_pager)));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }
}
