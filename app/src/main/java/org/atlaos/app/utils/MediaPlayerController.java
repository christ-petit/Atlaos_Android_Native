package org.atlaos.app.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.atlaos.app.R;

import java.io.IOException;

public class MediaPlayerController implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{
    private SeekBar seekBar;
    private MaterialButton button;
    private TextView lengthView;
    private TextView curentPositionView;
    private MediaPlayer mediaPlayer;
   Context context;
    private Handler mHandler = new Handler();

    public MediaPlayerController(SeekBar seekBar, MaterialButton button, TextView lengthView, TextView curentPositionView, Context context){
       this.seekBar =seekBar;
       this.button= button;
       this.lengthView=lengthView;
       this.curentPositionView=curentPositionView;
       button.setOnClickListener(this);
       this.seekBar.setOnSeekBarChangeListener(this);
       this.context=context;
    }



    public void loadRecord(Uri song) {
    if (mediaPlayer == null) mediaPlayer=new MediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, song);
            mediaPlayer.prepare();
            button.setIconResource(R.drawable.ic_baseline_play_arrow_white_24);
            seekBar.setProgress(0);
            seekBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IOException e) {

        }
    }


    @Override
    public void onClick(View v) {
        if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                button.setIconResource(R.drawable.ic_baseline_play_arrow_white_24);
        }
        else {
            mediaPlayer.start();
            button.setIconResource(R.drawable.baseline_pause_white_24);
        }

    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {


            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            lengthView.setText(""+milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            curentPositionView.setText(""+milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }



    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    public void onDestroy(){
        if (mediaPlayer!= null)  mediaPlayer.release();
    }
}
