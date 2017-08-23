package com.ickkey.dztenant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.utils.ToastUtils;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author 侯慧杰
 * @date 2017/8/20
 * @Description:
 */


public class GestureLoginActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle=new Bundle();
        bundle.putInt(ConstantValues.GESTURE_PAGER_TYPE, ConstantValues.GESTURE_HANDLE_HOME_IN);
        loadRootFragment(R.id.fl_container, GestureLoginFragment.newInstance(GestureLoginFragment.class,bundle));
        RenterApp.getInstance().activityMap.put(GestureLoginActivity.class,GestureLoginActivity.this);
    }

    @Override
    public void onBackPressedSupport() {
        ToastUtils.showShortToast(GestureLoginActivity.this,"请完成手势密码验证");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RenterApp.getInstance().activityMap.remove(GestureLoginActivity.class);
    }
}
