
package com.gz.gzair.activity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import com.gz.gzair.util.WebServiceUtil;
import com.gz.gzair.view.BLTimeAlert;
import com.gz.gzair.view.BLWindAlert;

@SuppressLint("HandlerLeak")
public class DeviceHomePageActivity extends BaseActivity
{
    private TextView mAutoButton;
    private LinearLayout mBackGround;
    private Button mCloseButton;
    private TextView mEairRunStateText;
    private TextView mEairValueText;
    private ImageView mErrorIconView;
    private boolean mInRefreshIng = false;
    private TextView mKillButton;
    private TextView mLocationWeatherText;
    private LinearLayout mOpenLayout;
    private Animation mRotateAnimation;
    private View mRunProgress;
    private TextView mRunTimeText;
    private TextView mSilenceButton;
    private View mTimeIconBgView;
    private View mTimeIconView;
    private Animation mTimerAnimation;
    private LinearLayout mTimerLayout;
    private Animation mWindAnimation;
    private View mWindIconView;
    private LinearLayout mWindLayout;
    private TextView mWindText;
    private SharedPreferences storeSp;
    private TextView flz;// 负离子
    private TextView hepaText;// hepa
    private TextView sleep;// 睡眠
    private int enivQu = 1;

    private Time timer;
    
    private TextView mpmText;

    // 风速档位
    private int fw = 1;
    // 定时
    private int time = 1;
    // 模式
    private int mode = 1;

    private int pm = 1;

    private String air;

    private int uv = 1;

    private int anion = 1;

    private int hepa = 1;

    private String phoneMac;

    private String wlsn;

    private String sb;// 改用户下的所有设备列表

    private String userId;
    
    private  String  intranetAdress=null;//内网地址

    private void findView()
    {
        this.mRunProgress = findViewById(R.id.run_progress);
        this.mEairValueText = ((TextView) findViewById(R.id.eair_value));
        this.mEairRunStateText = ((TextView) findViewById(R.id.eair_run_state));
        this.mWindText = ((TextView) findViewById(R.id.wind));
        this.mRunTimeText = ((TextView) findViewById(R.id.run_time));
        this.mLocationWeatherText = ((TextView) findViewById(R.id.location_text));
        this.mCloseButton = ((Button) findViewById(R.id.btn_close));
        this.mErrorIconView = ((ImageView) findViewById(R.id.err_icon));
        this.mWindIconView = findViewById(R.id.wind_icon);
        this.mTimeIconBgView = findViewById(R.id.timer_icon_bg);
        this.mTimeIconView = findViewById(R.id.timer_icon);
        this.mWindLayout = ((LinearLayout) findViewById(R.id.wind_layout));
        this.mTimerLayout = ((LinearLayout) findViewById(R.id.timer_layout));
        this.mOpenLayout = ((LinearLayout) findViewById(R.id.open_layout));
        this.mBackGround = ((LinearLayout) findViewById(R.id.eair_bg));
        this.flz = (TextView) findViewById(R.id.flz);
        this.hepaText = (TextView) findViewById(R.id.hepa);
        this.sleep = (TextView) findViewById(R.id.sleep);
        this.mSilenceButton = ((TextView) findViewById(R.id.btn_silence));
        this.mAutoButton = ((TextView) findViewById(R.id.btn_auto));
        this.mKillButton = ((TextView) findViewById(R.id.btn_kill));
        this.mpmText=(TextView) findViewById(R.id.pm);
    }

