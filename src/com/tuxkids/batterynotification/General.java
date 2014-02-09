package com.tuxkids.batterynotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
	boolean a, b, c, d,register;

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryInfoReceiver, batteryLevelFilter);
		register=true;
	//		getBatteryInformation();
	}
	
	public void onDestroy(){
		super.onDestroy();
		if (register==true){
		unregisterReceiver(mBatteryInfoReceiver);
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_screen);
		if (register==true){
			unregisterReceiver(mBatteryInfoReceiver);
			}
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

//	// Set ringtone berdasarkan statusnya
//
//	private void getBatteryInformation() {

		BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

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
					if (low == true && battery_level <= 12 && statusString == 2) {
						if (d == false) {
							setRingtoneLow();
							CharSequence text4 = "Battery is Low\n"
									+ "Please charge your phone";
							int duration = Toast.LENGTH_LONG;
							Toast toast = Toast.makeText(context, text4,
									duration);
							toast.show();
							d = true;
						}
					}
					if (full == true && statusString == 4) {
						if (c == false) {
							setRingtoneFull();
							tampilNotifikasi();
							c = true;
						}
					}
				}
			}
		};
		
//		if (register ==false){
//				IntentFilter batteryLevelFilter = new IntentFilter(
//						Intent.ACTION_BATTERY_CHANGED);
//				registerReceiver(mBatteryInfoReceiver, batteryLevelFilter);
//				register=true;
//			}
//		else if (register==true){
//			unregisterReceiver(mBatteryInfoReceiver);
//			register=false;
//		}
		
//	}

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

	public void tampilNotifikasi() {

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Battery Full", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "Battery Full",
				"Swipe to remove notification", activity);
		notificationManager.notify(0, notification);
	}

}
