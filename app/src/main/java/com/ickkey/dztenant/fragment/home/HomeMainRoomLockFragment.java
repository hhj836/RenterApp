package com.ickkey.dztenant.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.view.RippleBackground;

import butterknife.BindView;

/**
 * Created by hhj on 2017/8/1.
 */

public class HomeMainRoomLockFragment extends BaseFragment {
    @BindView(R.id.tv_electric)
    TextView tv_electric;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.waveView)
    RippleBackground waveView;

    @Override
    public int getLayoutId() {
        return R.layout.fm_home_main_room_lock;
    }

    @Override
    public void initView() {
        setTitleGone();
        waveView.startRippleAnimation();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(waveView.isRippleAnimationRunning()){

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(waveView.isRippleAnimationRunning()){
            waveView.stopRippleAnimation();
        }
    }


}
