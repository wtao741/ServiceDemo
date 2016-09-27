package com.android.servicetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Ling、 on 2016/9/27.
 */
public class OpenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("tag","OpenActivity onCreate");

        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 1;
        params.height = 1;
        params.x = 1;
        params.y = 1;
        window.setAttributes(params);

        ScreenListener l = new ScreenListener(this);
        l.begin(new ScreenListener.ScreenStateListener() {

            Intent mIntent;

            @Override
            public void onUserPresent() {
                Log.e("onUserPresent", "onUserPresent");
            }

            @Override
            public void onScreenOn() {
                finish();
                Log.e("tag", "OpenActivity onScreenOn");
            }

            @Override
            public void onScreenOff() {
                Log.e("tag", "OpenActivity onScreenOff");
            }
        });
    }
}
