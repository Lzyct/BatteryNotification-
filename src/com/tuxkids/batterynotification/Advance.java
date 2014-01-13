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
        setChecked();
        	
    }
	
	public void getChecked (){
		SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);		
		fast = sharedPrefs.getBoolean("preffast", false);		
	}

	public void setChecked(){
		getChecked();
		cb1 = (CheckBoxPreference)findPreference("preffast");
		String enable  = "echo 1 > "+fastcharge;
		String disable = "echo 0 > "+fastcharge;
		String close = "exit";	
		String status = getCurrentStatus();
		if ((status.equals("1")) ){
			cb1.setChecked(true);
			fast=true;
		}
		else if ((status.equals("0")) ){
			cb1.setChecked(true);
			fast=false;
		}
		else if (fast==true){
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
