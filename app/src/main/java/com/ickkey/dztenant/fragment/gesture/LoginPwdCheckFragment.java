package com.ickkey.dztenant.fragment.gesture;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
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
    @OnClick(R.id.btn_confirm)
    public void onClick(){
        if(et_pwd.getText().toString().equals(RenterApp.getInstance().getUserInfo().pwd)){
            if(findFragment(GestureLoginFragment.class)!=null){
                GestureLoginFragment fragment=findFragment(GestureLoginFragment.class);
                fragment.getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
            startWithPop(HomeFragment.newInstance(HomeFragment.class));
        }

    }
}
