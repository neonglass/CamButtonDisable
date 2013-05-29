/*
    Cam Button Disabler - Disables Glass camera button when device is upside down.
    Copyright (C) 2013 James Betker

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.appliedanalog.glass.disablecam;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.appliedanalog.glass.disablecam.R;

public class MainActivity extends Activity {

    private static String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Permanently enable this receiver.
        Log.v(TAG, "Permanently disabling camera button when device is upside down. Uninstall app to undo this.");
        ComponentName component = new ComponentName(this, CamButtonReceiver.class);
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	//just exit.
    	finish();
    }

}
