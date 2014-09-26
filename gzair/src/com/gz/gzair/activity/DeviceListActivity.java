package com.gz.gzair.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import com.gz.gzair.adapter.DeviceAdapter;
import com.gz.gzair.util.UdpHelper;
/****
 * ��ȡ�豸���б���
 * @author sbp
 *
 */
public class DeviceListActivity extends BaseActivity {
    private List<String> lists=new ArrayList<String>();
    private SharedPreferences storeSP;
    private  LinearLayout add_device_layout;
    private String phoneMac;
    private String sb;//���û��µ������豸�б�
    private String userId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EairApplaction.getInstance().addActivity(this);
        setContentView(R.layout.device_list_layout);
        storeSP=this.getSharedPreferences(Constant.STOREDB, 0);
        Bundle bundle=this.getIntent().getBundleExtra("sbbundle");
        sb=bundle.getString("sb");
        userId=bundle.getString("userId");
        phoneMac=storeSP.getString(Constant.PHONEMAC, "");
        if(!"0".equals(sb)){
            String[] sbs=sb.split("&");
            for(String mac:sbs){
                if(mac.contains(":")){
                    lists.add(mac.split(":")[0]);
                }else{
                    lists.add(mac);
                }
            }
        }
        ListView listView=(ListView) this.findViewById(R.id.deviceList);
        add_device_layout=(LinearLayout) this.findViewById(R.id.add_device_layout);
        listView.setAdapter(new DeviceAdapter(lists,this));
        //�������������¼�
        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                String mac=lists.get(pos);
                //������Լ�һ����ʾ����
                Thread tt=new Thread(new StartThread(mac));
                tt.start();
            }
        });
        add_device_layout.setOnClickListener(new AddDeviceListener(userId));
    }
    
    
    private class AddDeviceListener implements OnClickListener{
        
        private String userId;
        
        public  AddDeviceListener(String userId){
            this.userId=userId;
        }

        @Override
        public void onClick(View arg0) {
            Intent  intent=new Intent(DeviceListActivity.this,AddDeviceActivity.class);
            Bundle bundle=new  Bundle();
            bundle.putString("userId", userId);
            intent.putExtra("uBundle", bundle);
            DeviceListActivity.this.finish();
            DeviceListActivity.this.startActivity(intent);
        }
        
    }
    
    
    /***
     * ���ڴ�����
     */
     private void hQhandle(String mac){
         //�������������������˻�ȡ�豸�ǲ����ڿ���
         try {
             Thread powerThread=new Thread(new PwonerThread(mac));
             powerThread.start();
             for (;;) {
                 if (UdpHelper.result != null) {
                     interval=0;
                     break;
                 }
                 Thread.sleep(500);
                 interval += 1;
                 if (interval > Constant.WAITTIME) {
                     UdpHelper.result = "timeout";
                     interval = 0;// ��ʼ�����ָ�0��
                     break;
                 }
             }
             //�߳�����
             powerThread.interrupt();
         } catch (Exception e) {
             e.printStackTrace();
         }
         Message msg=new  Message();
         msg.obj=mac;
         if ("timeout".equals(UdpHelper.result)||msg==null) {
             msg.what=-1;
             UdpHelper.result=null;
             handle.sendMessage(msg);
         }else if("NotfindSbAddress".equals(UdpHelper.result)){
             msg.what=2;
             UdpHelper.result=null;
             handle.sendMessage(msg);
         }
         else{//���ص���Ϣ
             String repMessage=UdpHelper.result;
             UdpHelper.result=null;
             System.out.println("==repMessage="+repMessage);
             if(repMessage!=null){
                 String mess[] =repMessage.split("&");
                 if(mess[1].contains("=")){
                    String on = mess[1].split("=")[1];
                    msg.what = Integer.parseInt(on);
                    handle.sendMessage(msg);
                 }
             }
         }
       
     }
    
    
    
    /****
     * �������߳̽����ж�
     */
    Handler   handle=new  Handler (){
        @Override
        public void handleMessage(Message msg) {
           String mac=msg.obj.toString();
           if(msg.what==0){//�豸������
               //��ȡ�豸���е�״̬
              String str = "wlsn="+mac+"&mac="+phoneMac+"&get&init&userid="+userId+"&cmd";
              String repMessage=getRepMessage(str);
               if ("timeout".equals(repMessage)||repMessage==null) {
                    Message msg2=new Message();
                    msg2.what=-1;
                    handle.sendMessage(msg2);
               }else{//���ص���Ϣ,������״̬���б���
                   storeData(repMessage);
               }
               Intent intent=new Intent(DeviceListActivity.this,DeviceHomePageActivity.class);
               Bundle bundle=new Bundle();
               bundle.putString("wlsn", mac);
               bundle.putString("sb", sb);
               bundle.putString("userId", userId);
               intent.putExtras(bundle);
               DeviceListActivity.this.finish();
               DeviceListActivity.this.startActivity(intent);
           }else if(msg.what==1){//�豸û�п�����
               Intent intent=new Intent(DeviceListActivity.this,OpenActivity.class);
               Bundle bundle=new Bundle();
               bundle.putString("wlsn", mac);
               bundle.putString("sb", sb);
               bundle.putString("userId", userId);
               intent.putExtras(bundle);
               DeviceListActivity.this.finish();
               DeviceListActivity.this.startActivity(intent);
           }else if(msg.what==-1){
               loadTipss();
           }else if(msg.what==2){
               Toast.makeText(DeviceListActivity.this, DeviceListActivity.this.getResources().getString(R.string.notsb1),
                       Toast.LENGTH_LONG).show();
           }
            super.handleMessage(msg);
        } 
        
    };
    
    
    private class PwonerThread  implements Runnable{
       private String mac;
       
       public PwonerThread(String mac){
           this.mac=mac;
       }
        @Override
        public void run() {
            String str = "wlsn="+mac+"&mac="+phoneMac+"&get&ispoweron&userid="+userId+"&cmd";
            try {
                udpH.sendSb(Constant.IPADDRESS,Constant.WPORT, str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
     }
    
    
    /***
     * �������ص�init�豸����Ϣ
     * @param message
     */
    private void  storeData(String message){//rep&timer=0&speed=3&pmx=15&air=A&mode=1&uv=1&anion=1&hepa=1&end
        Editor editor=storeSP.edit();
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
    
    
    
    private class StartThread implements Runnable{
        
        private String mac;
        
        public StartThread(String mac){
            this.mac=mac;
        }
        @Override
        public void run() {
                hQhandle(mac);
            }
     }
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // ������ؾ��˳�Ӧ��
              EairApplaction.getInstance().exit();  
        }
        return super.onKeyDown(keyCode, event);
    }

    
}