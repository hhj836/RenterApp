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
import com.ickkey.dztenant.event.TransactionEvent;
import com.ickkey.dztenant.fragment.gesture.CreateGestureFragment;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.fragment.gesture.LoginPwdCheckFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.fragment.home.HomeMainFragment;
import com.ickkey.dztenant.fragment.login.LoginFragment;
import com.ickkey.dztenant.net.HttpRequestUtils;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    TransactionEvent mTransactionEvent;
    @Subscribe(sticky = true,threadMode= ThreadMode.MAIN) // sticky事件可以保证即使Activity被强杀，也会在恢复后拿到数据
    public void onEvent(TransactionEvent event) {
        if(!event.tag.equals(fragment_tag)){
            return;
        }

        handleTransactionEvent(event);
    }
    private void handleTransactionEvent(TransactionEvent event){
        if (FragmentationHack.isStateSaved(getFragmentManager())) {
            mTransactionEvent = event;
        } else {
            if(event.eventType==TransactionEvent.TYPE_POP){
                pop();
            }else {
                if(event.toFragment!=null){
                    super.startWithPop(event.toFragment);
                }
            }

        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(BaseFragment.this);
        View view=inflater.inflate(R.layout.fm_base,null);
        btn_left_base= (RippleView) view.findViewById(R.id.btn_left_base);
        btn_left_base.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                TransactionEvent event=new TransactionEvent(fragment_tag);
                EventBus.getDefault().post(event);

            }

        });
        btn_right_base= view.findViewById(R.id.btn_right_base);
        btn_right_base.setVisibility(View.INVISIBLE);
        tv_left_base=  view.findViewById(R.id.tv_left_base);
        tv_right_base=  view.findViewById(R.id.tv_right_base);
        rl_title_base=  view.findViewById(R.id.rl_title_base);
        rl_title_content_base=  view.findViewById(R.id.rl_title_content_base);
        fm_content_base=  view.findViewById(R.id.fm_content_base);
        if(getTitleContentResId()!=0){
            View titleContent=getActivity().getLayoutInflater().inflate(getTitleContentResId(),null);
            rl_title_content_base.removeAllViews();
            rl_title_content_base.addView(titleContent);
        }else {
            tv_title_base= view.findViewById(R.id.tv_title_base);
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
                //解决ui闪烁问题
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
        if(mTransactionEvent!=null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleTransactionEvent(mTransactionEvent);
                    mTransactionEvent=null;
                }
            },300);

        }
    }

    @Override
    public void startWithPop(ISupportFragment toFragment) {
        TransactionEvent event=new TransactionEvent(fragment_tag);
        event.toFragment=toFragment;
        event.eventType=TransactionEvent.TYPE_START_AND_POP;
        EventBus.getDefault().post(event);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(BaseFragment.this);
        HttpRequestUtils.getInstance().getRequestQueue().cancelAll(fragment_tag);
        LogUtil.info(getClass(),"onDestroyView");
        super.onDestroyView();
        RenterApp.getInstance().removeFragment(BaseFragment.this);

    }

}
