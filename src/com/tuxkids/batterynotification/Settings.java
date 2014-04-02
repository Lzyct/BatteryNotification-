package com.tuxkids.batterynotification;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

	public String nilai="unknown";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings_screen);
	}
	public void onResume(){
		super.onResume();
	}
	
	public void getValue(String nilai){
		SharedPreferences sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this);
		nilai = sharedPrefs.getString("value_battery_low", "NULL");
		//send broadcast to General.java
		getBaseContext().sendBroadcast(new Intent().setAction("update.NILAI").putExtra("Nilai", nilai));
	}
	
	public String getNilai(){
    	return nilai;
    	}
}
