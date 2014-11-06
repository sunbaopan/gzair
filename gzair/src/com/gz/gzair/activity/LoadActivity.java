
package com.gz.gzair.activity;

import java.util.List;
import android.content.DialogInterface.OnClickListener;  
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import com.gz.gzair.activity.DownloadService.DownloadBinder;
import com.gz.gzair.db.DBManager;
import com.gz.gzair.util.Util;
import com.gz.gzair.util.WebServiceUtil;
public class LoadActivity extends BaseActivity  {

    private SharedPreferences storeSP;

    private String mac = "";

    private List<String> deviceList;
    
    private EairApplaction app;
    
    private boolean isDestroy = true;
    
    private DownloadBinder binder;
    
    private boolean isBinded;
    
    private String apkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        EairApplaction.getInstance().addActivity(this);
        app = (EairApplaction) getApplication();
        storeSP = this.getSharedPreferences(Constant.STOREDB, 0);
        mac = storeSP.getString(Constant.PHONEMAC, "");
        PackageInfo info;
        // 检查网络是否有问题
        boolean isExistNet = Util.isNetworkConnected(this);
        if (!isExistNet) {// 没有网络
            loadTips();
            return;
        }

        try {
            PackageManager manager = LoadActivity.this.getPackageManager();
            info = manager.getPackageInfo(LoadActivity.this.getPackageName(), 0);
            String version = info.versionName;
            WebServiceUtil wsu = new WebServiceUtil();
            String repMessage = wsu.getVersion(version);
            if (repMessage == null) {// 尝试访问2次
                try {
                    Thread.sleep(Constant.SLEEPTIME);
                    repMessage = wsu.getVersion(version);
                    if (repMessage == null) {
                        Thread.sleep(Constant.SLEEPTIME);
                        repMessage = wsu.getVersion(version);
                    }
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
            if (repMessage != null && !("notupd".equals(repMessage))
                    && !("timeout".equals(repMessage))) {// 需要升级的，程序就停在首页等待升级，升级过以后才能进行下一步。
                // 这里来检测版本是否需要更新
                String mess[] = repMessage.split("&");
                apkUrl=mess[1];
                showNoticeDialog();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.coums();
    }
    
    
    private void showNoticeDialog(){  
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本");
        builder.setMessage("是否下载更新?");
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(LoadActivity.this, DownloadService.class);
                it.putExtra("apkUrl", apkUrl);
                startService(it);
                bindService(it, conn, Context.BIND_AUTO_CREATE);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                    coums();
            }
        });
        builder.show();
    }  
    
   
    public  void  coums (){
        if ("".equals(mac)) {// 如果手机里面没有mac提示用户用wifi的方式联网，来获取mac地址
            boolean bool = Util.isWifi(this);
            if (bool) {// 是wifi联网
                mac = Util.getLocalMacAddressFromWifiInfo(this);
                Editor editor = storeSP.edit();
                editor.putString(Constant.PHONEMAC, mac);
                editor.commit();// 将手机的mac地址保存
                Thread tt = new Thread(new TimeThread());
                tt.start();
            } else {
                Toast.makeText(this, this.getResources().getString(R.string.notwifi),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Thread tt = new Thread(new TimeThread());
            tt.start();
        }
    }

    /***
     * 处理下面是怎么跳转
     */
    private void pdhandle() {
        // 获取手机里面存储的设备
        DBManager db = new DBManager(LoadActivity.this);
        deviceList = db.query();
        if (deviceList == null || deviceList.size() == 0) {// 没有设备,需要配置设备
            Intent intent = new Intent(this, ConfigActivity.class);
            this.finish();
            this.startActivity(intent);
        } else {// 有设备
            Intent intent = new Intent(this, DeviceListActivity.class);
            this.finish();
            this.startActivity(intent);
        }
    }

    private class TimeThread implements Runnable {
        @Override
        public void run() {
            // hQhandle();
            // pdhandle();
            // 跳转到登陆界面
            Intent intent = new Intent(LoadActivity.this, LoginActivity.class);
            LoadActivity.this.finish();
            LoadActivity.this.startActivity(intent);

        }
    }
    
    
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            binder = (DownloadBinder) service;
            System.out.println("服务启动!!!");
            // 开始下载
            isBinded = true;
            binder.start();

        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        System.out.println(" notification  onresume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (isDestroy && app.isDownload()) {
            Intent it = new Intent(this, DownloadService.class);
            startService(it);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }
        System.out.println(" notification  onNewIntent");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        System.out.println(" notification  onPause");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        isDestroy = false;
        System.out.println(" notification  onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinded) {
            System.out.println(" onDestroy   unbindservice");
            unbindService(conn);
        }
        if (binder != null && binder.isCanceled()) {
            System.out.println(" onDestroy  stopservice");
            Intent it = new Intent(this, DownloadService.class);
            stopService(it);
        }
    }
}
