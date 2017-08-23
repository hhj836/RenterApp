package com.ickkey.dztenant.fragment.home;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.Urls;
import com.ickkey.dztenant.net.request.BaseRequest;
import com.ickkey.dztenant.net.request.GetLocksPwdReq;
import com.ickkey.dztenant.net.response.GetLocksPwdResp;
import com.ickkey.dztenant.net.response.GetLocksResp;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.Json2ObjHelper;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.ToastUtils;
import com.ickkey.dztenant.view.ScaleCircleNavigator;
import com.ickkey.dztenant.view.pwdkeyboard.PopEnterPassword;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/7/27.
 */

public class HomeMainFragment extends BaseFragment {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.rootView)
    RelativeLayout  rootView;
    @BindView(R.id.lock_desc)
    TextView   lock_desc;
    @BindView(R.id.ll_custom_pwd)
    LinearLayout ll_custom_pwd;

    FragmentPagerAdapter  fragmentPagerAdapter;
    HomeFragment homeFragment;
     GetLocksResp getLocksResp;
    public List<GetLocksResp.LockItem> defaultLocks=new ArrayList<>();

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fm_home_main;
    }


    @Override
    public void initView() {
        setTitle(getString(R.string.text_main_pager));
        btn_left_base.setVisibility(View.INVISIBLE);
        if(RenterApp.getInstance().getUserInfo().isVisitor==0){
            for(int i=0;i<2;i++){
                GetLocksResp  getLocksResp=new GetLocksResp();
                GetLocksResp.LockItem item=getLocksResp.new LockItem();
                item.isOnlie=1;
                item.installAddress="虚拟门锁0"+(i+1);
                item.quantity=100;
                defaultLocks.add(item);
            }

            fragmentPagerAdapter=new PagerAdapter(getChildFragmentManager(),defaultLocks);
            lock_desc.setText(defaultLocks.get(0).installAddress);
            mViewPager.setOffscreenPageLimit(defaultLocks.size());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    lock_desc.setText(defaultLocks.get(position).installAddress);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mViewPager.setAdapter(fragmentPagerAdapter);
            initMagicIndicator();
            ll_custom_pwd.setClickable(true);

        }else {
            getLocks();
        }

    }
    public  void  getLocks(){
        DialogUtils.showProgressDialog(_mActivity);
        ll_custom_pwd.setClickable(false);

       /* BaseRequest request=new BaseRequest();
        request.userId= RenterApp.getInstance().getUserInfo().userId;
        request.token=RenterApp.getInstance().getUserInfo().token;
        NetEngine.getInstance().sendGetLocksRequest(_mActivity, new OnResponseListener<GetLocksResp>() {
            @Override
            public void onSucceed(GetLocksResp getLocksResp) {

            }

            @Override
            public void onError(String errorMsg) {

            }
        },fragment_tag,request);*/
        BaseRequest request=new BaseRequest();
        request.userId= RenterApp.getInstance().getUserInfo().userId;
        request.token=RenterApp.getInstance().getUserInfo().token;
        NetEngine.getInstance().getHttpResult(new CommonResponseListener() {
            @Override
            public void onSucceed(Object obj) {
                getLocksResp= (GetLocksResp) obj;
                if(getLocksResp.msg!=null&&getLocksResp.msg.size()>0){
                    fragmentPagerAdapter=new PagerAdapter(getChildFragmentManager(),getLocksResp.msg);
                    lock_desc.setText(getLocksResp.msg.get(0).installAddress);
                    mViewPager.setOffscreenPageLimit(getLocksResp.msg.size());
                    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            lock_desc.setText(getLocksResp.msg.get(position).houseNumber);

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    mViewPager.setAdapter(fragmentPagerAdapter);
                    initMagicIndicator();
                    ll_custom_pwd.setClickable(true);

                }

            }
        }, Urls.GET_LOCKS,GetLocksResp.class,_mActivity,request);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }
    private  void setCustomPwd(){
        if(getLocksResp!=null){
            GetLocksPwdReq getLocksPwdReq=new GetLocksPwdReq();
            getLocksPwdReq.token=RenterApp.getInstance().getUserInfo().token;
            getLocksPwdReq.locksId=getLocksResp.msg.get(mViewPager.getCurrentItem()).id;
            getLocksPwdReq.userId=RenterApp.getInstance().getUserInfo().userId;
            NetEngine.getInstance().getHttpResult(new CommonResponseListener(){
                @Override
                public void onSucceed(Object object) {
                    super.onSucceed(object);
                    GetLocksPwdResp getLocksPwdResp= (GetLocksPwdResp) object;
                    PopEnterPassword popEnterPassword = new PopEnterPassword(_mActivity, getLocksPwdResp.msg.pwdId, new PopEnterPassword.onUpdateSucceedListener() {
                        @Override
                        public void onSucceed() {
                            showToast("设置成功");

                        }
                    });
                    popEnterPassword.showAtLocation(rootView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            },Urls.GET_LOCK_PWD,GetLocksPwdResp.class,_mActivity,getLocksPwdReq);


        }
    }
    @OnClick({R.id.ll_custom_pwd})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_custom_pwd:
                if(RenterApp.getInstance().getUserInfo().isVisitor==0){
                    showToast("你未被授权门锁，无法自定义密码");
                    return;
                }
                DialogUtils.showDialog(_mActivity, R.layout.dialog_uplockspwd_succeed, true, new DialogUtils.CustomizeAction() {
                    @Override
                    public void setCustomizeAction(final AlertDialog dialog, View view) {
                        TextView btn_confirm= (TextView) view.findViewById(R.id.btn_confirm);
                        TextView btn_cancel= (TextView) view.findViewById(R.id.btn_cancel);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                setCustomPwd();

                            }
                        });
                    }
                });

                 /* GetLocksPwdReq getLocksPwdReq=new GetLocksPwdReq();
                    getLocksPwdReq.token=RenterApp.getInstance().getUserInfo().token;
                    getLocksPwdReq.locksId=resp.msg.get(mViewPager.getCurrentItem()).id;
                    getLocksPwdReq.userId=RenterApp.getInstance().getUserInfo().userId;
                    NetEngine.getInstance().sendGetLocksPwdRequest(_mActivity,new CommonResponseListener<GetLocksPwdResp>(){
                        @Override
                        public void onSucceed(GetLocksPwdResp getLocksPwdResp) {
                            super.onSucceed(getLocksPwdResp);
                            PopEnterPassword popEnterPassword = new PopEnterPassword(_mActivity, getLocksPwdResp.pwdId, new PopEnterPassword.onUpdateSucceedListener() {
                                @Override
                                public void onSucceed() {
                                    DialogUtils.showDialog(_mActivity, R.layout.dialog_uplockspwd_succeed, true, new DialogUtils.CustomizeAction() {
                                        @Override
                                        public void setCustomizeAction(final AlertDialog dialog, View view) {
                                                TextView btn_confirm= (TextView) view.findViewById(R.id.btn_confirm);
                                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            popEnterPassword.showAtLocation(rootView,
                                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


                        }
                    },fragment_tag,getLocksPwdReq);*/
                break;
        }
    }

    private void initMagicIndicator() {
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(_mActivity);
        scaleCircleNavigator.setCircleCount(fragmentPagerAdapter.getCount());
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(Color.DKGRAY);
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
    public class PagerAdapter extends FragmentPagerAdapter{
        List<GetLocksResp.LockItem> locks;

        public PagerAdapter(FragmentManager fm,List<GetLocksResp.LockItem> locks) {
            super(fm);
            this.locks=locks;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle=new Bundle();
            bundle.putSerializable("LockItem",locks.get(position));
            HomeMainRoomLockFragment lockFragment= (HomeMainRoomLockFragment) HomeMainRoomLockFragment.newInstance(HomeMainRoomLockFragment.class,bundle);
            lockFragment.setHomeMainFragment(HomeMainFragment.this);
            return  lockFragment;
        }

        @Override
        public int getCount() {
            return locks.size();
        }
    }
}
