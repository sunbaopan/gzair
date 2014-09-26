
package com.gz.gzair.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;

import com.gz.gzair.R;

public class BLTimeAlert
{
    
    private static int mOnSelected;

    public static Dialog showAlert(Context paramContext, int paramInt1, int paramInt2,
            int paramInt3, final OnAlertSelectId paramOnAlertSelectId)
    {
        Dialog localDialog = new Dialog(paramContext, R.style.MMTheme_DataSheet);
        LinearLayout localLinearLayout = (LinearLayout) ((LayoutInflater) paramContext
                .getSystemService("layout_inflater")).inflate(R.layout.bl_timer_layout, null);
        localLinearLayout.setMinimumWidth(10000);
        SeekBar localSeekBar = (SeekBar) localLinearLayout.findViewById(R.id.time_bar);
        final CheckBox localCheckBox0=(CheckBox) localLinearLayout.findViewById(R.id.time_0);
        final CheckBox localCheckBox1 = (CheckBox) localLinearLayout.findViewById(R.id.time_1);
        final CheckBox localCheckBox2 = (CheckBox) localLinearLayout.findViewById(R.id.time_2);
        final CheckBox localCheckBox3 = (CheckBox) localLinearLayout.findViewById(R.id.time_3);
        final CheckBox localCheckBox4 = (CheckBox) localLinearLayout.findViewById(R.id.time_4);
        localSeekBar.setMax(100);
        if(paramInt1==0){
            localCheckBox0.setChecked(true);
            localCheckBox1.setChecked(false);
            localCheckBox2.setChecked(false);
            localCheckBox3.setChecked(false);
            localCheckBox4.setChecked(false);
            localSeekBar.setProgress(0);
        }else if(paramInt1==2){
            localCheckBox0.setChecked(true);
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(false);
            localCheckBox3.setChecked(false);
            localCheckBox4.setChecked(false);
            localSeekBar.setProgress(25);
        }else if(paramInt1==4){
            localCheckBox0.setChecked(true);
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(false);
            localCheckBox4.setChecked(false);
            localSeekBar.setProgress(50);
        }else if(paramInt1==6){
            localCheckBox0.setChecked(true);
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(true);
            localCheckBox4.setChecked(false);
            localSeekBar.setProgress(75);
        }else if(paramInt1==8){
            localCheckBox0.setChecked(true);
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(true);
            localCheckBox4.setChecked(true);
            localSeekBar.setProgress(99);
        }
            
        localSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt,
                    boolean paramAnonymousBoolean)
            {
                if (paramAnonymousInt < 12)
                {  
                    if(paramAnonymousInt!=0){
                        paramAnonymousSeekBar.setProgress(0);
                    }else{
                        return;
                    }
                    localCheckBox0.setChecked(true);
                    localCheckBox1.setChecked(false);
                    localCheckBox2.setChecked(false);
                    localCheckBox3.setChecked(false);
                    localCheckBox4.setChecked(false);
                }else if ((paramAnonymousInt >= 12) && (paramAnonymousInt < 37))
                {
                    if(paramAnonymousInt!=25){
                        paramAnonymousSeekBar.setProgress(25);
                    }else{
                        return;
                    }
                    localCheckBox0.setChecked(true);
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(false);
                    localCheckBox3.setChecked(false);
                    localCheckBox4.setChecked(false);
                }
                else if ((paramAnonymousInt >= 37) && (paramAnonymousInt < 62))
                {
                    if(paramAnonymousInt!=50){
                        paramAnonymousSeekBar.setProgress(50);
                    }else{
                        return;
                    }
                    localCheckBox0.setChecked(true);
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(false);
                    localCheckBox4.setChecked(false);
                }else if  ((paramAnonymousInt >= 62) && (paramAnonymousInt < 87)) {
                    if(paramAnonymousInt!=75){
                        paramAnonymousSeekBar.setProgress(75);
                    }else{
                        return;
                    }
                    localCheckBox0.setChecked(true);
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(true);
                    localCheckBox4.setChecked(false);
                }
                
                else if  ((paramAnonymousInt >= 87) && (paramAnonymousInt < 100))
                {
                    if(paramAnonymousInt!=100){
                        paramAnonymousSeekBar.setProgress(100);
                    }else{
                        return;
                    }
                    localCheckBox0.setChecked(true);
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(true);
                    localCheckBox4.setChecked(true);
                }
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
            {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
            {
                if (paramAnonymousSeekBar.getProgress() < 12){
                    BLTimeAlert.mOnSelected = 0;
                    paramOnAlertSelectId.onClick(BLTimeAlert.mOnSelected );
                }
               else  if ((paramAnonymousSeekBar.getProgress() >= 12)
                        && (paramAnonymousSeekBar.getProgress() <37)){
                    
                    BLTimeAlert.mOnSelected = 2;
                    paramOnAlertSelectId.onClick(BLTimeAlert.mOnSelected );
               }
                else if ((paramAnonymousSeekBar.getProgress() >= 37)
                        && (paramAnonymousSeekBar.getProgress() < 62))
                {
                    
                    BLTimeAlert.mOnSelected = 4;
                    paramOnAlertSelectId.onClick(BLTimeAlert.mOnSelected );
                }
                else if ((paramAnonymousSeekBar.getProgress() >= 62)
                        && (paramAnonymousSeekBar.getProgress() <87)){
                    BLTimeAlert.mOnSelected = 6;
                    paramOnAlertSelectId.onClick(BLTimeAlert.mOnSelected );
                }
                else if ((paramAnonymousSeekBar.getProgress() >= 87)
                        && (paramAnonymousSeekBar.getProgress() <=100)){
                    BLTimeAlert.mOnSelected = 8;
                    paramOnAlertSelectId.onClick(BLTimeAlert.mOnSelected );
                }
            }
        });
 
        localDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            public void onCancel(DialogInterface paramAnonymousDialogInterface)
            {
            }
        });
   
        WindowManager.LayoutParams localLayoutParams = localDialog.getWindow().getAttributes();
        localLayoutParams.x = 0;
        localLayoutParams.y = -1000;
        localLayoutParams.gravity = 80;
        localDialog.onWindowAttributesChanged(localLayoutParams);
        localDialog.setCanceledOnTouchOutside(true);
        localDialog.setContentView(localLinearLayout);
        return localDialog;
    }
    
    public static  void onClick(int mOnSelected2) {
        onAlertSelectId.onClick(mOnSelected2);
    }
    public static  OnAlertSelectId onAlertSelectId = null; 
    
    public static abstract interface OnAlertSelectId
    {
        public abstract void onClick(int paramInt);
    }

    static class TimeAdapter extends BaseAdapter
    {
        private Context mContext;
        private LayoutInflater mInflater;
        private int mSelection;
        private ArrayList<String> universityList = new ArrayList();

        public TimeAdapter(Context paramContext, int paramInt1, int paramInt2)
        {
            for (int i = paramInt2;; i++)
            {
                if (i > paramInt1)
                {
                    this.mContext = paramContext;
                    this.mInflater = LayoutInflater.from(this.mContext);
                    return;
                }
                this.universityList.add(String.valueOf(i));
            }
        }

        public int getCount()
        {
            return universityList.size();
        }

        public String getItem(int paramInt)
        {
            return (String) this.universityList.get(paramInt % 8);
        }

        public long getItemId(int paramInt)
        {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
            ViewHolder localViewHolder;
            if (paramView == null)
            {
                localViewHolder = new ViewHolder();
                paramView = this.mInflater.inflate(R.layout.galley_item_layout, null);
                localViewHolder.text = ((TextView) paramView.findViewById(R.id.text));
                paramView.setTag(localViewHolder);
                localViewHolder.text.setText((CharSequence) this.universityList.get(paramInt % 8));
                if (this.mSelection != paramInt % 8) {
                    localViewHolder.text.setTextSize(2, 30.0F);
                    localViewHolder.text.setTextColor(this.mContext.getResources().getColor(
                            R.color.color_green));
                }
            }
            localViewHolder = (ViewHolder) paramView.getTag();
            localViewHolder.text.setTextSize(2, 20.0F);
            localViewHolder.text.setTextColor(this.mContext.getResources().getColor(
                    R.color.color_gray));
            return paramView;
        }

        public void setOnselection(int paramInt)
        {
            this.mSelection = (paramInt % 8);
            notifyDataSetChanged();
        }

        class ViewHolder
        {
            TextView text;

            ViewHolder()
            {
            }
        }
    }
}
