package com.stompbox.project6.stompbox;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by mrpatel5 on 4/11/2017.
 */

public class InputRecorded {
    // Class variables
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Stompbox/"; // directory;
    final String output = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Stompbox/temp.mp3"; // in Internal Storage

    // Constructor
    public InputRecorded(){
        // Do need context (this.activity) ?
        mediaRecorder = new MediaRecorder(); // object to record audio
        mediaPlayer = new MediaPlayer(); // object to playback audio
        //"/data/data/com.stompbox.project6.stompbox/files" + "/temp.mp3"


    }

    // For now, assume app has RECORD_AUDIO permissions

    // Record
    public void onRecord(boolean start){
        if(start){
            startRecording();
        }
        else{
            stopRecording();
        }
    }

    // Playback
    public void onPlay(boolean start){
        if(start){
            startPlaying();
        }
        else{
            stopPlaying();
        }
    }

    // Start Recording
    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // microphone input
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); //
        mediaRecorder.setOutputFile(output);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

        try{
            mediaRecorder.prepare();
            mediaRecorder.start(); // start recording

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    // Stop Recording
    public void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    // Start Playing
    public void startPlaying() {
        mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(output);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Stop Playing
    private void stopPlaying(){
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
