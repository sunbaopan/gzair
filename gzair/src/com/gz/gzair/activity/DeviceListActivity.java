
package com.gz.gzair.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gz.gzair.Constant;
import com.gz.gzair.EairApplaction;
import com.gz.gzair.R;
import com.gz.gzair.adapter.DeviceAdapter;
import com.gz.gzair.domain.DeviceDomain;
import com.gz.gzair.util.UdpHelper;
import com.gz.gzair.util.WebServiceUtil;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;

/****
 * ��ȡ�豸���б��
 * 
 * @author sbp
 */
public class DeviceListActivity extends BaseActivity {
    private List<DeviceDomain> lists = new ArrayList<DeviceDomain>();
    private SharedPreferences storeSP;
    public static String g_userId = null;
    public static DeviceListActivity devicelistinstance = null;
    private LinearLayout add_device_layout;
    private String phoneMac;
    private String sb;// ���û��µ������豸�б�
    private String userId;
    private int count = 0;// û��ע��������Ĵ���

    private ListView listView;

    private int pos;// �������λ��
    
    private DeviceAdapter  da;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EairApplaction.getInstance().addActivity(this);
        setContentView(R.layout.device_list_layout);
        storeSP = this.getSharedPreferences(Constant.STOREDB, 0);
        Bundle bundle = this.getIntent().getBundleExtra("sbbundle");
        sb = bundle.getString("sb");
        userId = bundle.getString("userId");
        g_userId = userId;
        devicelistinstance = this;
        phoneMac = storeSP.getString(Constant.PHONEMAC, "");
        if (sb == null) {
            WebServiceUtil wsu = new WebServiceUtil();
            sb = wsu.querySbByUserId(userId);
            if (sb == null) {
                try {
                    Thread.sleep(Constant.SLEEPTIME);
                    sb = wsu.querySbByUserId(userId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!"0".equals(sb) && sb != null) {
            String[] sbs = sb.split("&");
            for (String str : sbs) {
                DeviceDomain domian = new DeviceDomain();
                String newStr[] = str.split(":");
                domian.setMac(newStr[0]);
                domian.setPoweron(newStr[1]);
                domian.setAir(newStr[2]);
                domian.setBm(newStr[3]);
                lists.add(domian);
            }
        }
        listView = (ListView) this.findViewById(R.id.deviceList);
        add_device_layout = (LinearLayout) this.findViewById(R.id.add_device_layout);
        da=new DeviceAdapter(lists, this, userId);
        listView.setAdapter(da);
        // �������������¼�
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                String mac = lists.get(pos).getMac();
                // ������Լ�һ����ʾ����
                Thread tt = new Thread(new StartThread(mac));
                tt.start();
            }
        });
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                pos = arg2;
                ItemOnLongClick1();
                return false;
            }

        });
        add_device_layout.setOnClickListener(new AddDeviceListener(userId));
    }

    private void ItemOnLongClick1() {
        // ע��setOnCreateContextMenuListener��������onContextItemSelected����ʹ�õ�
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v,
                    ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "ɾ��");
                menu.add(0, 1, 0, "�޸�");
            }
        });
    }

    // �����˵���Ӧ����
    public boolean onContextItemSelected(MenuItem item) {
        DeviceDomain domain = lists.get(pos);
        switch (item.getItemId()) {
            case 0:// ɾ������
                WebServiceUtil ws=new WebServiceUtil();
                String result=ws.deleteIP(domain.getMac(), userId);
                if(result==null){//���Է���2�Σ�ȷ��ɾ���ɹ���
                    try {
                        Thread.sleep(Constant.SLEEPTIME);
                        result =ws.deleteIP(domain.getMac(), userId);
                        if(result==null){
                            Thread.sleep(Constant.SLEEPTIME);
                            result =ws.deleteIP(domain.getMac(), userId);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if("0".equals(result)||result==null){//ɾ��ʧ��  �Ժ�����
                    Toast.makeText(DeviceListActivity.this, DeviceListActivity.this.getResources().getString(R.string.delfail),
                            Toast.LENGTH_SHORT).show();
                }else{
                    lists.remove(pos);
                    da.notifyDataSetChanged();
                }
                break;
            case 1: // �޸Ĳ���
                Intent intent = new Intent();
                intent.setClass(DeviceListActivity.this, RenameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bm", domain.getBm());
                bundle.putString("mac", domain.getMac());
                bundle.putString("userid", userId);
                intent.putExtra("macbundle", bundle);
                DeviceListActivity.this.startActivity(intent);
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);

    }

    private class AddDeviceListener implements OnClickListener {

        private String userId;

        public AddDeviceListener(String userId) {
            this.userId = userId;
        }

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(DeviceListActivity.this, AddDeviceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            intent.putExtra("uBundle", bundle);
            DeviceListActivity.this.finish();
            DeviceListActivity.this.startActivity(intent);
        }

    }

    /***
     * ���ڴ���
     */
    private void hQhandle(String mac) {
        // �������������������˻�ȡ�豸�ǲ����ڿ���
        try {
            Thread powerThread = new Thread(new PwonerThread(mac));
            powerThread.start();
            for (;;) {
                if (UdpHelper.result != null) {
                    interval = 0;
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
            // �߳�����
            powerThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = mac;
        if ("timeout".equals(UdpHelper.result) || msg == null) {
            msg.what = -1;
            UdpHelper.result = null;
            handle.sendMessage(msg);
        } else if ("NotfindSbAddress".equals(UdpHelper.result)) {
            count += 1;
            if (count >= 3) {// ������������豸��ʱ���������
                msg.what = 3;
                UdpHelper.result = null;
                handle.sendMessage(msg);
            } else {
                msg.what = 2;
                UdpHelper.result = null;
                handle.sendMessage(msg);
            }
        }
        else {// ���ص���Ϣ
            String repMessage = UdpHelper.result;
            UdpHelper.result = null;
            System.out.println("==repMessage=" + repMessage);
            if (repMessage != null) {
                String mess[] = repMessage.split("&");
                if (mess[1].contains("=")) {
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
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mac = msg.obj.toString();
            if (msg.what == 0) {// �豸������
                // ��ȡ�豸���е�״̬
                String str = "wlsn=" + mac + "&mac=" + phoneMac + "&get&init&userid=" + userId
                        + "&cmd";
                String repMessage = getRepMessage(str);
                if ("timeout".equals(repMessage) || repMessage == null) {
                    Message msg2 = new Message();
                    msg2.what = -1;
                    handle.sendMessage(msg2);
                } else {// ���ص���Ϣ,������״̬���б���
                    storeData(repMessage);
                }
                Intent intent = new Intent(DeviceListActivity.this, DeviceHomePageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wlsn", mac);
                bundle.putString("sb", sb);
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                // DeviceListActivity.this.finish();
                DeviceListActivity.this.startActivity(intent);
            } else if (msg.what == 1) {// �豸û�п�����
                Intent intent = new Intent(DeviceListActivity.this, OpenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wlsn", mac);
                bundle.putString("sb", sb);
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                // DeviceListActivity.this.finish();
                DeviceListActivity.this.startActivity(intent);
            } else if (msg.what == -1) {
                loadTipss();
            } else if (msg.what == 2) {
                Toast.makeText(DeviceListActivity.this,
                        DeviceListActivity.this.getResources().getString(R.string.notsb1),
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(DeviceListActivity.this,
                        DeviceListActivity.this.getResources().getString(R.string.notsb2),
                        Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }

    };

    private class PwonerThread implements Runnable {
        private String mac;

        public PwonerThread(String mac) {
            this.mac = mac;
        }

        @Override
        public void run() {
            String str = "wlsn=" + mac + "&mac=" + phoneMac + "&get&ispoweron&userid=" + userId
                    + "&cmd";
            try {
                udpH.sendSb(Constant.IPADDRESS, Constant.WPORT, str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * �����ص�init�豸����Ϣ
     * 
     * @param message
     */
    private void storeData(String message) {// rep&timer=0&speed=3&pmx=15&air=A&mode=1&uv=1&anion=1&hepa=1&end
        Editor editor = storeSP.edit();
        String[] mess = message.split("&");
        if (mess.length == 10) {
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

    private class StartThread implements Runnable {

        private String mac;

        public StartThread(String mac) {
            this.mac = mac;
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
