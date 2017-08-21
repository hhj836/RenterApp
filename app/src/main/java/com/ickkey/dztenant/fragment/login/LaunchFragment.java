package com.ickkey.dztenant.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.gesture.CreateGestureFragment;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.LoginResponse;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by hhj on 2017/8/1.
 */

public class LaunchFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fm_launch;
    }

    @Override
    public void initView() {
        setFragmentAnimator(new FragmentAnimator(R.anim.anim_fade_in,R.anim.anim_fade_out,R.anim.anim_fade_in,R.anim.anim_fade_out));
        setTitleGone();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(RenterApp.getInstance().getUserInfo()==null){
                    startWithPop(LoginFragment.newInstance(LoginFragment.class));
                }else {
                    LoginReq loginReq=new LoginReq();
                    loginReq.mobile=RenterApp.getInstance().getUserInfo().username;
                    loginReq.password=RenterApp.getInstance().getPwd();
                    NetEngine.getInstance().sendLoginRequest(_mActivity,new CommonResponseListener<LoginResponse>(){
                        @Override
                        public void onSucceed(LoginResponse loginResponse) {
                            super.onSucceed(loginResponse);
                            RenterApp.getInstance().saveUserInfo(loginResponse);
                            goOn();

                        }
                    },fragment_tag,loginReq);

                }
            }
        },500);

    }


    public void goOn(){
        Bundle bundle=new Bundle();
        bundle.putInt(ConstantValues.GESTURE_PAGER_TYPE, ConstantValues.GESTURE_HANDLE_LOGIN_IN);
        if(RenterApp.getInstance().getCache().getAsBinary(ConstantValues.GESTURE_PASSWORD)!=null){
            startWithPop(GestureLoginFragment.newInstance(GestureLoginFragment.class,bundle));
        }else {
            startWithPop(CreateGestureFragment.newInstance(CreateGestureFragment.class,bundle));
        }
    }
}
