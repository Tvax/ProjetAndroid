package com.example.vanlife.util;

import android.content.Context;
import android.widget.Toast;

import com.example.vanlife.R;

/**
 * Class to display something on Toast
 *
 * @author tefleury, thpradier
 */

public final class Display {
    /**
     * Display message on context in Toast
     * 
     * @param message message to display
     * @param context application context
     */	
    public static void Toast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Display tips on context in Toast
     * 
     * @param applicationContext application context
     */	
    public static void StartupTips(Context applicationContext) {
        if (GatewayDatabase.getTips(applicationContext)) {
            Toast(applicationContext.getString(R.string.long_press_on_map_tip), applicationContext);
            Toast(applicationContext.getString(R.string.slide_right_tip), applicationContext);
        }
    }
}
