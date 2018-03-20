package com.example.vanlife.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 *
 * Created by teiva on 15/03/18.
 */

public final class Keyboard {
    private static final int HIDE_IMPLICIT_ONLY = 0;

    public static void close(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), HIDE_IMPLICIT_ONLY);
    }
}
