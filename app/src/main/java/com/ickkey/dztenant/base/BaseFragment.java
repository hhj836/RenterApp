package com.ickkey.dztenant.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentationHack;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.fragment.gesture.CreateGestureFragment;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.fragment.gesture.LoginPwdCheckFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.fragment.home.HomeMainFragment;
import com.ickkey.dztenant.fragment.login.LoginFragment;
import com.ickkey.dztenant.net.HttpRequestUtils;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by hhj on 2017/7/27.
 *
 */

/**     //重复fragment解决办法
 *   if(findFragment(RegisterFragment.class)!=null){
     RegisterFragment fragment=findFragment(RegisterFragment.class);
     fragment.getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
     }
 */
public abstract class BaseFragment extends BaseBackFragment {
    public Handler handler= RenterApp.getInstance().getMainThreadHandler();
    public final String fragment_tag=getClass().getSimpleName()+ UUID.randomUUID();
    private boolean isPop;



    public static BaseFragment newInstance(Class<? extends  BaseFragment> clazz, Bundle...args) {

        BaseFragment fragment = null;
        try {
            fragment = clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(args.length>0){
            fragment.setArguments(args[0]);
        }
        return fragment;
    }
    public abstract int getLayoutId();
    public abstract  void initView();
    public int getTitleContentResId(){
        return  0;
    }
    public  boolean  isShowTitle(){
        return  true;
    }
    public RippleView btn_left_base;
    public RippleView btn_right_base;
    public TextView tv_title_base;
    TextView tv_left_base;
    TextView   tv_right_base;
    RelativeLayout rl_title_content_base;
    RelativeLayout rl_title_base;
    FrameLayout fm_content_base;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fm_base,null);
        btn_left_base= (RippleView) view.findViewById(R.id.btn_left_base);
        btn_left_base.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
               pop();
            }

        });
        btn_right_base= (RippleView) view.findViewById(R.id.btn_right_base);
        btn_right_base.setVisibility(View.INVISIBLE);
        tv_left_base= (TextView) view.findViewById(R.id.tv_left_base);
        tv_right_base= (TextView) view.findViewById(R.id.tv_right_base);
        rl_title_base= (RelativeLayout) view.findViewById(R.id.rl_title_base);
        rl_title_content_base= (RelativeLayout) view.findViewById(R.id.rl_title_content_base);
        fm_content_base= (FrameLayout) view.findViewById(R.id.fm_content_base);
        if(getTitleContentResId()!=0){
            View titleContent=getActivity().getLayoutInflater().inflate(getTitleContentResId(),null);
            rl_title_content_base.removeAllViews();
            rl_title_content_base.addView(titleContent);
        }else {
            tv_title_base= (TextView) view.findViewById(R.id.tv_title_base);
        }
        if(getLayoutId()!=0){
            View content=getActivity().getLayoutInflater().inflate(getLayoutId(),null);
            fm_content_base.removeAllViews();
            fm_content_base.addView(content);
        }
        ButterKnife.bind(this,view);
        if(isShowTitle()){
            setTitleVisible();
        }else {
            setTitleGone();
        }
        btn_right_base.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBtnRightClick();
                    }
                },getResources().getInteger(R.integer.rippleDuration));
            }
        });
        initView();
        RenterApp.getInstance().addFragment(BaseFragment.this);
        return attachToSwipeBack(view);


    }
    public BaseFragment setBtnRightText(String s){
        btn_right_base.setVisibility(View.VISIBLE);
        tv_right_base.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_right_base.setText(s);
        return  BaseFragment.this;


    }

    /**
     * 右按钮事件，防止闪烁
     */
    public  void  onBtnRightClick(){

    }
    public BaseFragment setBtnRightImgRes(int res){
        btn_right_base.setVisibility(View.VISIBLE);
        tv_right_base.setBackgroundResource(res);
        tv_right_base.setText("");
        return  BaseFragment.this;
    }
    public BaseFragment setBtnLeftText(String s){
        btn_left_base.setVisibility(View.VISIBLE);
        tv_left_base.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_left_base.setText(s);
        return  BaseFragment.this;

    }
    public BaseFragment setBtnLeftImgRes(int res){
        btn_left_base.setVisibility(View.VISIBLE);
        tv_left_base.setBackgroundResource(res);
        tv_left_base.setText("");
        return  BaseFragment.this;

    }
    public BaseFragment setOnBtnLeftClickListener(RippleView.OnRippleCompleteListener onRippleCompleteListener){
        btn_left_base.setVisibility(View.VISIBLE);
        btn_left_base.setOnRippleCompleteListener(onRippleCompleteListener);
        return  BaseFragment.this;

    }
    @Deprecated
    public BaseFragment setOnBtnRightClickListener(RippleView.OnRippleCompleteListener onRippleCompleteListener){
        btn_right_base.setVisibility(View.VISIBLE);
        btn_right_base.setOnRippleCompleteListener(onRippleCompleteListener);
        return  BaseFragment.this;

    }
    public BaseFragment setTitle(String s){
        tv_title_base.setText(s);
        return  BaseFragment.this;

    }
    public void setTitleGone(){
        rl_title_base.setVisibility(View.GONE);
    }
    public void setTitleVisible(){
        rl_title_base.setVisibility(View.VISIBLE);
    }
    public void showToast(String s){
        ToastUtils.showShortToast(_mActivity,s);
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
        HttpRequestUtils.getInstance().getRequestQueue().cancelAll(fragment_tag);
        LogUtil.info(getClass(),"onDestroyView");
        isPop=true;
        super.onDestroyView();
        RenterApp.getInstance().removeFragment(BaseFragment.this);

    }

    @Override
    public void pop() {
        isPop=true;
        super.pop();


    }
    @Override
    public void startWithPop(final ISupportFragment toFragment) {
        if(FragmentationHack.isStateSaved(getFragmentManager())){
            new Thread(){
                @Override
                public void run() {
                    while (FragmentationHack.isStateSaved(getFragmentManager())&&!isPop){
                        try {
                            Thread.sleep(500);
                            LogUtil.info(getClass(),"startWithPop---sleep---");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!isPop){
                                BaseFragment.this.getSupportDelegate().startWithPop(toFragment);
                            }

                        }
                    });
                }
            }.start();

        }else {
            super.startWithPop(toFragment);
        }


    }
}
