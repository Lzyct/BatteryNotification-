package com.tuxkids.batterynotification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Advance extends PreferenceActivity {
	
	CheckBoxPreference cb1;
	boolean fast;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.advance_screen);
        setChecked();
 
    }
	
	public void getChecked (){
		SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);
		
		cb1 = (CheckBoxPreference) findPreference("preffast");
		
		fast = sharedPrefs.getBoolean("preffast", false);		
	}

	public void setChecked(){
		getChecked();
		if (fast==true){
			cb1.setChecked(true);
		}
		else {
			cb1.setChecked(false);
		}
	}
}
