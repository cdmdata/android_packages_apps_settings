/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.setupwizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.android.settings.R;
import com.android.settings.WirelessSettings;

public class NetworkSetupWizard extends WirelessSettings implements OnClickListener {
    private View mNextButton;
    private View mPrevButton;
    
    @Override
    protected void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        super.onCreate(icicle);
        setContentView(R.layout.network_settings_setupwizard);
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(this);
        
        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(this);
    }

	public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.next_button:
			finishAndGoHome();
			break;
			
		case R.id.prev_button:
			onBackPressed();
			break;

		default:
			break;
		}
        
    }

	private void finishAndGoHome() {
		setDontRunAgain();
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

	private void setDontRunAgain() {
		Settings.System.putInt(getContentResolver(), 
                "run_setup_wizard", 0);
		
	}
}
