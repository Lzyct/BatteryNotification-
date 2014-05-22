package com.tuxkids.batterynotification;

import java.util.prefs.PreferenceChangeListener;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class General extends PreferenceActivity {
	boolean low, full, charge, discharge;
	CheckBoxPreference cb1, cb2, cb3, cb4;
		
	// boolean to fix always playing tone
	boolean a, b, c, d,register,sambut;

	
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryInfoReceiver, batteryLevelFilter);
		
		register=true;
	}
	
	public void onDestroy(){
		super.onDestroy();
		if (register==true){
		unregisterReceiver(mBatteryInfoReceiver);
//		getBaseContext().unregisterReceiver(mIntentReceiver);
		}
	}
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_screen);
		//setContentView(R.layout.ad_layout);
		
		//set sambutan just show once when first application created
		SharedPreferences prefs = getSharedPreferences("sambutan", MODE_PRIVATE);
		Editor mEditor = prefs.edit();
		boolean sambutan= prefs.getBoolean("sambutan",false);
		if (sambutan == false){
			sambutan();
			sambut=true;
			mEditor.putBoolean("sambutan",sambut);
			mEditor.commit();
		}
		//cek register mBatteryInfoReceiver
		if (register==true){
			unregisterReceiver(mBatteryInfoReceiver);
//			getBaseContext().unregisterReceiver(mIntentReceiver);
			}
		moveTaskToBack(true);		
	}

	//First Notif
	public void sambutan(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true)
					.setTitle("Tips")
					.setMessage("[*]Press back button when you will close this app\n"+
								"[*]Need Restart Your Phone for Run Application Service")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					
			AlertDialog alert = builder.create();
			alert.show();
		
	}

	
	// mendapatkan status checkbox
	public void getChecked() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		cb1 = (CheckBoxPreference) findPreference("preflow");
		cb2 = (CheckBoxPreference) findPreference("preffull");
		cb3 = (CheckBoxPreference) findPreference("prefcharge");
		cb4 = (CheckBoxPreference) findPreference("prefdischarge");
		


		low = sharedPrefs.getBoolean("preflow", false);
		full = sharedPrefs.getBoolean("preffull", false);
		charge = sharedPrefs.getBoolean("prefcharge", false);
		discharge = sharedPrefs.getBoolean("prefdischarge", false);

		

	}

		BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				
				//get value low_level from Settings
				PreferenceChangeListener mPreferenceListener = null;
				SharedPreferences prefs = getSharedPreferences("low_level", Context.MODE_PRIVATE);
				prefs.registerOnSharedPreferenceChangeListener((OnSharedPreferenceChangeListener) mPreferenceListener);				
				String low_battery_level= prefs.getString("value_battery_low", null);

				
				
				if (intent.ACTION_BATTERY_CHANGED.equals(action)) {
										
					// get battery level
					int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,
							0);
					int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,
							100);
					int battery_level = level * 100 / scale;

					// get status
					int status = intent.getIntExtra(
							BatteryManager.EXTRA_STATUS, 0);
					int statusString;

					if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
						statusString = 1;
					} else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
						statusString = 2;
					} else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
						statusString = 3;
					} else if (status == BatteryManager.BATTERY_STATUS_FULL) {
						statusString = 4;
					} else {
						statusString = 5;
					}
					getChecked();

					if (charge == true && statusString == 1) {
						if (a == false) {
							setRingtoneCharge();
							CharSequence text1 = "Battery is Charge";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text1,
									duration);
							toast.show();
							a = true;
							b = false;
							d = false;
						}
					}
					if (discharge == true && statusString == 2) {
						if (b == false) {
							setRingtoneDischarge();
							CharSequence text2 = "Battery is Discharge";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text2,
									duration);
							toast.show();
							b = true;
							a = false;
							c = false;
						}
					}
					
					if (low_battery_level == null){
					int low_level=10;
			            if (low == true && battery_level <= low_level && statusString == 2) {
							if (d == false) {
								setRingtoneLow();
								tampilNotifikasiLow();
								d = true;
							
								}
			            	}
					} else {
						int low_level = Integer.parseInt(low_battery_level);
						if (low == true && battery_level <= low_level && statusString == 2) {
							if (d == false) {
								setRingtoneLow();
								tampilNotifikasiLow();
								d = true;
							
								}
			            	}
					}
					if (full == true && statusString == 4) {
						if (c == false) {
							setRingtoneFull();
							tampilNotifikasiFull();
							c = true;
						}
					}
				}
			}
		};

	// ringtone low
	public void setRingtoneLow() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("low_sound",
				"DEFAULT_RINGTONE_URI");
		String dR = "DEFAULT_RINGTONE_URI";
		if (!(ringtonePreference.equals(dR))) {
			if (ringtonePreference != null) {
				Uri charge = Uri.parse(ringtonePreference);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), charge);
				r.play();
			}
		}

	}

	// ringtone full
	public void setRingtoneFull() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("full_sound",
				"DEFAULT_RINGTONE_URI");
		String dR = "DEFAULT_RINGTONE_URI";
		if (!(ringtonePreference.equals(dR))) {
			if (ringtonePreference != null) {
				Uri charge = Uri.parse(ringtonePreference);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), charge);
				r.play();
			}
		}

	}

	// ringtone charge
	public void setRingtoneCharge() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("charge_sound",
				"DEFAULT_RINGTONE_URI");
		String dR = "DEFAULT_RINGTONE_URI";
		if (!(ringtonePreference.equals(dR))) {
			if (ringtonePreference != null) {
				Uri charge = Uri.parse(ringtonePreference);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), charge);
				r.play();
			}
		}

	}

	// ringtone discharge
	public void setRingtoneDischarge() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("discharge_sound",
				"DEFAULT_RINGTONE_URI");
		String dR = "DEFAULT_RINGTONE_URI";
		if (!(ringtonePreference.equals(dR))) {
			if (ringtonePreference != null) {
				Uri charge = Uri.parse(ringtonePreference);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), charge);
				r.play();
			}
		}
	}

	//Notification Full
	public void tampilNotifikasiFull() {

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Battery Full", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(this, Advance.class);
		PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "Battery Full",
				"Touch to see Battery Information", activity);
		notificationManager.notify(0, notification);
	}
	
	//Notification Low
	public void tampilNotifikasiLow() {

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Battery Low", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(this, Advance.class);
		PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "Battery Low",
				"Touch to see Battery Information", activity);
		notificationManager.notify(0, notification);
	}

}
