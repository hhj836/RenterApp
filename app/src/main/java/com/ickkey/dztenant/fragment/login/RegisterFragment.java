package com.ickkey.dztenant.fragment.login;

import android.os.Bundle;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.utils.LogUtil;

/**
 * Created by hhj on 2017/7/27.
 */

public class RegisterFragment extends BaseFragment {

    @Override
    public boolean enableSwipeBack() {
        return true;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fm_register;
    }

    @Override
    public void initView() {
        setTitle("注册");

    }
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        LogUtil.info(getClass(),"onNewBundle");
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        // todo,当该Fragment对用户可见时
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        // todo,当该Fragment对用户不可见时
    }
}
