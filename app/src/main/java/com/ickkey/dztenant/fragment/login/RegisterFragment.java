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

}
