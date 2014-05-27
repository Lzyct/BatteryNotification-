package com.tuxkids.batterynotification;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.settings_screen);
		setContentView(R.layout.ad_layout);
		sendValue();
	}
	public void onResume(){
		super.onResume();
		sendValue();
	}
	
	public void sendValue(){
		ListPreference low_level = (ListPreference) findPreference ("value_battery_low");
		String x = low_level.getEntry().toString();
		low_level.setSummary(x);
		low_level.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				
				String nilai = newValue.toString();
				SharedPreferences prefs = getSharedPreferences("low_level", MODE_PRIVATE);
				Editor mEditor = prefs.edit();
				mEditor.putString("value_battery_low", nilai);
				mEditor.commit();
				preference.setSummary(nilai+" Percent");
				return true;
			}
        });		
		
		}
}
