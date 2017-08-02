package com.ickkey.dztenant.fragment.gesture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.ickkey.dztenant.ConstantValue;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.utils.ToastUtils;
import com.ickkey.dztenant.utils.cache.ACache;
import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/8/1.
 */

public class GestureLoginFragment extends BaseFragment {
    @BindView(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.forgetGestureBtn)
    TextView forgetGestureBtn;

    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;
    private int inType=ConstantValue.GESTURE_HANDLE_LOGIN_IN;

    @Override
    public int getLayoutId() {
        return R.layout.fm_gesture_login;
    }

    @Override
    public void initView() {
        inType=getArguments().getInt(ConstantValue.GESTURE_PAGER_TYPE,ConstantValue.GESTURE_HANDLE_LOGIN_IN);
        if(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN){
            btn_left_base.setVisibility(View.INVISIBLE);
        }
        setTitle("验证手势密码");
        aCache= RenterApp.getInstance().getCache();
        gesturePassword = aCache.getAsBinary(ConstantValue.GESTURE_PASSWORD);

        forgetGestureBtn.setVisibility(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN? View.VISIBLE:View.INVISIBLE);
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }
    @Override
    public boolean onBackPressedSupport() {
        if(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN){
            showToast("请完成手势密码设置");
            return  true;
        }else {
            return super.onBackPressedSupport();
        }

    }
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        ToastUtils.showShortToast(_mActivity,"验证成功");
        if(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN){
            startWithPop(HomeFragment.newInstance(HomeFragment.class));
        }else {
            Bundle bundle =new Bundle();
            bundle.putInt(ConstantValue.GESTURE_PAGER_TYPE,ConstantValue.GESTURE_HANDLE_UPDATE);
            startWithPop(CreateGestureFragment.newInstance(CreateGestureFragment.class,bundle));
        }
    }

    /**
     * 忘记手势密码（去账号登录界面）
     */
    @OnClick(R.id.forgetGestureBtn)
    void forgetGesturePasswrod() {
        start(LoginPwdCheckFragment.newInstance(LoginPwdCheckFragment.class));
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.global_yellow),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.gesture_error),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.global_yellow);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }


}
