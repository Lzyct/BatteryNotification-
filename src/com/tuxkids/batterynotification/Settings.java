package com.tuxkids.batterynotification;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Settings extends PreferenceActivity {
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.settings_screen);
		sendValue();
	}
	public void onResume(){
		super.onResume();
		sendValue();
	}
	public void onPause(){
		super.onPause();
	}
	
	public void sendValue(){
		ListPreference low_level = (ListPreference) findPreference ("value_battery_low");
		low_level.setSummary(low_level.getEntry());
		low_level.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				
				String nilai = (String) newValue;
				SharedPreferences prefs = getSharedPreferences("low_level", MODE_PRIVATE);
				Editor mEditor = prefs.edit();
				mEditor.putString("value_battery_low", nilai);
				mEditor.commit();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(getBaseContext(), "nilai : "+nilai,
						duration);
				toast.show();
				preference.setSummary(nilai+"%");
				return true;
			}
        });		
		
		}
}
