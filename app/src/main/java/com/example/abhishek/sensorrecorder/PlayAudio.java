package com.example.abhishek.sensorrecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Abhishek on 7/23/2017.
 */

public class PlayAudio extends AsyncTask <ParamsAsync, Long, Integer> {

    @Override
    protected Integer doInBackground(ParamsAsync... params) {
        MediaPlayer mp = MediaPlayer.create(params[0].contxt, params[0].rawIdentifier);
        try {
            Thread.sleep(5000);
            mp.start();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return Integer.valueOf(mp.getDuration());
    }
}