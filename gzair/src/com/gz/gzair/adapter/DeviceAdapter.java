
package com.gz.gzair.adapter;
import java.util.List;
import com.gz.gzair.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class DeviceAdapter extends BaseAdapter {

    private List<String> lists;

    private LayoutInflater mInflater;

    public DeviceAdapter( List<String> list, Context context) {
        this.lists = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // 装载布局文件
            convertView = mInflater.inflate(R.layout.device_list_item_layout, null);
        }
        String mac= lists.get(position);
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        deviceName.setText(mac);
        return convertView;
    }

}
