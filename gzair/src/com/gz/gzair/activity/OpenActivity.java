
package com.gz.gzair.activity;

import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OpenActivity extends BaseActivity implements OnClickListener {

    public Button btn_open;
    private String appMac = "";
    private TextView location_pm;
    private TextView pm;
    private SharedPreferences storeSP;
    private String wlsn ;
    private String sb;//改用户下的所有设备列表
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EairApplaction.getInstance().addActivity(this);
        setContentView(R.layout.local_weath_layout);
        storeSP = this.getSharedPreferences(Constant.STOREDB, 0);
        appMac = storeSP.getString(Constant.PHONEMAC, "");
        btn_open = (Button) this.findViewById(R.id.btn_open);
        Bundle bundle=this.getIntent().getExtras();
        wlsn=bundle.getString("wlsn");
        sb=bundle.getString("sb");
        userId=bundle.getString("userId");
        TextView location_text = (TextView) this.findViewById(R.id.location_text);
        location_pm = (TextView) this.findViewById(R.id.location_pm);
        pm = (TextView) this.findViewById(R.id.pm);
        pm.setText(this.getResources().getString(R.string.aqi));
        btn_open.setOnClickListener(this);
        if (EairApplaction.status) {
            location_text.setText(EairApplaction.todayWeather);
            location_pm.setText(EairApplaction.aqi);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btn_open) {
            String str = "wlsn=" + wlsn + "&mac=" + appMac + "&set&poweron=0&userid="+userId+"&cmd";
            String result = getRepMessage(str);
            if ("timeout".equals(result) || result == null) {// 开机失败
                tips();
            } else {// 开机成功
                    // 获取设备的初始化状态
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                str = "wlsn=" + wlsn + "&mac=" + appMac + "&get&init&userid="+userId+"&cmd";
                String mess = getRepMessage(str);
                if ("timeout".equals(mess) || mess == null) {// 没有获取到
                    tips();
                } else {
                    storeData(mess);
                    // 到运行页面去
                    Intent intent = new Intent(OpenActivity.this, DeviceHomePageActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("wlsn", wlsn);
                    bundle.putString("sb", sb);
                    bundle.putString("userId", userId);
                    intent.putExtras(bundle);
                    OpenActivity.this.finish();
                    OpenActivity.this.startActivity(intent);
                }
            }

        }

    }

    /***
     * 处理返回的init设备的信息
     * 
     * @param message
     */
    private void storeData(String message) {
        Editor editor = storeSP.edit();
        String[] mess = message.split("&");
        if(mess.length==10){
            editor.putInt(Constant.TIME, Integer.parseInt(mess[1].split("=")[1]));
            editor.putInt(Constant.WINDDW, Integer.parseInt(mess[2].split("=")[1]));
            editor.putInt(Constant.PM, Integer.parseInt(mess[3].split("=")[1]));
            editor.putString(Constant.AIR, mess[4].split("=")[1]);
            editor.putInt(Constant.MODE, Integer.parseInt(mess[5].split("=")[1]));
            editor.putInt(Constant.UV, Integer.parseInt(mess[6].split("=")[1]));
            editor.putInt(Constant.ANION, Integer.parseInt(mess[7].split("=")[1]));
            editor.putInt(Constant.HEPA, Integer.parseInt(mess[8].split("=")[1]));
            editor.commit();
        }
    }

}
