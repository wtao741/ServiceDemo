package com.android.servicetest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Ling、 on 2016/9/26.
 */
public class ServiceTest extends Service {

    private final static int GRAY_SERVICE_ID = 1001;

    /**
     * 定时唤醒的时间间隔，5分钟
     */
    private final static int ALARM_INTERVAL = 3 * 60 * 1000;
    private final static int WAKE_REQUEST_CODE = 6666;

    int i = 0;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("tag","i = "+(i++));
            mHandler.sendEmptyMessageDelayed(1,1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mHandler.sendEmptyMessageDelayed(1,0);
        }catch (Exception e){

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("tag",""+Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

//        //发送唤醒广播来促使挂掉的UI进程重新启动起来
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent alarmIntent = new Intent();
//        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
//        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, operation);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        private static final String TAG = "GrayInnerService";

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Log.e(TAG, "onCreate: ");
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e(TAG, "onStartCommand: ");
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public boolean stopService(Intent name) {
            Log.e(TAG,"stopService");
            return super.stopService(name);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.e(TAG,"onDestroy");
        }
    }
}


