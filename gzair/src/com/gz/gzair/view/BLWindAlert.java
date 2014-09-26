
package com.gz.gzair.view;

import com.gz.gzair.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class BLWindAlert extends Dialog
{
    public BLWindAlert(Context context) {
        super(context);
    }

    private static int mOnSelected;

    public static Dialog showAlert(Context paramContext, int paramInt,
            final OnAlertSelectId paramOnAlertSelectId)
    {
        Dialog localDialog = new Dialog(paramContext, R.style.MMTheme_DataSheet);
        LinearLayout localLinearLayout = (LinearLayout) ((LayoutInflater) paramContext
                .getSystemService("layout_inflater")).inflate(R.layout.bl_seekbar_layout, null);
        localLinearLayout.setMinimumWidth(10000);
        SeekBar localSeekBar = (SeekBar) localLinearLayout.findViewById(R.id.wind_bar);
        final CheckBox localCheckBox1 = (CheckBox) localLinearLayout.findViewById(R.id.wind_1);
        final CheckBox localCheckBox2 = (CheckBox) localLinearLayout.findViewById(R.id.wind_2);
        final CheckBox localCheckBox3 = (CheckBox) localLinearLayout.findViewById(R.id.wind_3);
        final CheckBox localCheckBox4 = (CheckBox) localLinearLayout.findViewById(R.id.wind_4);
        final CheckBox localCheckBox5 = (CheckBox) localLinearLayout.findViewById(R.id.wind_5);
        localSeekBar.setMax(100);
        if (paramInt == 1) {
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(false);
            localCheckBox3.setChecked(false);
            localCheckBox4.setChecked(false);
            localCheckBox5.setChecked(false);
            localSeekBar.setProgress(0);
        } else if (paramInt == 2) {
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(false);
            localCheckBox4.setChecked(false);
            localCheckBox5.setChecked(false);
            localSeekBar.setProgress(25);
        } else if (paramInt == 3) {
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(true);
            localCheckBox4.setChecked(false);
            localCheckBox5.setChecked(false);
            localSeekBar.setProgress(50);
        } else if (paramInt == 4) {
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(true);
            localCheckBox4.setChecked(true);
            localCheckBox5.setChecked(false);
            localSeekBar.setProgress(75);
        } else if (paramInt == 5) {
            localCheckBox1.setChecked(true);
            localCheckBox2.setChecked(true);
            localCheckBox3.setChecked(true);
            localCheckBox4.setChecked(true);
            localCheckBox5.setChecked(true);
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
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(false);
                    localCheckBox3.setChecked(false);
                    localCheckBox4.setChecked(false);
                    localCheckBox5.setChecked(false);
                }else if ((paramAnonymousInt >= 12) && (paramAnonymousInt < 37))
                {
                    if(paramAnonymousInt!=25){
                            paramAnonymousSeekBar.setProgress(25);
                    }else{
                        return;
                    }
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(false);
                    localCheckBox4.setChecked(false);
                    localCheckBox5.setChecked(false);
                }
                else if ((paramAnonymousInt >= 37) && (paramAnonymousInt < 62))
                {
                    if(paramAnonymousInt!=50){
                            paramAnonymousSeekBar.setProgress(50);
                    }else{
                        return;
                    }
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(true);
                    localCheckBox4.setChecked(false);
                    localCheckBox5.setChecked(false);
                }
                else  if((paramAnonymousInt >= 62) && (paramAnonymousInt < 87))
                {
                    if(paramAnonymousInt!=75){
                            paramAnonymousSeekBar.setProgress(75);
                    }else{
                        return;
                    }
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(true);
                    localCheckBox4.setChecked(true);
                    localCheckBox5.setChecked(false);
                }else  if((paramAnonymousInt >= 87) && (paramAnonymousInt < 100))
                {
                    if(paramAnonymousInt!=100){
                            paramAnonymousSeekBar.setProgress(100);
                    }else{
                        return;
                    }
                    localCheckBox1.setChecked(true);
                    localCheckBox2.setChecked(true);
                    localCheckBox3.setChecked(true);
                    localCheckBox4.setChecked(true);
                    localCheckBox5.setChecked(true);
                }
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
            {

            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
            {

                if (paramAnonymousSeekBar.getProgress() < 12) {
                    BLWindAlert.mOnSelected = 1;
                    paramOnAlertSelectId.onClick(BLWindAlert.mOnSelected);
                }
                else if ((paramAnonymousSeekBar.getProgress() >= 12)
                        && (paramAnonymousSeekBar.getProgress() < 37)) {
                   
                    BLWindAlert.mOnSelected = 2;
                    paramOnAlertSelectId.onClick(BLWindAlert.mOnSelected);
                }
                else if ((paramAnonymousSeekBar.getProgress() >= 37)
                        && (paramAnonymousSeekBar.getProgress() < 62))
                {
                    
                    BLWindAlert.mOnSelected = 3;
                    paramOnAlertSelectId.onClick(BLWindAlert.mOnSelected);
                }
                else if((paramAnonymousSeekBar.getProgress() >= 62)
                        && (paramAnonymousSeekBar.getProgress() <87)){
                    
                    BLWindAlert.mOnSelected = 4;
                    paramOnAlertSelectId.onClick(BLWindAlert.mOnSelected);
                    
                } else if((paramAnonymousSeekBar.getProgress() >= 87)
                        && (paramAnonymousSeekBar.getProgress() <=100)){
                    
                    BLWindAlert.mOnSelected = 5;
                    paramOnAlertSelectId.onClick(BLWindAlert.mOnSelected);
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

    public static void onClick(int mOnSelected2) {
        onAlertSelectId.onClick(mOnSelected2);
    }

    public static OnAlertSelectId onAlertSelectId = null;

    public static abstract interface OnAlertSelectId
    {
        public abstract void onClick(int paramInt);
    }
}
