package com.ickkey.dztenant.view.pwdkeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.request.UpdateLockPwdReq;
import com.ickkey.dztenant.net.response.BaseResponse;
import com.ickkey.dztenant.utils.ToastUtils;


/**
 * 输入支付密码
 *
 * @author lining
 */
public class PopEnterPassword extends PopupWindow {

    private PasswordView pwdView;

    private View mMenuView;

    private Activity mContext;

    public PopEnterPassword(final Activity context, final String pwdId, final onUpdateSucceedListener onUpdateSucceedListener) {

        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.pop_enter_password, null);

        pwdView = (PasswordView) mMenuView.findViewById(R.id.pwd_view);

        //添加密码输入完成的响应
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(final String password) {

                UpdateLockPwdReq updateLockPwdReq=new UpdateLockPwdReq();
                updateLockPwdReq.token= RenterApp.getInstance().getUserInfo().token;
                updateLockPwdReq.pwd=password;
                updateLockPwdReq.pwdId=pwdId;
                NetEngine.getInstance().sendUpdateLocksPwdRequest(context,new CommonResponseListener<BaseResponse>(){
                    @Override
                    public void onSucceed(BaseResponse baseResponse) {
                        super.onSucceed(baseResponse);
                        dismiss();
                        if(onUpdateSucceedListener!=null){
                            onUpdateSucceedListener.onSucceed();
                        }

                    }
                },null,updateLockPwdReq);


            }
        });
        if(pwdView.getRootView()!=null){
            pwdView.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        // 监听键盘上方的返回
        pwdView.getVirtualKeyboardView().getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_add_anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x66000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }
    public interface  onUpdateSucceedListener{
        void onSucceed();
    }
}
