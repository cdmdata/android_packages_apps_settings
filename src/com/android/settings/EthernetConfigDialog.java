/*
 * Copyright (C) 2010 The Android-x86 Open Source Project
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
 *
 * Author: Yi Sun <beyounn@gmail.com>
 */

package com.android.settings;


import java.util.List;

//import com.android.settings.R;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
//import android.net.EthernetManager;
//import android.net.EthernetDevInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

public class EthernetConfigDialog extends AlertDialog implements
        DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

	private static final String TAG = "ETHERNET_SETTING_DIALOG";
	
	private Context context;
    private EthernetEnabler mEthEnabler;
    private View mView;
//    private Spinner mDevList;
//    private TextView mDevs;
    private RadioButton mConTypeDhcp;
    private RadioButton mConTypeManual;
    private EditText mIpaddr;
    private EditText mDns;
    private EditText mGw;
    private EditText mMask;
//
//    private EthernetLayer mEthLayer;
//    private EthernetManager mEthManager;
//    private EthernetDevInfo mEthInfo;
//    private boolean mEnablePending;
//
    public EthernetConfigDialog(Context context, EthernetEnabler Enabler) {
        super(context);
        //mEthLayer = new EthernetLayer(this);
        mEthEnabler = Enabler;
//        mEthManager=Enabler.getManager();
        buildDialogContent(context);
        this.context = context;
    }

    public int buildDialogContent(Context context) {
        this.setTitle(R.string.eth_config_title);
        this.setView(mView = getLayoutInflater().inflate(R.layout.eth_configure, null));
//        mDevs = (TextView) mView.findViewById(R.id.eth_dev_list_text);
//        mDevList = (Spinner) mView.findViewById(R.id.eth_dev_spinner);
        mConTypeDhcp = (RadioButton) mView.findViewById(R.id.dhcp_radio);
        mConTypeManual = (RadioButton) mView.findViewById(R.id.manual_radio);
        mIpaddr = (EditText)mView.findViewById(R.id.ipaddr_edit);
        mMask = (EditText)mView.findViewById(R.id.netmask_edit);
        mDns = (EditText)mView.findViewById(R.id.eth_dns_edit);
        mGw = (EditText)mView.findViewById(R.id.eth_gw_edit);

        Log.w(TAG, "Use static: " + Settings.System.getInt(context.getContentResolver(), Settings.System.ETHERNET_USE_STATIC_IP, 0));
        boolean useStatic = (Settings.System.getInt(context.getContentResolver(), Settings.System.ETHERNET_USE_STATIC_IP, 0) == 1);
        if (useStatic) {
        	mConTypeDhcp.setChecked(false);
        	mConTypeManual.setChecked(true);
        	
        	mIpaddr.setText(Settings.System.getString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_IP));
			mGw.setText(Settings.System.getString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_GATEWAY));
			mDns.setText(Settings.System.getString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_DNS1));
			mMask.setText(Settings.System.getString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_NETMASK));
			
        }
        else{
        	mConTypeDhcp.setChecked(true);
        	mConTypeManual.setChecked(false);
        	
        	

        }
        
        mIpaddr.setEnabled(useStatic);
        mMask.setEnabled(useStatic);
        mDns.setEnabled(useStatic);
        mGw.setEnabled(useStatic);
        
        mConTypeManual.setOnClickListener(new RadioButton.OnClickListener() {
            public void onClick(View v) {
                mIpaddr.setEnabled(true);
                mDns.setEnabled(true);
                mGw.setEnabled(true);
                mMask.setEnabled(true);
            }
        });

        mConTypeDhcp.setOnClickListener(new RadioButton.OnClickListener() {
            public void onClick(View v) {
                mIpaddr.setEnabled(false);
                mDns.setEnabled(false);
                mGw.setEnabled(false);
                mMask.setEnabled(false);
            }
        });

        this.setInverseBackgroundForced(true);
        this.setButton(BUTTON_POSITIVE, context.getText(R.string.menu_save), this);
        this.setButton(BUTTON_NEGATIVE, context.getText(R.string.menu_cancel), this);
//        String[] Devs = mEthEnabler.getManager().getDeviceNameList();
//        if (Devs != null) {
//            if (localLOGV)
//                Slog.v(TAG, "found device: " + Devs[0]);
//            updateDevNameList(Devs);
//            if (mEthManager.isConfigured()) {
//                mEthInfo = mEthManager.getSavedConfig();
//                for (int i = 0 ; i < Devs.length; i++) {
//                    if (Devs[i].equals(mEthInfo.getIfName())) {
//                        mDevList.setSelection(i);
//                        break;
//                    }
//                }
//                mIpaddr.setText(mEthInfo.getIpAddress());
//                mGw.setText(mEthInfo.getRouteAddr());
//                mDns.setText(mEthInfo.getDnsAddr());
//                mMask.setText(mEthInfo.getNetMask());
//                if (mEthInfo.getConnectMode().equals(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP)) {
//                    mIpaddr.setEnabled(false);
//                    mDns.setEnabled(false);
//                    mGw.setEnabled(false);
//                    mMask.setEnabled(false);
//                } else {
//                    mConTypeDhcp.setChecked(false);
//                    mConTypeManual.setChecked(true);
//                    mIpaddr.setEnabled(true);
//                    mDns.setEnabled(true);
//                    mGw.setEnabled(true);
//                    mMask.setEnabled(true);
//                }
//            }
//        }
        return 0;
    }
//
    private void handle_saveconf() {
        if (mConTypeDhcp.isChecked()) {
        	Settings.System.putInt(context.getContentResolver(), Settings.System.ETHERNET_USE_STATIC_IP, 0);
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_IP, null);
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_GATEWAY, null);
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_DNS1, null);
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_NETMASK, null);
        } else {
        	Settings.System.putInt(context.getContentResolver(), Settings.System.ETHERNET_USE_STATIC_IP, 1);
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_IP, mIpaddr.getText().toString());
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_GATEWAY, mGw.getText().toString());
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_DNS1, mDns.getText().toString());
            Settings.System.putString(context.getContentResolver(), Settings.System.ETHERNET_STATIC_NETMASK, mMask.getText().toString());
        }
        
        //mEthManager.updateDevInfo(info);
//        if (mEnablePending) {
//            mEthManager.setEnabled(true);
//            mEnablePending = false;
//        }
        
        
    }

//
//
//    public void updateDevNameList(String[] DevList) {
//        if (DevList != null) {
//            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
//                    getContext(), android.R.layout.simple_spinner_item, DevList);
//            adapter.setDropDownViewResource(
//                    android.R.layout.simple_spinner_dropdown_item);
//            mDevList.setAdapter(adapter);
//        }
//
//    }
//
//    public void enableAfterConfig() {
//        mEnablePending = true;
//    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case BUTTON_POSITIVE:
			handle_saveconf();
			break;
		case BUTTON_NEGATIVE:
			// Don't need to do anything
			break;
		}
		
	}
}
