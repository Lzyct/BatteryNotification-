package com.tuxkids.batterynotification;


import java.io.File;

import android.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class Advance extends PreferenceActivity {
	
	
	CheckBoxPreference cb1;
	boolean test = false;
	static CMDProcessor cmd = new CMDProcessor();
	private final static String fastcharge = "/sys/kernel/fast_charge/force_fast_charge";
	final String close = "exit";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advance_screen);
        
        //set checked state #fastcharge
        setChecked();
        checkFastSupport();    
        
        //battery calibration
        calibration();
    }
	
	//FastCharge
	public void setValue(){
		//exec command
		final String enable  = "echo 1 > "+fastcharge;
		final String disable = "echo 0 > "+fastcharge;	
        
		//get on change checkbox preference
        findPreference("preffast").
        setOnPreferenceChangeListener( new OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object val) {
				Boolean checkBoxVal = (Boolean) val;
				if(checkBoxVal.booleanValue()==true) {
                    cmd.execCommand(enable);
                    cmd.execCommand(close);
                }
				else {
					cmd.execCommand(disable);
                    cmd.execCommand(close);
				}
				// TODO Auto-generated method stub
				return true;
			}
        });
        	
	}
	
	

	public void setChecked(){
		cb1 = (CheckBoxPreference) findPreference("preffast");
		String status = getCurrentStatus();
		String aktif = "1";
		String tdk="0";
		
		//set checked by value fast charge
		if ((status.equals(aktif)) ){
			cb1.setChecked(true);
		}
		else if ((status.equals(tdk)) ){
			cb1.setChecked(false);
		}
	}

	
	public void checkFastSupport(){
		File myFile = new File(fastcharge);
		if (myFile.exists()) {
			//set value fast charge
	        setValue();
			}
		else {
				cb1.setEnabled(false);
				cb1.setSummary("You're kernel not support fast charge");
			}
		}
	
	public static String getCurrentStatus() {
		return cmd.readString(fastcharge);
	}

	//Battery Calibration
	public void calibration(){
		Preference kalibrasi = findPreference("kalibrasi");
		kalibrasi.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				CalibrationAlert();
				return false;
			}
		});
	}
	
	public void CalibrationAlert(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true)
			       .setTitle("Alert")
			       .setMessage(
			    		"Are you sure to calibration your battery ?")
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               //do calibration 
			        	   String kalibrasi = "rm /data/system/batterystats.bin";
			                cmd.execCommand(kalibrasi);
			                cmd.execCommand(close);
			                
			              //show toast notification
			              //show message error when kernel not support
							Context context = getApplicationContext();
							CharSequence text1 = "/data/system/batterystats.bin was removed";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text1, duration);
							toast.show();
			                dialog.cancel();
			           }
			       })
			       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
}
