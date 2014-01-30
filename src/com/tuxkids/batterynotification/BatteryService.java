package com.tuxkids.batterynotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BatteryService extends Service{
    private static String TAG = "Battery Tools";
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.d(TAG, "BatteryService started");
        Intent dialogIntent = new Intent(getBaseContext(), MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.setAction(Intent.ACTION_BATTERY_CHANGED);
        getApplication().startActivity(dialogIntent);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "BatteryService destroyed");
    }
}