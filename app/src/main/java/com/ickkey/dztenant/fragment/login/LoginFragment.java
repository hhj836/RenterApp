package com.ickkey.dztenant.fragment.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.andexert.library.RippleView;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.activity.MainActivity;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.fragment.home.HomeMainFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.response.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import me.yokeyword.fragmentation.ISupportFragment;


/**
 * Created by hhj on 2017/7/27.
 *
 */

public class LoginFragment extends BaseFragment {

    @OnClick(R.id.test)
    public void onClick(){
        start(RegisterFragment.newInstance(RegisterFragment.class));

    }


    @Override
    public int getLayoutId() {
        return R.layout.fm_login;
    }

    @Override
    public void initView() {
        setTitle("登录");
        setBtnRightText(getString(R.string.text_main_pager));
        setOnBtnRightClickListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                startWithPop(HomeFragment.newInstance(HomeFragment.class));
            }
        });
        btn_left_base.setVisibility(View.INVISIBLE);
        Map<String,Object> map=new HashMap<>();
        map.put("username","10086");
        map.put("password","10086");
        map.put("captcha","10086");
        NetEngine.sendLoginRequest(_mActivity, new CommonResponseListener<LoginResponse>() {
            @Override
            public void onSucceed(LoginResponse testResponseBean) {

            }

        },fragment_tag,map);


    }
}
