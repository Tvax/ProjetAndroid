package com.example.vanlife.util;

import android.content.Context;
import android.widget.Toast;

import com.example.vanlife.R;

/**
 *
 * Created by teiva on 17/03/18.
 */

public final class Display {
    public static void Toast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void StartupTips(Context applicationContext) {
        if (GatewayDatabase.getTips(applicationContext)) {
            Toast(applicationContext.getString(R.string.long_press_on_map_tip), applicationContext);
            Toast(applicationContext.getString(R.string.slide_right_tip), applicationContext);
        }
    }
}
