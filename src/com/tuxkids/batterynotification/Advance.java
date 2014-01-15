package com.tuxkids.batterynotification;


import java.io.File;

import android.R.string;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

public class Advance extends PreferenceActivity  {
	
	
	static CMDProcessor cmd = new CMDProcessor();
	private final static String fastcharge = "/sys/kernel/fast_charge/force_fast_charge";
	final String close = "exit";
	
	

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advance_screen);
    	
        
        BatteryInformation();
        //set checked state #fastcharge
        setChecked();
        checkFastSupport();    
        
        //battery calibration
        calibration();
    }

	
	//Battery Information
	/**
     * Format a number of tenths-units as a decimal string without using a
     * conversion to float.  E.g. 347 -> "34.7"
     */
    private final String tenthsToFixedString(int x) {
        int tens = x / 10;
        return Integer.toString(tens) + "." + (x - 10 * tens);
    }

   
  private void BatteryInformation (){
	 
	  BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
		//deklarasi preference
	    	Preference mBatteryStatus = (Preference)findPreference("status");
	    	Preference mBatteryPlug = (Preference)findPreference("plug");
	    	Preference mBatteryLevel = (Preference)findPreference("level");
	    	Preference mBatteryHealth = (Preference)findPreference("health");
	    	Preference mBatteryVoltage = (Preference)findPreference("voltage");
	    	Preference mBatteryTemp = (Preference)findPreference("temp");
	    	Preference mBatteryTechnology = (Preference)findPreference("tekno");
	    	Preference mBatteryTime = (Preference)findPreference("time");
	    	
	    	
	    	
	            public void onReceive(Context context, Intent intent) {
	                context.unregisterReceiver(this);
		        	String action = intent.getAction();
		        	if (intent.ACTION_BATTERY_CHANGED.equals(action)) {
		            	
	            	//get battery level
	                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
	                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
	                mBatteryLevel.setSummary(String.valueOf(level * 100 / scale) + "%");
	                 
	                //get voltage
	                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
	                mBatteryVoltage.setSummary(String.valueOf(voltage));
	         
	                //get temperature
	                int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
	                String ftemp = tenthsToFixedString(temp);
	                mBatteryTemp.setSummary(String.valueOf(ftemp + getString(R.string.battery_info_temperature_units)));
	                
	                //get technology
	                String tekno = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
	                mBatteryTechnology.setSummary(String.valueOf(tekno));
	                
	                //get time boot
	                long uptime = SystemClock.elapsedRealtime();
	                mBatteryTime.setSummary(String.valueOf(DateUtils.formatElapsedTime(uptime / 1000)));
	                
	                //get plugType
	             
	                int plugType = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
	                String Tipe;
	                switch (plugType) {
	                    case 0:
	                    	Tipe = getString(R.string.battery_info_power_unplugged);
	                        break;
	                    case BatteryManager.BATTERY_PLUGGED_AC:
	                    	Tipe = getString(R.string.battery_info_power_ac);
	                        break;
	                    case BatteryManager.BATTERY_PLUGGED_USB:
	                        Tipe = getString(R.string.battery_info_power_usb);
	                    	 break;
	                    case (BatteryManager.BATTERY_PLUGGED_AC|BatteryManager.BATTERY_PLUGGED_USB):
	                        Tipe = getString(R.string.battery_info_power_ac_usb);
	                    	break;
	                    default:
	                    	Tipe = getString(R.string.battery_info_power_unknown);
	                         break;
	                }
	                mBatteryPlug.setSummary(String.valueOf(Tipe));
	                
	                //get health
	                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
	                String healthString;
	                if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
	                    healthString = getString(R.string.battery_info_health_good);
	                } else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
	                    healthString = getString(R.string.battery_info_health_overheat);
	                } else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
	                    healthString = getString(R.string.battery_info_health_dead);
	                } else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
	                    healthString = getString(R.string.battery_info_health_over_voltage);
	                } else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
	                    healthString = getString(R.string.battery_info_health_unspecified_failure);
	                } else {
	                    healthString = getString(R.string.battery_info_health_unknown);
	                }
	                mBatteryHealth.setSummary(String.valueOf(healthString));
	          
	                //get status
	                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
	                String statusString;
	                
	                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
	                    statusString = getString(R.string.battery_info_status_charging);
	                } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
	                    statusString = getString(R.string.battery_info_status_discharging);
	                } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
	                    statusString = getString(R.string.battery_info_status_not_charging);
	                } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
	                    statusString = getString(R.string.battery_info_status_full);
	                } else {
	                    statusString = getString(R.string.battery_info_status_unknown);
	                }
	                mBatteryStatus.setSummary(String.valueOf(statusString));
	            }
	         }
	    };
	    IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    registerReceiver(mBatteryInfoReceiver, batteryLevelFilter);
  }
	//FastCharge
	public void setValue(){
		//exec command
		final String enable  = "echo 1 > "+fastcharge;
		final String disable = "echo 0 > "+fastcharge;	
        
		//get on change checkbox preference
		CheckBoxPreference cb1 = (CheckBoxPreference) findPreference("preffast");
		cb1.
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
		String status = getCurrentStatus();
		String aktif = "1";
		String tdk="0";
		CheckBoxPreference cb1 = (CheckBoxPreference) findPreference("preffast");
		//set checked by value fast charge
		if ((status.equals(aktif)) ){
			cb1.setChecked(true);
		}
		else if ((status.equals(tdk)) ){
			cb1.setChecked(false);
		}
	}

	
	public void checkFastSupport(){
		CheckBoxPreference cb1 = (CheckBoxPreference) findPreference("preffast");
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

    	Preference mKalibrasi = (Preference)findPreference("kalibrasi");
		mKalibrasi.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
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
