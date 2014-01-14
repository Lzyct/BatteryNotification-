package com.tuxkids.batterynotification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Advance extends PreferenceActivity {
	
	CheckBoxPreference cb1;
	boolean fast;
	static CMDProcessor cmd = new CMDProcessor();
	private final static String fastcharge = "/sys/kernel/fast_charge/force_fast_charge";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.advance_screen);
        //set checked state
        setChecked();
        
        //set value fast charge
        setValue();
        
        
    }
	
	public void setValue(){
		//exec command
		final String enable  = "echo 1 > "+fastcharge;
		final String disable = "echo 0 > "+fastcharge;
		final String close = "exit";	
        
		//get on change checkbox preference
        super.findPreference("preffast").
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
	
	public void getChecked (){
		SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);		
		fast = sharedPrefs.getBoolean("preffast", false);		
	}

	public void setChecked(){
		getChecked();
		cb1 = (CheckBoxPreference)findPreference("preffast");
		String status = getCurrentStatus();
		String aktif = "1";
		String tdk="0";
		
		//check status by value on status
		if ((status.equals(aktif)) ){
			fast=true;
		}
		else if ((status.equals(tdk)) ){
			fast=false;
		}
		
		//after get status then execute by fast value
		if (fast==true){
			cb1.setChecked(true);
		}
		else if (fast==false) {
			cb1.setChecked(false);
		}
	}

	
	public void checkFastSupport(){
		File myFile = new File(fastcharge);
		
		if (myFile.exists()) {
			}
		else {
				alert();
				cb1.setEnabled(false);
			}
		}
	
	public static String getCurrentStatus() {
		return cmd.readString(fastcharge);
	}

	
	public void alert (){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false)
		       .setTitle("Alert")
		       .setMessage(
		    		"Your Kernel Not Support Fast Charge\n" +
		    		"Make sure you're kernel have fast charge future"+
		    		"to use this option")
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		}
	}
