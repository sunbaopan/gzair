
package com.gz.gzair.activity;

import java.util.List;
import android.app.AlertDialog;
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
import android.util.Log;
import android.widget.Toast;
import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import com.gz.gzair.activity.DownloadService.DownloadBinder;
import com.gz.gzair.util.Util;
import com.gz.gzair.util.WebServiceUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
public class LoadActivity extends BaseActivity  {

    private SharedPreferences storeSP;

    private String mac = "";
 
    private EairApplaction app;
    
    private boolean isDestroy = true;
    
    private DownloadBinder binder;
    
    private boolean isBinded;
    
    private String apkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        //EairApplaction.getInstance().addActivity(this);
        app = (EairApplaction) getApplication();
        storeSP = this.getSharedPreferences(Constant.STOREDB, 0);
        mac = storeSP.getString(Constant.PHONEMAC, "");
        //�������͵Ĺر�debugģʽ
        XGPushConfig.enableDebug(this,false);
        // ע�����ͽӿ�
        XGPushManager.registerPush(getApplicationContext());
        PackageInfo info;
        // ��������Ƿ�������
        boolean isExistNet = Util.isNetworkConnected(this);
        if (!isExistNet) {// û������
            loadTips();
            return;
        }

        try {
            PackageManager manager = LoadActivity.this.getPackageManager();
            info = manager.getPackageInfo(LoadActivity.this.getPackageName(), 0);
            String version = info.versionName;
            WebServiceUtil wsu = new WebServiceUtil();
            String repMessage = wsu.getVersion(version);
            if (repMessage == null) {// ���Է���2��
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
                    && !("timeout".equals(repMessage))) {// ��Ҫ�����ģ������ͣ����ҳ�ȴ��������������Ժ���ܽ�����һ����
                // ���������汾�Ƿ���Ҫ����
                String mess[] = repMessage.split("&");
                apkUrl=mess[1];
                showNoticeDialog();
                return;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.coums();
    }
    
    
    private void showNoticeDialog(){  
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��⵽�°汾");
        builder.setMessage("�Ƿ����ظ���?");
        builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(LoadActivity.this, DownloadService.class);
                it.putExtra("apkUrl", apkUrl);
                startService(it);
                bindService(it, conn, Context.BIND_AUTO_CREATE);
            }
        }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                    coums();
            }
        });
        builder.show();
    }  
    
   
    public  void  coums (){
        if ("".equals(mac)) {// ����ֻ�����û��mac��ʾ�û���wifi�ķ�ʽ����������ȡmac��ַ
            boolean bool = Util.isWifi(this);
            if (bool) {// ��wifi����
                mac = Util.getLocalMacAddressFromWifiInfo(this);
                Editor editor = storeSP.edit();
                editor.putString(Constant.PHONEMAC, mac);
                editor.commit();// ���ֻ���mac��ַ����
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

   

    private class TimeThread implements Runnable {
        @Override
        public void run() {
            // hQhandle();
            // pdhandle();
            // ��ת����½����
            Intent intent = new Intent(LoadActivity.this, LoginActivity.class);
            LoadActivity.this.finish();
            LoadActivity.this.startActivity(intent);

        }
    }
    
    
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadBinder) service;
            System.out.println("��������!!!");
            // ��ʼ����
            isBinded = true;
            binder.start();

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println(" notification  onresume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
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
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println(" notification  onPause");
    }

    @Override
    protected void onStop() {
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
