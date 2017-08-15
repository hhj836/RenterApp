package com.ickkey.dztenant.fragment.gesture;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.fragment.login.RegisterFragment;
import com.ickkey.dztenant.view.UpdateEtWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/8/1.
 */

public class LoginPwdCheckFragment extends BaseFragment {
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;
    @BindView(R.id.forget_pwd)
    TextView forget_pwd;
    @Override
    public int getLayoutId() {
        return R.layout.fm_login_pwd_check;
    }

    @Override
    public void initView() {
        setTitle("密码登入验证");
        btn_confirm.setClickable(false);
        et_pwd.addTextChangedListener(new UpdateEtWatcher(btn_confirm));

    }
    @OnClick({R.id.btn_confirm,R.id.forget_pwd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_confirm:
                if(et_pwd.getText().toString().equals(RenterApp.getInstance().getUserInfo().pwd)){
                    if(findFragment(GestureLoginFragment.class)!=null){
                        GestureLoginFragment fragment=findFragment(GestureLoginFragment.class);
                        fragment.getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                    }
                    startWithPop(HomeFragment.newInstance(HomeFragment.class));
                }
                break;
            case R.id.forget_pwd:
                Bundle bundle=new Bundle();
                bundle.putInt(ConstantValues.REGISTER_OR_PWD,ConstantValues.FRAGMENT_FORGET_PWD);
                start(RegisterFragment.newInstance(RegisterFragment.class,bundle));
                break;
        }


    }
}
