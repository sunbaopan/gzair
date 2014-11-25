package com.gz.gzair.activity;

import com.gz.gzair.Constant;
import com.gz.gzair.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
public class ShowActivity extends BaseActivity {
    
    private SharedPreferences storeSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shownotice_layout);
        storeSp = this.getSharedPreferences(Constant.STOREDB, 0);
        TextView  text =(TextView) this.findViewById(R.id.title);
        text.setText(storeSp.getString(Constant.TITLE,""));
        TextView  content =(TextView) this.findViewById(R.id.content);
        content.setText(storeSp.getString(Constant.CONTENT,""));
    }

   
}
