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

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoginOut(LoginOutEvent event) {
            if(RenterApp.getInstance().fragmentMap.get(HomeFragment.class)!=null){
                RenterApp.getInstance().fragmentMap.get(HomeFragment.class).start(LoginFragment.newInstance(LoginFragment.class));
            }
            RenterApp.getInstance().logOut(MainActivity.this);
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
