package com.android.settings.setupwizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = BootReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && needToRunSetup(context) ) {
			Log.d(TAG, "android.intent.action.BOOT_COMPLETED");
			Log.d(TAG, "Starting Wizard");
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, SetupWizard.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
	}
	
	private boolean needToRunSetup(Context context){

		int value = Settings.System.getInt(context.getContentResolver(), 
                "run_setup_wizard", 1);
		
		return (value == 1);
	}

}
