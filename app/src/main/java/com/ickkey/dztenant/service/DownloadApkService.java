package com.ickkey.dztenant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;


import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.utils.PathUtil;
import com.ickkey.dztenant.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by 侯慧杰
 * on 2016/5/7.
 */
public class DownloadApkService extends Service {
    private NotificationManager nm;
    private Notification notification;
    private int download_precent=0;
    private RemoteViews views;
    private int notificationId=1234;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载保存路径 */
    private String mSavePath;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private String urlPath;
    private int prePrecent;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    views.setTextViewText(R.id.tvProcess,"已下载"+download_precent+"%");
                    views.setProgressBar(R.id.pbDownload,100,download_precent,false);
                    notification.contentView=views;
                    nm.notify(notificationId, notification);
                    break;
                case DOWNLOAD_FINISH:
                    download_precent=0;
                    nm.cancel(notificationId);
                    // 安装文件
                    installApk();
                    stopSelf();
                    break;
                default:
                    break;
            }
        };
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent,int startId){
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        ToastUtils.showLongToast(RenterApp.getInstance(),"正在下载,请在消息栏查看进度");
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification=new Notification();
        notification.icon=android.R.drawable.stat_sys_download;
        notification.tickerText=getString(R.string.app_name)+"更新";
        notification.when= System.currentTimeMillis();
        notification.defaults= Notification.DEFAULT_LIGHTS;
        //设置任务栏中下载进程显示的views
        views=new RemoteViews(getPackageName(), R.layout.download_apk);
        notification.contentView=views;
        //将下载任务添加到任务栏中
        nm.notify(notificationId,notification);
        if(intent!=null)
        urlPath=intent.getStringExtra("url");
        //启动线程开始执行下载任务
        downFile();
        return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



    //下载更新文件
    private void downFile() {
        new Thread() {
            public void run(){
                try
                {
                    // 判断SD卡是否存在，并且是否具有读写权限
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
                        // 获得存储卡的路径
                        String sdpath = Environment.getExternalStorageDirectory() + "/";
                        mSavePath = sdpath + "download";
                        URL url = new URL(urlPath);
                        // 创建连接
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        // 获取文件大小
                        int length = conn.getContentLength();
                        // 创建输入流
                        InputStream is = conn.getInputStream();

                        File file = new File(mSavePath);
                        // 判断文件目录是否存在
                        if (!file.exists())
                        {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, PathUtil.getFileName(urlPath));
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        int count = 0;
                        // 缓存
                        byte buf[] = new byte[1024];
                        // 写入到文件中
                        do
                        {
                            int numread = is.read(buf);
                            count += numread;
                            // 计算进度条位置
                            download_precent = (int) (((float) count / length) * 100);
                            // 更新进度
                            if(download_precent-prePrecent>5){
                                mHandler.sendEmptyMessage(DOWNLOAD);
                                prePrecent=download_precent;
                            }

                            if (numread <= 0)
                            {
                                // 下载完成
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                            // 写入文件
                            fos.write(buf, 0, numread);
                        } while (!cancelUpdate);// 点击取消就停止下载.
                        fos.close();
                        is.close();
                    }
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }




            }
        }.start();
    }



    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, PathUtil.getFileName(urlPath));
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(i);
    }





}
