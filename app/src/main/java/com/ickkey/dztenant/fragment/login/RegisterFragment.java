package com.ickkey.dztenant.fragment.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.request.GetVerifyReq;
import com.ickkey.dztenant.net.request.RegisterReq;
import com.ickkey.dztenant.net.response.BaseResponse;
import com.ickkey.dztenant.net.response.GetVerifyResp;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.ValidatorUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/7/27.
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.ll_pwd)
    LinearLayout ll_pwd;
    @BindView(R.id.ll_verify)
    LinearLayout ll_verify;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_verify)
    EditText et_verify;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_pwd_confirm)
    EditText et_pwd_confirm;

    @BindView(R.id.tv_step1)
    TextView tv_step1;
    @BindView(R.id.tv_step2)
    TextView tv_step2;
    @BindView(R.id.tv_step3)
    TextView tv_step3;


    @BindView(R.id.btn_submit_verify)
    TextView btn_submit_verify;

    @BindView(R.id.btn_register)
    TextView btn_register;
    String  verifyCode="";
    @Override
    public int getLayoutId() {
        return R.layout.fm_register;
    }

    @Override
    public void initView() {
        setTitle("注册");
        btn_submit_verify.setClickable(false);
        btn_register.setClickable(false);
        et_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable.toString())){
                    btn_submit_verify.setClickable(true);
                    btn_submit_verify.setBackgroundResource(R.drawable.login_btn_bg);
                }else {
                    btn_submit_verify.setClickable(false);
                    btn_submit_verify.setBackgroundResource(R.drawable.login_btn_bg_disable);
                }

            }
        });
        et_pwd.addTextChangedListener(new BetweenWatcher());
        et_pwd_confirm.addTextChangedListener(new BetweenWatcher());

    }
    public class BetweenWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(et_pwd.getText().toString().length()>0&&et_pwd_confirm.getText().toString().length()>0){
                btn_register.setClickable(true);
                btn_register.setBackgroundResource(R.drawable.login_btn_bg);
            }else {
                btn_register.setClickable(false);
                btn_register.setBackgroundResource(R.drawable.login_btn_bg_disable);
            }

        }
    }
    public void step2(){
        ll_phone.setVisibility(View.GONE);
        ll_verify.setVisibility(View.VISIBLE);
        tv_step1.setTextColor(_mActivity.getResources().getColor(R.color.global_tv_color));
        tv_step2.setTextColor(_mActivity.getResources().getColor(R.color.colorPrimaryDark));
    }
    public void step3(){
        ll_verify.setVisibility(View.GONE);
        ll_pwd.setVisibility(View.VISIBLE);
        tv_step2.setTextColor(_mActivity.getResources().getColor(R.color.global_tv_color));
        tv_step3.setTextColor(_mActivity.getResources().getColor(R.color.colorPrimaryDark));
    }
    @OnClick({R.id.btn_verify,R.id.btn_submit_verify,R.id.btn_register})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_verify:
                if(!ValidatorUtils.isMobile(et_phone.getText().toString())){
                    showToast("手机号码不合法");
                      return;
                }
                DialogUtils.showProgressDialog(_mActivity);
                GetVerifyReq req=new GetVerifyReq();
                req.mobile=et_phone.getText().toString().trim();
                NetEngine.getInstance().sendGetVerifyRequest(_mActivity, new CommonResponseListener<GetVerifyResp>(){
                    @Override
                    public void onSucceed(GetVerifyResp getVerifyResp) {
                        super.onSucceed(getVerifyResp);
                        showToast("验证码已发送，请注意查收");
                        verifyCode=getVerifyResp.phoneCode;
                        step2();
                    }
                },fragment_tag,req);

                break;
            case R.id.btn_submit_verify:
                if(!et_verify.getText().toString().equals(verifyCode)){
                    showToast("验证码不正确");
                    return;
                }
                step3();
                break;
            case R.id.btn_register:
                if(et_pwd.getText().toString().trim().length()<6||et_pwd_confirm.getText().toString().trim().length()<6){
                    showToast(getString(R.string.pwd_too_short));
                    return;
                }
                if(!et_pwd.getText().toString().trim().equals(et_pwd_confirm.getText().toString().trim())){
                    showToast(getString(R.string.pwd_confirm_error));
                    return;
                }
                RegisterReq registerReq=new RegisterReq();
                registerReq.code=verifyCode;
                registerReq.mobile=et_phone.getText().toString().trim();
                registerReq.password=et_pwd.getText().toString().trim();
                DialogUtils.showProgressDialog(_mActivity);
                NetEngine.getInstance().sendRegisterRequest(_mActivity,new CommonResponseListener<BaseResponse>(){
                    @Override
                    public void onSucceed(BaseResponse registerResp) {
                        super.onSucceed(registerResp);
                        showToast("注册成功");
                        pop();
                    }
                },fragment_tag,registerReq);
                break;
        }
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
