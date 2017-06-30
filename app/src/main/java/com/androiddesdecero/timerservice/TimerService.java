package com.androiddesdecero.timerservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class TimerService extends Service {

    private static final String TAG = "TimerService";
    private static final int COUNT_NOTIFICATION_ID  = 1111;
    public static final String INTENT_RECEIVER = "TIMER";
    public static final String INTENT_DATA_TIME = "TIEMPO";
    private Intent intent;
    private int time;
    private NotificationCompat.Builder notifiacionBuilder;
    private NotificationManager notificationManager;
    private boolean checkTimer;
    private Thread thread;

    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i (TAG,"onCreate");
        checkTimer = true;
        intent = new Intent(INTENT_RECEIVER);
        time = 20;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i (TAG,"onStartCommand");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(time>0){
                    if(checkTimer==true){
                        try{
                            TimeUnit.SECONDS.sleep(1);
                        }catch (Exception e){

                        }
                        time= time-1;
                        updateUI(time);
                        Log.i (TAG,"onStartCommand: "+time);
                    }else{
                        break;
                    }
                }
                Log.i (TAG,"hilo secundadio ha finalizado");
                showNotificacion();
                stopSelf();
            }
        };
        thread = new Thread(runnable);
        thread.start();
        return Service.START_STICKY;
    }

    private void updateUI(int time){
        intent.putExtra(INTENT_DATA_TIME, String.valueOf(time));
        sendBroadcast(intent);
    }

    private void showNotificacion(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifiacionBuilder = new NotificationCompat.Builder(TimerService.this)
                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                .setContentText("ya has descansado, Anda 10 Minutos")
                .setContentTitle("Timer Aplication")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                COUNT_NOTIFICATION_ID,
                notifiacionBuilder.build()
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
        checkTimer=false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
