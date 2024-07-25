package com.example.taskmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class AlramReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {

    }
    public void playalramsound(Context context){
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.sample);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
                mediaPlayer.start();
            } else {
                Log.e("AlramReceiver", "MediaPlayer creation failed!");
            }
        } catch (Exception e) {
            Log.e("AlramReceiver", "Error playing sound: " + e.getMessage());
        }
    }
}
