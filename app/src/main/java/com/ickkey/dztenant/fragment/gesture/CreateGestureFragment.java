package com.ickkey.dztenant.fragment.gesture;

import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ickkey.dztenant.ConstantValue;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.home.HomeFragment;
import com.ickkey.dztenant.utils.ToastUtils;
import com.ickkey.dztenant.utils.cache.ACache;
import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternIndicator;
import com.star.lockpattern.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hhj on 2017/8/1.
 */

public class CreateGestureFragment extends BaseFragment {
    @BindView(R.id.lockPatterIndicator)
    LockPatternIndicator lockPatternIndicator;
    @BindView(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @BindView(R.id.messageTv)
    TextView messageTv;

    private List<LockPatternView.Cell> mChosenPattern = null;
    private ACache aCache;
    private static final long DELAYTIME = 600L;
    private int inType=ConstantValue.GESTURE_HANDLE_LOGIN_IN;
    @Override
    public int getLayoutId() {
        return R.layout.fm_create_gesture;
    }

    @Override
    public void initView() {
        if(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN){
            btn_left_base.setVisibility(View.INVISIBLE);
        }
        setTitle("设置手势密码").setOnBtnLeftClickListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN){

                }else {
                    pop();
                }
            }
        });

        aCache= RenterApp.getInstance().getCache();
        lockPatternView.setOnPatternListener(patternListener);
        inType=getArguments().getInt(ConstantValue.GESTURE_PAGER_TYPE,ConstantValue.GESTURE_HANDLE_LOGIN_IN);


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
    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            //updateStatus(Status.DEFAULT, null);
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            //Log.e(TAG, "--onPatternDetected--");
            if(mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lockPatternIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 重新设置手势
     */
    @OnClick(R.id.resetBtn)
    void resetLockPattern() {
        mChosenPattern = null;
        lockPatternIndicator.setDefaultIndicator();
        updateStatus(Status.DEFAULT, null);
        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {

        ToastUtils.showShortToast(_mActivity,"设置成功");
        if(inType==ConstantValue.GESTURE_HANDLE_LOGIN_IN){
            startWithPop(HomeFragment.newInstance(HomeFragment.class));
        }else {
            pop();
        }

    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
        aCache.put(ConstantValue.GESTURE_PASSWORD, bytes);
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.global_yellow),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.global_yellow),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.gesture_error),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.gesture_error),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.global_yellow);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
