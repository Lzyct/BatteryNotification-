package com.tuxkids.batterynotification;

import java.util.prefs.PreferenceChangeListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryService extends Service{
    private static String TAG = "BatteryNotification+";
    boolean low, full, charge, discharge;
	// boolean to fix always playing tone
	boolean a, b, c, d;
    
    General Gl = new General();
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @SuppressWarnings("deprecation")

    public void onCreate()
    {
    	IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryInfoReceiver, batteryLevelFilter);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky
		
        return START_STICKY;
    }
    
  
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    	unregisterReceiver(mBatteryInfoReceiver);
        super.onDestroy();
        Log.d(TAG, "BatteryService destroyed");
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
    
	public void getChecked() {

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);			

		low = sharedPrefs.getBoolean("preflow", false);
		full = sharedPrefs.getBoolean("preffull", false);
		charge = sharedPrefs.getBoolean("prefcharge", false);
		discharge = sharedPrefs.getBoolean("prefdischarge", false);		

	}
    
    

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