    private void initEairView()
    {

        TextView localTextView2 = this.mRunTimeText;
        localTextView2.setText(getString(R.string.format_time, time));
        this.mEairRunStateText.setText(R.string.room_air);
        this.mRunProgress.setVisibility(0);
        this.mEairValueText.setBackgroundDrawable(null);
        if(pm==0){
            this.mpmText.setText("获取中....");
        }else{
            this.mpmText.setText("pm:"+pm);
        }
        if ("A".equals(air)) {
            enivQu = 1;
        } else if ("B".equals(air)) {
            enivQu = 2;
        } else if ("C".equals(air)) {
            enivQu = 3;
        } else if ("D".equals(air)) {
            enivQu = 4;
        } else if ("E".equals(air)) {
            enivQu = 5;
        } else if ("x".equals(air.toLowerCase())) {
            enivQu = 6;
        }
        while (true) {
            switch (enivQu)
            {
                case 1:
                    this.mBackGround.setBackgroundResource(R.drawable.bg_best);
                    this.mWindIconView.setBackgroundResource(R.drawable.best_fan);
                    this.mTimeIconBgView.setBackgroundResource(R.drawable.best_time);
                    this.mTimeIconView.setBackgroundResource(R.drawable.best_time2);
                    this.mWindText.setTextColor(getResources().getColor(R.color.color_air_best));
                    this.mRunTimeText.setTextColor(getResources().getColor(R.color.color_air_best));
                    this.mCloseButton.setBackgroundResource(R.drawable.best_close_selector);
                    this.mEairValueText.setText(R.string.air_best);
                    break;
                case 4:
                    this.mBackGround.setBackgroundResource(R.drawable.bg_bad);
                    this.mWindIconView.setBackgroundResource(R.drawable.bad_fan);
                    this.mTimeIconBgView.setBackgroundResource(R.drawable.bad_timer);
                    this.mTimeIconView.setBackgroundResource(R.drawable.bad_timer2);
                    this.mWindText.setTextColor(getResources().getColor(R.color.color_air_bad));
                    this.mRunTimeText.setTextColor(getResources().getColor(R.color.color_air_bad));
                    this.mCloseButton.setBackgroundResource(R.drawable.bad_close_selector);
                    this.mEairValueText.setText(R.string.air_bad);
                    break;
                case 3:
                    this.mBackGround.setBackgroundResource(R.drawable.bg_normal);
                    this.mWindIconView.setBackgroundResource(R.drawable.normal_fan);
                    this.mTimeIconBgView.setBackgroundResource(R.drawable.normal_timer);
                    this.mTimeIconView.setBackgroundResource(R.drawable.normal_timer2);
                    this.mWindText.setTextColor(getResources().getColor(R.color.color_air_normal));
                    this.mRunTimeText.setTextColor(getResources()
                            .getColor(R.color.color_air_normal));
                    this.mCloseButton.setBackgroundResource(R.drawable.normal_close_selector);
                    this.mEairValueText.setText(R.string.air_normal);
                    break;
                case 2:
                    this.mBackGround.setBackgroundResource(R.drawable.bg_good);
                    this.mWindIconView.setBackgroundResource(R.drawable.good_fan);
                    this.mTimeIconBgView.setBackgroundResource(R.drawable.good_timer);
                    this.mTimeIconView.setBackgroundResource(R.drawable.good_timer2);
                    this.mWindText.setTextColor(getResources().getColor(R.color.color_air_good));
                    this.mRunTimeText.setTextColor(getResources().getColor(R.color.color_air_good));
                    this.mCloseButton.setBackgroundResource(R.drawable.good_close_selector);
                    this.mEairValueText.setText(R.string.air_good);
                    break;
                case 6:
                    this.mBackGround.setBackgroundResource(R.drawable.bg_best);
                    this.mWindIconView.setBackgroundResource(R.drawable.best_fan);
                    this.mTimeIconBgView.setBackgroundResource(R.drawable.best_time);
                    this.mTimeIconView.setBackgroundResource(R.drawable.best_time2);
                    this.mWindText.setTextColor(getResources().getColor(R.color.color_air_best));
                    this.mRunTimeText.setTextColor(getResources().getColor(R.color.color_air_best));
                    this.mCloseButton.setBackgroundResource(R.drawable.best_close_selector);
                    this.mEairValueText.setText(R.string.notkown);
                    break;
                default:
                    this.mBackGround.setBackgroundResource(R.drawable.bg_bad);
                    this.mWindIconView.setBackgroundResource(R.drawable.bad_fan);
                    this.mTimeIconBgView.setBackgroundResource(R.drawable.bad_timer);
                    this.mTimeIconView.setBackgroundResource(R.drawable.bad_timer2);
                    this.mWindText.setTextColor(getResources().getColor(R.color.color_air_bad));
                    this.mRunTimeText.setTextColor(getResources().getColor(R.color.color_air_bad));
                    this.mCloseButton.setBackgroundResource(R.drawable.bad_close_selector);
                    this.mEairValueText.setText(R.string.air_bader);
                    break;

            }
            this.mErrorIconView.setVisibility(View.INVISIBLE);
            // 风速档位
            if (fw == 1) {
                TextView textView1 = this.mWindText;
                Object[] object1 = new Object[1];
                object1[0] = Integer.valueOf(1);
                textView1.setText(getString(R.string.format_wind, object1));
                this.mWindAnimation.setDuration(25000L);
            } else if (fw == 2) {
                TextView textView2 = this.mWindText;
                Object[] object2 = new Object[1];
                object2[0] = Integer.valueOf(2);
                textView2.setText(getString(R.string.format_wind, object2));
                this.mWindAnimation.setDuration(20000L);
            } else if (fw == 3) {
                TextView textView3 = this.mWindText;
                Object[] object3 = new Object[1];
                object3[0] = Integer.valueOf(3);
                textView3.setText(getString(R.string.format_wind, object3));
                this.mWindAnimation.setDuration(15000L);
            } else if (fw == 4) {
                TextView textView4 = this.mWindText;
                Object[] object4 = new Object[1];
                object4[0] = Integer.valueOf(4);
                textView4.setText(getString(R.string.format_wind, object4));
                this.mWindAnimation.setDuration(10000L);
            }else if (fw == 5) {
                TextView textView5= this.mWindText;
                Object[] object5 = new Object[1];
                object5[0] = Integer.valueOf(5);
                textView5.setText(getString(R.string.format_wind, object5));
                this.mWindAnimation.setDuration(10000L);
            }
            
            if (this.mTimeIconView.getAnimation() == null)
                this.mTimeIconView.startAnimation(this.mTimerAnimation);
            break;
        }

        if (mode == 0) {
            mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_s, 0, 0);
            mAutoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_n, 0, 0);
            sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_n, 0, 0);
        }
        if (mode == 1) {
            mAutoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_s, 0, 0);
            mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_n, 0, 0);
            sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_n, 0, 0);
        }

        if (mode == 2) {
            sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_s, 0, 0);
            mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_n, 0, 0);
            mAutoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_n, 0, 0);
        }
        if (uv == 0) {
            mKillButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.kill_s, 0, 0);
        }else{
            mKillButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.kill_n, 0, 0);
        }
        if (anion == 0) {
            flz.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.flz_s, 0, 0);
        }else{
            flz.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.flz_n, 0, 0);
        }
        if (hepa == 0) {
            hepaText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hepa_s, 0, 0);
        }

    }

    /***
     * 初始化页面信息
     */
    private void initWeatherView()
    {
        String weather = EairApplaction.todayWeather;
        String aqi = EairApplaction.aqi;
        TextView localTextView1 = this.mLocationWeatherText;
        Object[] object = new Object[3];
        object[0] = aqi;
        if (weather != null&&weather.contains("~")) {
            Object[] objectTem = weather.split("~");
            object[1] = objectTem[0];
            object[2] = objectTem[1];
        } else {
            object[1] = 0;
            object[2] = 1;
        }
        localTextView1.setText(getString(R.string.format_location_weather, object));
    }

    /***
     * 加载动画按钮
     */
    private void loadAnim()
    {
        this.mRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.air_speed_rotate);
        this.mWindAnimation = AnimationUtils.loadAnimation(this, R.anim.wind_anim);
        this.mTimerAnimation = AnimationUtils.loadAnimation(this, R.anim.timer_anim);
    }

    private void setListener()
    {
        this.mWindLayout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                DeviceHomePageActivity.this.showWindDialog();
            }
        });
        this.mTimerLayout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                DeviceHomePageActivity.this.showTimerDialog();
            }
        });

        this.mCloseButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
               
                String mess = null;
                if(intranetAdress==null){
                    String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&poweron=1&userid="
                            + userId + "&cmd";
                    mess=getRepMessage(cmd);
                }else{
                    String cmd = "mac=" + phoneMac + "&set&poweron=1&userid="
                            + userId + "&cmd";
                    mess=getNetRepMessage(intranetAdress,cmd);
                }
                if ("timeout".equals(mess) || mess == null) {
                    if(intranetAdress!=null){
                        intranetAdress=null;
                    }
                    tips();
                } else if (mess.contains("door_open=1")) {
                    openTips();
                } else {
                    if(timer!=null){
                        timer.cancel();
                    }
                    Intent inte = new Intent(DeviceHomePageActivity.this, DeviceListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sb", sb);
                    bundle.putString("userId", userId);
                    inte.putExtra("sbbundle", bundle);
                    DeviceHomePageActivity.this.finish();
                    DeviceHomePageActivity.this.startActivity(inte);
                }
            }
        });
        // 手动
        this.mSilenceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phoneMac = storeSp.getString(Constant.PHONEMAC, "");
             
                String mess = null;
                if(intranetAdress==null){
                    String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&mode=0&userid=" + userId
                            + "&cmd";
                    mess=getRepMessage(cmd);
                }else{
                    String cmd = "mac=" + phoneMac + "&set&mode=0&userid=" + userId
                            + "&cmd";
                    mess=getNetRepMessage(intranetAdress,cmd);
                }
                if ("timeout".equals(mess) || mess == null) {
                    if(intranetAdress!=null){
                        intranetAdress=null;
                    }
                    tips();
                } else if (mess.contains("door_open=1")) {
                    openTips();
                } else {
                    mode=0;
                    mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_s, 0,
                            0);
                    mAutoButton
                            .setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_n, 0, 0);
                    sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_n, 0, 0);
                }

            }
        });
        // 自动
        this.mAutoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mess = null;
                if(intranetAdress==null){
                    String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&mode=1&userid=" + userId
                            + "&cmd";
                    mess=getRepMessage(cmd);
                }else{
                    String cmd ="mac=" + phoneMac + "&set&mode=1&userid=" + userId
                            + "&cmd";
                    mess=getNetRepMessage(intranetAdress,cmd);
                }
                if ("timeout".equals(mess) || mess == null) {
                    if(intranetAdress!=null){
                        intranetAdress=null;
                    }
                    tips();
                } else if (mess.contains("door_open=1")) {
                    openTips();
                } else {
                    mode=1;
                    if(intranetAdress==null){
                        air = mess.split("&")[2].split("=")[1];// rep&success&air=x&end
                    }else{
                        air = mess.split("&")[3].split("=")[1];// rep&success&air=x&end
                    }
                    if ("A".equals(air)) {
                        fw = 1;
                    } else if ("B".equals(air)) {
                        fw = 2;
                    } else if ("C".equals(air)) {
                        fw = 3;
                    } else if ("D".equals(air)) {
                        fw = 4;
                    } else if ("E".equals(air)) {
                        fw = 4;
                    } else if ("x".equals(air)) {
                        fw = 1;
                    }
                    Editor windEd = storeSp.edit();
                    windEd.putString(Constant.AIR, air);
                    windEd.putInt(Constant.WINDDW, fw);
                    windEd.commit();
                    TextView localTextView1 = DeviceHomePageActivity.this.mWindText;
                    localTextView1.setText(getString(R.string.format_wind, fw));
                    mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_n, 0,
                            0);
                    mAutoButton
                            .setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_s, 0, 0);
                    sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_n, 0, 0);
                }

            }
        });
        // 杀菌
        this.mKillButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (uv == 1) {
                    
                    String mess = null;
                    if(intranetAdress==null){
                        String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&uv=0&userid=" + userId
                                + "&cmd";
                        mess=getRepMessage(cmd);
                    }else{
                        String cmd = "mac=" + phoneMac + "&set&uv=0&userid=" + userId
                                + "&cmd";
                        mess=getNetRepMessage(intranetAdress,cmd);
                    }
                    if ("timeout".equals(mess) || mess == null) {
                        if(intranetAdress!=null){
                            intranetAdress=null;
                        }
                        tips();
                    } else if (mess.contains("door_open=1")) {
                        openTips();
                    } else {
                        mKillButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.kill_s,
                                0, 0);
                        uv = 0;
                    }
                } else {
                   
                    String mess = null;
                    if(intranetAdress==null){
                        String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&uv=1&userid=" + userId
                                + "&cmd";
                        mess=getRepMessage(cmd);
                    }else{
                        String cmd ="mac=" + phoneMac + "&set&uv=1&userid=" + userId
                                + "&cmd";
                        mess=getNetRepMessage(intranetAdress,cmd);
                    }
                    if ("timeout".equals(mess) || mess == null) {
                        if(intranetAdress!=null){
                            intranetAdress=null;
                        }
                        tips();
                    } else if (mess.contains("door_open=1")) {
                        openTips();
                    } else {
                        mKillButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.kill_n,
                                0, 0);
                        uv = 1;
                    }
                }
            }
        });
        // 负离子
        this.flz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (anion == 1) {
                  
                    String mess = null;
                    if(intranetAdress==null){
                        String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&anion=0&userid="
                                + userId + "&cmd";
                        mess=getRepMessage(cmd);
                    }else{
                        String cmd = "mac=" + phoneMac + "&set&anion=0&userid="
                                + userId + "&cmd";
                        mess=getNetRepMessage(intranetAdress,cmd);
                    }
                    if ("timeout".equals(mess) || mess == null) {
                        if(intranetAdress!=null){
                            intranetAdress=null;
                        }
                        tips();
                    } else if (mess.contains("door_open=1")) {
                        openTips();
                    } else {
                        flz.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.flz_s, 0, 0);
                        anion = 0;
                    }
                } else {
                    
                    String mess = null;
                    if(intranetAdress==null){
                        String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&anion=1&userid="
                                + userId + "&cmd";
                        mess=getRepMessage(cmd);
                    }else{
                        String cmd = "mac=" + phoneMac + "&set&anion=1&userid="
                                + userId + "&cmd";
                        mess=getNetRepMessage(intranetAdress,cmd);
                    }
                    if ("timeout".equals(mess) || mess == null) {
                        if(intranetAdress!=null){
                            intranetAdress=null;
                        }
                        tips();
                    } else if (mess.contains("door_open=1")) {
                        openTips();
                    } else {
                        flz.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.flz_n, 0, 0);
                        anion = 1;
                    }
                }

            }
        });
        // hepa
        /***
         * this.hepaText.setOnClickListener(new View.OnClickListener() {
         * 
         * @Override public void onClick(View v) { } });
         ****/
        // 睡眠
        this.sleep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             
                String mess = null;
                if(intranetAdress==null){
                    String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&mode=2&userid=" + userId
                            + "&cmd";
                    mess=getRepMessage(cmd);
                }else{
                    String cmd = "mac=" + phoneMac + "&set&mode=2&userid=" + userId
                            + "&cmd";
                    mess=getNetRepMessage(intranetAdress,cmd);
                }
                if ("timeout".equals(mess) || mess == null) {
                    if(intranetAdress!=null){
                        intranetAdress=null;
                    }
                    tips();
                } else if (mess.contains("door_open=1")) {
                    openTips();
                } else {
                    mode=2;
                    Editor windEd = storeSp.edit();
                    windEd.putInt(Constant.WINDDW, 1);
                    windEd.commit();
                    fw = 1;
                    TextView localTextView1 = DeviceHomePageActivity.this.mWindText;
                    localTextView1.setText(getString(R.string.format_wind, fw));
                    mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_n, 0,
                            0);
                    mAutoButton
                            .setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_n, 0, 0);
                    sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_s, 0, 0);
                }

            }
        });
    }

    private void showTimerDialog()
    {
        BLTimeAlert.showAlert(this, time, 1, 8, new BLTimeAlert.OnAlertSelectId()
        {
            public void onClick(int paramAnonymousInt) {
              
                String mess = null;
                if(intranetAdress==null){
                    String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&timer="
                            + paramAnonymousInt + "&userid=" + userId + "&cmd";
                    mess=getRepMessage(cmd);
                }else{
                    String cmd = "mac=" + phoneMac + "&set&timer="
                            + paramAnonymousInt + "&userid=" + userId + "&cmd";
                    mess=getNetRepMessage(intranetAdress,cmd);
                }
                if ("timeout".equals(mess) || mess == null) {
                    if(intranetAdress!=null){
                        intranetAdress=null;
                    }
                    tips();
                } else if (mess.contains("door_open=1")) {
                    openTips();
                } else {// 返回成功了
                    Editor timeEd = storeSp.edit();
                    timeEd.putInt(Constant.TIME, paramAnonymousInt);
                    timeEd.commit();
                    time = paramAnonymousInt;
                    TextView localTextView1 = DeviceHomePageActivity.this.mRunTimeText;
                    localTextView1.setText(getString(R.string.format_time, time));
                }
            }
        }).show();
    }

    private void showWindDialog()
    {
        // 风力有四个档位
        BLWindAlert.showAlert(this, fw, new BLWindAlert.OnAlertSelectId()
        {
            public void onClick(int paramAnonymousInt)
            {
                Log.i(">>>>>>>>>>>", String.valueOf(paramAnonymousInt));
                String mess = null;
                if(intranetAdress==null){
                    String cmd = "wlsn=" + wlsn + "&mac=" + phoneMac + "&set&speed="
                            + paramAnonymousInt + "&userid=" + userId + "&cmd";
                    mess=getRepMessage(cmd);
                }else{
                    String cmd = "mac=" + phoneMac + "&set&speed="
                            + paramAnonymousInt + "&userid=" + userId + "&cmd";
                    mess=getNetRepMessage(intranetAdress,cmd);
                }
                if ("timeout".equals(mess) || mess == null) {
                    if(intranetAdress!=null){
                        intranetAdress=null;
                    }
                    tips();
                } else if (mess.contains("door_open=1")) {
                    openTips();
                } else {// 返回成功了
                    Editor windEd = storeSp.edit();
                    windEd.putInt(Constant.WINDDW, paramAnonymousInt);
                    windEd.commit();
                    fw = paramAnonymousInt;
                    TextView localTextView1 = DeviceHomePageActivity.this.mWindText;
                    localTextView1.setText(getString(R.string.format_wind, fw));
                    // 设置风速变成手动
                    mSilenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hand_s, 0,
                            0);
                    mAutoButton
                            .setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.auto2_n, 0, 0);
                    sleep.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sleep_n, 0, 0);
                }
            }
        }).show();
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.device_home_page_layout);
        Bundle bundle = this.getIntent().getExtras();
        wlsn = bundle.getString("wlsn");
        sb = bundle.getString("sb");
        userId = bundle.getString("userId");
        EairApplaction.getInstance().addActivity(this);
        loadAnim();
        findView();
        setListener();
        storeSp = this.getSharedPreferences(Constant.STOREDB, 0);
        phoneMac = storeSp.getString(Constant.PHONEMAC, "");
        fw = storeSp.getInt(Constant.WINDDW, 1);
        time = storeSp.getInt(Constant.TIME, 0);
        air = storeSp.getString(Constant.AIR, "A");
        mode = storeSp.getInt(Constant.MODE, 1);
        uv = storeSp.getInt(Constant.UV, 1);
        anion = storeSp.getInt(Constant.ANION, 1);
        hepa = storeSp.getInt(Constant.HEPA, 1);
        pm= storeSp.getInt(Constant.PM, 1);
        //添加一个判断是否在内网
        WebServiceUtil ws=new WebServiceUtil();
        String result=ws.areaNetWork(wlsn, phoneMac, userId);
        if(!"not".equals(result)){//手机和设备在同一个内网
           intranetAdress=result;
       }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 点击返回就退出应用
              EairApplaction.getInstance().exit();  
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onPause()
    {
        super.onPause();
        this.mRunProgress.setVisibility(4);
        this.mRunProgress.clearAnimation();
        this.mInRefreshIng = false;
        if (this.timer != null)
        {
            this.timer.cancel();
            this.timer = null;
        }
    }

    protected void onResume()
    {
        super.onResume();
        initWeatherView();
        initEairView();
        this.mWindIconView.startAnimation(this.mWindAnimation);
        this.mRunProgress.startAnimation(this.mRotateAnimation);
        this.mInRefreshIng = true;
        if (this.timer == null) {
            this.timer = new Time();
            timer.test();// //启动定时器任务
        }
    }

    public class Time extends TimerTask {
        private Timer timer = null;// 开始要将timer对象赋初值null
        private long timestop = 20000;// 每隔20秒执行一次任务

        public Time() {
            timer = new Timer();
            System.out.println("java定时器测试");
        }// 构造函数

        public void test()
        {
            Time t = new Time();
            Date date = new Date();
            timer.schedule(t, date, timestop);
        }// 真正启动任务的函数，当在main函数中new一个新对象时，调用此函数，在此函数中有schedule方法，启动任务。

        @Override
        public void run() {
            getInitData();
            air = storeSp.getString(Constant.AIR, "A");
            fw = storeSp.getInt(Constant.WINDDW, 1);
            time = storeSp.getInt(Constant.TIME, 0);
            mode = storeSp.getInt(Constant.MODE, 1);
            uv = storeSp.getInt(Constant.UV, 1);
            anion = storeSp.getInt(Constant.ANION, 1);
            hepa = storeSp.getInt(Constant.HEPA, 1);
            pm= storeSp.getInt(Constant.PM, 1);
            Message msg = new Message();
            msg.what = 1;
            handle.sendMessage(msg);

        }

    }

    /***
     * 根据空气环境质量修改页面显示的
     */
    Handler handle = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DeviceHomePageActivity.this.initEairView();
                    break;
                case -1:// 超时操作
                    tips();
                    break;
                case 2:
                    openTips();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public void getInitData() {
        String str = "wlsn=" + wlsn + "&mac=" + phoneMac + "&get&init&userid=" + userId + "&cmd";
        String repMessage = getRepMessage(str);
        if ("timeout".equals(repMessage) || repMessage == null) {
            Message msg2 = new Message();
            msg2.what = -1;
            handle.sendMessage(msg2);
        } else if (repMessage.contains("door_open=1")) {
            Message msg2 = new Message();
            msg2.what = 2;
            handle.sendMessage(msg2);
        } else {// 返回的信息,对运行状态进行保存
            if (!repMessage.contains("success")) {
                storeData(repMessage);
            }
        }
    }

    /***
     * 处理返回的init设备的信息
     * 
     * @param message
     */
    private void storeData(String message) {// rep&timer=0&speed=3&pmx=15&air=A&mode=1&uv=1&anion=1&hepa=1&end
        Editor editor = storeSp.edit();
        String[] mess=message.split("&");
        if(mess.length==10){
            editor.putInt(Constant.TIME, Integer.parseInt(mess[1].split("=")[1]));
            editor.putInt(Constant.WINDDW, Integer.parseInt(mess[2].split("=")[1]));
            editor.putInt(Constant.PM, Integer.parseInt(mess[3].split("=")[1]));
            editor.putString(Constant.AIR, mess[4].split("=")[1]);
            editor.putInt(Constant.MODE, Integer.parseInt(mess[5].split("=")[1]));
            editor.putInt(Constant.UV, Integer.parseInt(mess[6].split("=")[1]));
            editor.putInt(Constant.ANION, Integer.parseInt(mess[7].split("=")[1]));
            editor.putInt(Constant.HEPA,Integer.parseInt( mess[8].split("=")[1]));
            editor.commit();
        }
    }

}
