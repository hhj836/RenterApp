package com.ickkey.dztenant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.fragment.login.LaunchFragment;
import com.ickkey.dztenant.utils.cache.ACache;
import butterknife.ButterKnife;
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
    }

}
