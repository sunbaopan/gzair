
package com.gz.gzair.activity;

import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import com.gz.gzair.util.UdpHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {

    public UdpHelper udpH;

    public int interval;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        udpH=new UdpHelper();
        
    }

    /****
     * 发送命令获取返回值
     * 
     * @param str
     * @return
     */
    public String getRepMessage(String str) {
        String message = null;
        try {
            Thread vThread=new Thread(new VisitorThread(str));
            vThread.start();
            for (;;) {
                if (UdpHelper.result != null) {
                    message = UdpHelper.result;
                    UdpHelper.result = null;
                    interval=0;
                    break;
                }
                Thread.sleep(500);
                interval += 1;
                if (interval > Constant.WAITTIME) {
                    message = "timeout";
                    interval = 0;// 初始化，恢复0。
                    break;
                }
            }
            vThread.interrupt();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }
    
    public class VisitorThread implements Runnable{
        private String str;
        public VisitorThread(String str){
            this.str=str;
        }
        @Override
        public void run() {
            try {
                udpH.sendSb(Constant.IPADDRESS, Constant.WPORT, str);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    
    
    
    /****
     * 通过内网进行来传输命令
     * @param str
     * @return
     */
    public String getNetRepMessage(String intranetAdress,String str) {
        String message = null;
        try {
            Thread vThread=new Thread(new VisitorNetThread(intranetAdress,str));
            vThread.start();
            for (;;) {
                if (UdpHelper.result != null) {
                    message = UdpHelper.result;
                    UdpHelper.result = null;
                    interval=0;
                    break;
                }
                Thread.sleep(500);
                interval += 1;
                if (interval > Constant.WAITTIME) {
                    message = "timeout";
                    interval = 0;// 初始化，恢复0。
                    break;
                }
            }
            vThread.interrupt();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }
    
    public class VisitorNetThread implements Runnable{
        private String str;
        private String intranetAdress;
        public VisitorNetThread(String intranetAdress,String str){
            this.str=str;
            this.intranetAdress=intranetAdress;
        }
        @Override
        public void run() {
            try {
                udpH.sendSb(intranetAdress, Constant.SBNETPORT, str);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    public  void tips(){
        Toast.makeText(BaseActivity.this, BaseActivity.this.getResources().getString(R.string.netException),
                Toast.LENGTH_SHORT).show();
    }
    
    public  void openTips(){
        Toast.makeText(BaseActivity.this, BaseActivity.this.getResources().getString(R.string.dooropen),
                Toast.LENGTH_SHORT).show();
    }
    
    
    
    public  void loadTips(){
        Toast.makeText(BaseActivity.this, BaseActivity.this.getResources().getString(R.string.netError),
                Toast.LENGTH_SHORT).show();
        new TipThread().start();
    }
    
    
    public  void loadTipss(){
        Toast.makeText(BaseActivity.this, BaseActivity.this.getResources().getString(R.string.netError),
                Toast.LENGTH_SHORT).show();
    }
    public  class TipThread extends Thread{

        @Override
        public void run() {
            //暂停3s
            try {
                Thread.sleep(3000);
                EairApplaction.getInstance().exit();  
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         
        }
        
    }

    
    
    

}
