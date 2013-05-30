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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.appliedanalog.glass.disablecam.R;

public class MainActivity extends Activity implements SensorEventListener {

    private static String TAG = "MainActivity";
    
    public static final String OP = "aa_cambutton_op";
    public static final String STORED_ACTION = "aa_stored_action";
    public static final String CAM_INTERCEPT_OP = "camintercept";
    
	//The threshold for the y-axis gravity value after which we will intercept camera button calls
	final float Y_AXIS_GRAVITY_THRESH = -8.f;
    
    SensorManager senseMan;
    int buttonsPressed;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	Intent intent = getIntent();
    	if(intent != null && intent.hasExtra(OP) && intent.getStringExtra(OP).equals(CAM_INTERCEPT_OP)){
    		Log.v(TAG, "Cambuttonreceiver main activity started up to check the devices orientation.");
    		senseMan = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	    	Sensor sensor = senseMan.getDefaultSensor(Sensor.TYPE_GRAVITY);
	    	senseMan.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
	    	buttonsPressed++;
    	}else{
	    	//just exit.
    		Log.v(TAG, "DisableCam activity started in lame mode; exitting.");
	    	finish();
    	}
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
			try{
				
				if(buttonsPressed <= 0){
					return;
				}
				
				if(event.values[1] < Y_AXIS_GRAVITY_THRESH){
					buttonsPressed--; //thwart this press.
			        Log.v(TAG, "Cam button intercepted and thwarted. Have a happy life!");
				}else{
					buttonsPressed--; //allow this one through.
					Log.v(TAG, "This cam button press is fine, let it through..");
					//create a "Special" camera button intent that will pass through the onReceive filter above.
					Intent specIntent = new Intent(getIntent().getStringExtra(STORED_ACTION));
					specIntent.putExtras(getIntent().getExtras());
					specIntent.removeExtra(OP);
					specIntent.removeExtra(STORED_ACTION);
					specIntent.putExtra(CamButtonReceiver.EXTRA_BYPASS_CAMBUTTON_FILTER, true);
					sendBroadcast(specIntent);
				}
				
				//We don't need the grav sensor anymore.
				senseMan.unregisterListener(this);
				finish();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
