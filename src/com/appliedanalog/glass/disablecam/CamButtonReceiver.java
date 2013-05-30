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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CamButtonReceiver extends BroadcastReceiver {
	final String TAG = "CamButtonInterceptor";
	
	
	//Special bypass intent extra
	public static final String EXTRA_BYPASS_CAMBUTTON_FILTER = "CamButtonFilterBypass";

    @Override
    public void onReceive(Context context, Intent intent) {
    	if(intent.getExtras() != null &&
    	   intent.getExtras().getBoolean(EXTRA_BYPASS_CAMBUTTON_FILTER, false)){
    		Log.v(TAG, "Got an accepted cam press, let it through!");
    		return;
    	}
    	
    	try{
	    	Log.v(TAG, "Intercepted: " + intent.getAction());
	        abortBroadcast(); //Although this is not actually an ordered broadcast, calling this method actually cancels it nonetheless!
	        				  //it's messy though; exceptions are thrown. Don't expect this to work forever.
	    	
    	}catch(Exception e){
    		//e.printStackTrace();
    	}

    	//Let the main activity handle this. The problem is that we cannot launch the sensor manager in this context without
		//leaking something.
    	Intent handlerIntent = new Intent(context, MainActivity.class);
    	if(intent.getExtras() != null){
    		handlerIntent.putExtras(intent.getExtras());
    	}
    	handlerIntent.putExtra(MainActivity.OP, MainActivity.CAM_INTERCEPT_OP);
    	handlerIntent.putExtra(MainActivity.STORED_ACTION, intent.getAction());
    	handlerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(handlerIntent);
    }

}
