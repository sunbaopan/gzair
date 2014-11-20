
package com.gz.gzair.activity;

import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class StrainerActivity extends BaseActivity {

    private String userId;

    private String wlsn;

    private String mac;

    private String intranetAdress;

    private TextView sytime;

    private TextView totaltime;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EairApplaction.getInstance().addActivity(this);
        setContentView(R.layout.strainer_layout);
        Bundle bundle = this.getIntent().getExtras();
        userId = (String) bundle.get("userId");
        wlsn = (String) bundle.get("wlsn");
        mac = (String) bundle.get("mac");
        intranetAdress = (String) bundle.get("intranetAdress");
        totaltime = (TextView) this.findViewById(R.id.totaltime);
        sytime = (TextView) this.findViewById(R.id.sytime);
        pd = ProgressDialog.show(StrainerActivity.this, null,
                this.getResources().getString(R.string.loadData), true, false);
        thread.start();
    }
    Thread thread = new Thread() {
        @Override
        public void run() {
            String mess;
            Message message = new Message();
            if ("1".equals(intranetAdress)) {// 需要外网
                String cmd = "wlsn=" + wlsn + "&mac=" + mac + "&set&hepa=3&userid="
                        + userId
                        + "&cmd";
                mess = getRepMessage(cmd);
            } else {// 需要内网
                String cmd = "mac=" + mac + "&set&hepa=3&userid=" + userId
                        + "&cmd";
                mess = getNetRepMessage(intranetAdress, cmd);
            }
            if ("timeout".equals(mess) || mess == null) {
                if (intranetAdress != null) {
                    intranetAdress = null;
                }
                message.arg1=2;
            } else if (mess.contains("door_open=1")) {
                message.arg1=3;
            } else if(mess.contains("ret")){
                String[] times = mess.split("&");
                if ("1".equals(intranetAdress)) {// 外网
                    message.obj=times[2]+"&"+times[3];
                } else {// 内网
                    message.obj=times[3]+"&"+times[4];
                }
                message.arg1=1;
            }else{
                message.arg1=4;
            }
            handle.sendMessage(message);
        }

    };

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pd.dismiss();
            if(msg.arg1==1){
                String obj=msg.obj.toString();
                String[] times=obj.split("&");
                totaltime.setText(times[0]+" 小时");// 总时间
                sytime.setText(times[1]+" 小时");// 使用时间
            }else if(msg.arg1==2){
                tips();
            }else if(msg.arg1==3){
                openTips();
            }else if(msg.arg1==4){
                Toast.makeText(StrainerActivity.this, StrainerActivity.this.getResources().getString(R.string.datafail),
                        Toast.LENGTH_SHORT).show();
            }
            
        }

    };

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 点击返回就退出应用
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
