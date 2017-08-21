package com.ickkey.dztenant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.event.LoginOutEvent;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.fragment.login.LaunchFragment;
import com.ickkey.dztenant.fragment.login.LoginFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(MainActivity.this);
        setContentView(R.layout.activity_main);
        loadRootFragment(R.id.fl_container, LaunchFragment.newInstance(LaunchFragment.class));
        RenterApp.getInstance().set_mActivity(MainActivity.this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoginOut(LoginOutEvent event) {

            if(RenterApp.getInstance().fragmentMap.get(HomeFragment.class)!=null){
                LoginFragment loginFragment= RenterApp.getInstance().fragmentMap.get(HomeFragment.class).findFragment(LoginFragment.class);
                RenterApp.getInstance().fragmentMap.get(HomeFragment.class).start(loginFragment==null?LoginFragment.newInstance(LoginFragment.class):loginFragment, ISupportFragment.SINGLETASK);
            }
        RenterApp.getInstance().logOut();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainActivity.this);
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }
}
