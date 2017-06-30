package com.androiddesdecero.timerservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.androiddesdecero.timerservice.TimerService.INTENT_DATA_TIME;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private Intent intent;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init(){
        textView = (TextView)findViewById(R.id.tvTimer);
        button = (Button)findViewById(R.id.btTimer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), TimerService.class);
                stopService(intent);
                startService(intent);
            }
        });
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                textView.setText(intent.getStringExtra(INTENT_DATA_TIME));
            }
        };
    }
    //Override onResume and setup your broadcast receiver.
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter(TimerService.INTENT_RECEIVER));
    }
    //Override onPause and unregister your receiver using the unregisterReceiver method
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
