package com.ickkey.dztenant.fragment.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ickkey.dztenant.BuildConfig;
import com.ickkey.dztenant.ConstantValues;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.gesture.CreateGestureFragment;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.net.CommonResponseListener;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.Urls;
import com.ickkey.dztenant.net.request.GetAppVersionReq;
import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.GetAppVersionResp;
import com.ickkey.dztenant.net.response.GetLocksResp;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.service.DownloadApkService;
import com.ickkey.dztenant.utils.DialogUtils;

import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by hhj on 2017/8/1.
 */

public class LaunchFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fm_launch;
    }

    @Override
    public void initView() {
        setFragmentAnimator(new FragmentAnimator(R.anim.anim_fade_in,R.anim.anim_fade_out,R.anim.anim_fade_in,R.anim.anim_fade_out));
        setTitleGone();
        NetEngine.getInstance().getHttpResult(new CommonResponseListener<GetAppVersionResp>(){
            @Override
            public void onSucceed(final GetAppVersionResp getAppVersionResp) {
                super.onSucceed(getAppVersionResp);
                if(!getAppVersionResp.msg.androidVersion.equals(BuildConfig.VERSION_NAME)){
                        showUpdateDialog(getAppVersionResp);
                }else {
                    toLogin();
                }


            }
        }, Urls.GET_APP_VERSION,GetAppVersionResp.class,_mActivity,new GetAppVersionReq());




    }
    public void showUpdateDialog(final GetAppVersionResp getAppVersionResp){
        AlertDialog dialog=  DialogUtils.showDialog(_mActivity, R.layout.dialog_version, false, new DialogUtils.CustomizeAction() {
            @Override
            public void setCustomizeAction(final AlertDialog dialog, View view) {
                TextView tv_title= (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText("新版本"+getAppVersionResp.msg.androidVersion);
                ListView info_list= (ListView) view.findViewById(R.id.info_list);
                info_list.setAdapter(new UpdateAdapter(getAppVersionResp.msg.androidNotes));

                TextView btn_cancel= (TextView) view.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        toLogin();
                    }
                });
                TextView btn_update= (TextView) view.findViewById(R.id.btn_update);
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent=new Intent();
                        intent.setClass(_mActivity, DownloadApkService.class);
                        intent.putExtra("url",getAppVersionResp.msg.androidUrl);
                        _mActivity.startService(intent);

                    }
                });


            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    return true;

                return false;
            }
        });
    }
    public void  toLogin(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(TextUtils.isEmpty(RenterApp.getInstance().getPwd())){
                    startWithPop(LoginFragment.newInstance(LoginFragment.class));
                }else {
                    LoginReq loginReq=new LoginReq();
                    loginReq.mobile=RenterApp.getInstance().getUserInfo().username;
                    loginReq.password=RenterApp.getInstance().getPwd();
                    NetEngine.getInstance().sendLoginRequest(_mActivity,new CommonResponseListener<LoginResponse>(){
                        @Override
                        public void onSucceed(LoginResponse loginResponse) {
                            super.onSucceed(loginResponse);
                            RenterApp.getInstance().saveUserInfo(loginResponse);
                            goOn();

                        }
                    },fragment_tag,loginReq);

                }
            }
        },500);
    }

    public void goOn(){
        Bundle bundle=new Bundle();
        bundle.putInt(ConstantValues.GESTURE_PAGER_TYPE, ConstantValues.GESTURE_HANDLE_LOGIN_IN);
        if(RenterApp.getInstance().getCache().getAsBinary(ConstantValues.GESTURE_PASSWORD+RenterApp.getInstance().getUserInfo().userId)!=null){
            startWithPop(GestureLoginFragment.newInstance(GestureLoginFragment.class,bundle));
        }else {
            startWithPop(CreateGestureFragment.newInstance(CreateGestureFragment.class,bundle));
        }
    }
    private class UpdateAdapter extends BaseAdapter{
        List<String> infos;
        public UpdateAdapter(List<String> infos){
            this.infos=infos;
        }

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int i) {
            return infos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder  viewHolder;
            if(view==null){
                view=View.inflate(_mActivity,R.layout.item_update,null);
                viewHolder=new ViewHolder();
                viewHolder.tv= (TextView) view.findViewById(R.id.tv);
                view.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.tv.setText((i+1)+"、"+infos.get(i));
            return view;
        }
        private class ViewHolder{
            public TextView tv;
        }

    }
}
