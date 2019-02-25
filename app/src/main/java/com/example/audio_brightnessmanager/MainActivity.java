package com.example.audio_brightnessmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    float BackLightValue = 0.1f;
    AudioManager mAudio;
    ProgressBar pb;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(this.batteryInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        SeekBar BackLightControl = (SeekBar)findViewById(R.id.brightness);
        BackLightControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BackLightValue = (float)progress/100;
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = BackLightValue;
                getWindow().setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mAudio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        SeekBar alarm = (SeekBar)findViewById(R.id.alarm);
        SeekBar music = (SeekBar)findViewById(R.id.music);
        SeekBar ring = (SeekBar)findViewById(R.id.ringtone);
        SeekBar system = (SeekBar)findViewById(R.id.system);
        SeekBar voice = (SeekBar)findViewById(R.id.voice);

        initControls(alarm,AudioManager.STREAM_ALARM);
        initControls(music,AudioManager.STREAM_MUSIC);
        initControls(ring,AudioManager.STREAM_RING);
        initControls(system,AudioManager.STREAM_SYSTEM);
        initControls(voice,AudioManager.STREAM_VOICE_CALL);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.share:
                share();
                return true;
            case R.id.exit:
                close();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("EXIT APP")
                .setMessage("DO YOU REALLY WANT TO EXIT?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("NO",null)
                .show();
    }

    private void share() {
        Intent intentshare = new Intent();
        intentshare.setAction(Intent.ACTION_SEND);
        intentshare.putExtra(android.content.Intent.EXTRA_SUBJECT,"AUDIO-BRIGHTNESS MANAGER");
        intentshare.putExtra(Intent.EXTRA_TEXT,"THIS APP IS STILL NOT UPLOADED TO PLAY STORE SO GET IT FROM YOUR FRIEND THROUGH 'ShareIT'");
        intentshare.setType("text/plain");
        startActivity(intentshare);
    }

    private void close() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("EXIT APP")
                .setMessage("DO YOU REALLY WANT TO EXIT?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("NO",null)
                .show();
    }

    BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setProgress(level);
            tv1 = (TextView)findViewById(R.id.tv1);
            tv1.setText(level+"%");
        }
    };
    private void initControls (SeekBar seek,final int stream)
    {
        seek.setMax(mAudio.getStreamMaxVolume(stream));
        seek.setProgress(mAudio.getStreamVolume(stream));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudio.setStreamVolume(stream,progress,AudioManager.FLAG_PLAY_SOUND);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
