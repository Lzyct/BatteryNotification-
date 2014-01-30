package com.tuxkids.batterynotification;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class General extends PreferenceActivity {
	boolean low, full, charge, discharge;
	CheckBoxPreference cb1, cb2, cb3, cb4;
	
	
	//boolean to fix always playing tone
	boolean a, b, c, d;

	@Override
	public void onResume() {
		super.onResume();
		BatteryInformation();
	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.general_screen);
	}

	//mendapatkan status checkbox
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

	// Set ringtone berdasarkan statusnya

	private void BatteryInformation() {

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
					
					if (low==true && battery_level <= 12){
							if (d==false){
								setRingtoneLow();
								CharSequence text4 = "Battery is Low\n"+
								 "Please charge your phone";
								int duration = Toast.LENGTH_LONG;
								Toast toast = Toast.makeText(context, text4, duration);
								toast.show();
							}
					}

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
					// mBatteryStatus.setSummary(String.valueOf(statusString));
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
					} else if (discharge == true && statusString == 2) {
						if (b == false){
							setRingtoneDischarge();
							CharSequence text2 = "Battery is Discharge";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text2, duration);
							toast.show();
							a = false;
							b = true;
//							c = false;
						}
					}
					   if (full==true && statusString == 4){
						   if (c==false){
							setRingtoneFull();
							CharSequence text3 = "Battery is Full\n"+
												 "Please unplug your charger";
							int duration = Toast.LENGTH_LONG;
							Toast toast = Toast.makeText(context, text3, duration);
							toast.show();
							c=true;
						   }
						}
				}
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryInfoReceiver, batteryLevelFilter);
		
	}

	//ringtone low
	public void setRingtoneLow() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("low_sound",
				"DEFAULT_RINGTONE_URI");
		if (ringtonePreference !=null){
			Uri charge = Uri.parse(ringtonePreference);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					charge);
			r.play();
			}
	}
	
	//ringtone full
	public void setRingtoneFull() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("full_sound",
				"DEFAULT_RINGTONE_URI");
		if (ringtonePreference !=null){
			Uri charge = Uri.parse(ringtonePreference);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					charge);
			r.play();
			}
	}
	
	//ringtone charge
	public void setRingtoneCharge() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("charge_sound",
				"DEFAULT_RINGTONE_URI");
		if (ringtonePreference !=null){
			Uri charge = Uri.parse(ringtonePreference);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					charge);
			r.play();
			}
	}
	
	//ringtone discharge
	public void setRingtoneDischarge() {
		String ringtonePreference;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		ringtonePreference = prefs.getString("discharge_sound",
				"DEFAULT_RINGTONE_URI");
		if (ringtonePreference !=null){
		Uri charge = Uri.parse(ringtonePreference);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
				charge);
		r.play();
		}
	}	
}
