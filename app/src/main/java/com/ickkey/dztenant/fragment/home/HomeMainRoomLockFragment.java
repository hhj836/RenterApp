package com.ickkey.dztenant.fragment.home;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.event.LockRefreshEvent;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.Urls;
import com.ickkey.dztenant.net.request.GetLocksIdReq;
import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.GetLocksIdResp;
import com.ickkey.dztenant.net.response.GetLocksResp;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.Json2ObjHelper;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.ToastUtils;
import com.ickkey.dztenant.view.RippleBackground;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/8/1.
 */

public class HomeMainRoomLockFragment extends BaseFragment {
    public static  final int LOCK_ONLINE=1;
    public static  final int LOCK_OFFLINE=0;
    @BindView(R.id.tv_electric)
    TextView tv_electric;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.waveView)
    RippleBackground waveView;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    GetLocksResp.LockItem  lock;
    GetLocksIdResp getLocksIdResp;
    public void setHomeMainFragment(HomeMainFragment homeMainFragment) {
        this.homeMainFragment = homeMainFragment;
    }

    HomeMainFragment homeMainFragment;
    @OnClick({R.id.rl_bg})
    public void onClick(){
        if(RenterApp.getInstance().getUserInfo().isVisitor==0){
            showToast("你未被授权门锁，无法刷新");
            return;
        }
            if(lock!=null){
                GetLocksIdReq getLocksIdReq=new GetLocksIdReq();
                getLocksIdReq.locksId=lock.id;
                getLocksIdReq.token=RenterApp.getInstance().getUserInfo().token;
                NetEngine.getInstance().getHttpResult(new CommonResponseListener(){
                    @Override
                    public void onSucceed(Object o) {
                        super.onSucceed(o);
                        getLocksIdResp= (GetLocksIdResp) o;
                        if(getLocksIdResp.msg!=null){
                            lock=getLocksIdResp.msg;
                                    if(lock!=null){
                                        LockRefreshEvent event=new LockRefreshEvent();
                                        event.lockItem=lock;
                                        EventBus.getDefault().post(event);
                                        showToast("刷新成功");
                                        rl_bg.setVisibility(View.VISIBLE);
                                        tv_electric.setText("电量"+lock.quantity+"%");
                                        if(lock.isOnlie==1){
                                            rl_bg.setBackgroundResource(R.drawable.circle_green);
                                            waveView.startRippleAnimation();
                                            tv_status.setText("正常");

                                        }else {
                                            rl_bg.setBackgroundResource(R.drawable.circle_gray);
                                            waveView.stopRippleAnimation();
                                            tv_status.setText("离线");
                                        }
                                    }
                        }


                    }
                }, Urls.GET_LOCKS_ID,GetLocksIdResp.class,_mActivity,getLocksIdReq);
            }
    }
    @Override
    public int getLayoutId() {
        return R.layout.fm_home_main_room_lock;
    }

    @Override
    public void initView() {
        setTitleGone();
        lock= (GetLocksResp.LockItem) getArguments().getSerializable("LockItem");
        if(lock!=null){
            rl_bg.setVisibility(View.VISIBLE);
            tv_electric.setText("电量"+lock.quantity+"%");
            if(lock.isOnlie==LOCK_ONLINE){
                rl_bg.setBackgroundResource(R.drawable.circle_green);
                waveView.startRippleAnimation();
                tv_status.setText("正常");
            }else {
                rl_bg.setBackgroundResource(R.drawable.circle_gray);
                tv_status.setText("离线");
            }
        }


    }
    public void stopAim(){
        if(waveView.isRippleAnimationRunning()){
            waveView.stopRippleAnimation();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAim();
    }


}
