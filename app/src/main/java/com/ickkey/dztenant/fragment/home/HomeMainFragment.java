package com.ickkey.dztenant.fragment.home;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.net.NetEngine;
import com.ickkey.dztenant.net.OnResponseListener;
import com.ickkey.dztenant.net.Urls;
import com.ickkey.dztenant.net.request.BaseRequest;
import com.ickkey.dztenant.net.response.GetLocksResp;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.view.pwdkeyboard.PopEnterPassword;
import com.ickkey.dztenant.view.ScaleCircleNavigator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

    FragmentPagerAdapter  fragmentPagerAdapter;
    HomeFragment homeFragment;
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
        fragmentPagerAdapter=new PagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(fragmentPagerAdapter);
        initMagicIndicator();
        getLocks();
    }
    public  void  getLocks(){


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
        new Thread(){
            @Override
            public void run() {
                String url= Urls.GET_LOCKS+"?userId="+RenterApp.getInstance().getUserInfo().userId+"&token="+RenterApp.getInstance().getUserInfo().token;
                String result=postDownloadJson(url,null);
                LogUtil.info(getClass(),"result--"+result);
            }
        }.start();
    }
    public static String postDownloadJson(String path,String post){
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            if(post!=null){
                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                // 发送请求参数
                printWriter.write(post);//post的参数 xx=xx&yy=yy
                // flush输出流的缓冲
                printWriter.flush();
            }

            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }
    @OnClick({R.id.ll_custom_pwd})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_custom_pwd:
                PopEnterPassword popEnterPassword = new PopEnterPassword(_mActivity);
                popEnterPassword.showAtLocation(rootView,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return HomeMainRoomLockFragment.newInstance(HomeMainRoomLockFragment.class);
            }else {
                return HomeMainRoomLockFragment.newInstance(HomeMainRoomLockFragment.class);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
