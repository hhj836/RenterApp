package com.ickkey.dztenant.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.gesture.CreateGestureFragment;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.LogUtil;


import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * Created by hhj on 2017/7/27.
 *
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @Override
    public boolean onBackPressedSupport() {
        LogUtil.info(getClass(),"onBackPressedSupport");
        _mActivity.finish();
        return true;
    }

    @OnClick({R.id.tv_register,R.id.tv_forget_pwd,R.id.btn_login})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_register:
                start(RegisterFragment.newInstance(RegisterFragment.class));
                break;
            case R.id.btn_login:
                if(TextUtils.isEmpty(et_username.getText().toString())){
                    showToast("请输入用户名");
                    return;

                }
                if(TextUtils.isEmpty(et_pwd.getText().toString())){
                    showToast("请输入密码");
                    return;

                }
                if(et_pwd.getText().toString().trim().length()<6){
                    showToast(getString(R.string.pwd_too_short));
                    return;

                }
                DialogUtils.showProgressDialog(_mActivity);
                LoginReq loginReq=new LoginReq();
                loginReq.mobile=et_username.getText().toString();
                loginReq.password=et_pwd.getText().toString();
                NetEngine.getInstance().sendLoginRequest(_mActivity,new CommonResponseListener<LoginResponse>(){
                    @Override
                    public void onSucceed(LoginResponse loginResponse) {
                        super.onSucceed(loginResponse);
                        RenterApp.getInstance().setPwd(et_pwd.getText().toString());
                        RenterApp.getInstance().saveUserInfo(loginResponse);
                        Bundle bundle=new Bundle();
                        bundle.putInt(ConstantValues.GESTURE_PAGER_TYPE, ConstantValues.GESTURE_HANDLE_LOGIN_IN);
                        if(RenterApp.getInstance().getCache().getAsBinary(ConstantValues.GESTURE_PASSWORD+loginResponse.userId)!=null){
                            start(GestureLoginFragment.newInstance(GestureLoginFragment.class,bundle));
                        }else {
                            start(CreateGestureFragment.newInstance(CreateGestureFragment.class,bundle));
                        }


                    }
                },fragment_tag,loginReq);
                break;
            case R.id.tv_forget_pwd:
                Bundle bundle=new Bundle();
                bundle.putInt(ConstantValues.REGISTER_OR_PWD,ConstantValues.FRAGMENT_FORGET_PWD);
                start(RegisterFragment.newInstance(RegisterFragment.class,bundle));
                break;
        }


    }



    @Override
    public int getLayoutId() {
        return R.layout.fm_login;
    }

    @Override
    public void onBtnRightClick() {
        startWithPop(HomeFragment.newInstance(HomeFragment.class));
    }

    @Override
    public void initView() {
        setFragmentAnimator(new FragmentAnimator(R.anim.anim_fade_in,R.anim.anim_fade_out, me.yokeyword.fragmentation.R.anim.h_fragment_pop_enter,me.yokeyword.fragmentation.R.anim.h_fragment_pop_exit));
        setTitleGone();


    }

}
