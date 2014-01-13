package com.tuxkids.batterynotification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class General extends PreferenceActivity {
	boolean low,full,charge,discharge;
	CheckBoxPreference cb1,cb2,cb3,cb4;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.general_screen);
        setChecked();
 
    }
	
	public void getChecked (){
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
	
	public void setChecked(){
		getChecked();
		
		if (low==true){
			cb1.setChecked(true);
		}
		else {
			cb1.setChecked(false);
		}
		
		if (full==true){
			cb2.setChecked(true);
		}
		else {
			cb2.setChecked(false);
		}
		
		if (charge == true){
			cb3.setChecked(true);
		}
		else {
			cb3.setChecked(false);
		}
		
		if (discharge==true){
			cb4.setChecked(true);
		}
		else {
			cb4.setChecked(false);
		}
	}
}
