package com.company.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class Musicactivity extends AppCompatActivity {


    private Button buttonPlayPause,buttonPrevious, buttonNext;

    private TextView textViewFileNameMusic,textViewProgress,textViewTotalTime;
    private SeekBar seekBarVolume, seekBarMusic;

    String title,filepath;
    int position;
    ArrayList<String> list;

    private MediaPlayer mediaPlayer;

    Runnable runnable;
    Handler handler;
    int totalTime;

    private Animation animation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicactivity);

        buttonPlayPause = findViewById(R.id.buttonPlay);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        textViewFileNameMusic = findViewById(R.id.textViewFileNameMusic);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewTotalTime = findViewById(R.id.textViewTotalTime);
        seekBarMusic = findViewById(R.id.musicSeekBar);
        seekBarVolume = findViewById(R.id.volumeSeekBar);

        animation = AnimationUtils.loadAnimation(Musicactivity.this,R.anim.translate_animation);
        textViewFileNameMusic.setAnimation(animation);

        title = getIntent().getStringExtra("title");
        filepath = getIntent().getStringExtra("filepath");
        position = getIntent().getIntExtra("position",0);
        list = getIntent().getStringArrayListExtra("list");


        textViewFileNameMusic.setText(title);

        mediaPlayer = new MediaPlayer();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        totalTime = mediaPlayer.getDuration();
        seekBarMusic.setMax(totalTime);

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.reset();
                if(position == 0){
                    position = list.size() - 1;
                }
                else{
                    position--;
                }

                String newFilePath = list.get(position);

                try {
                    mediaPlayer.setDataSource(newFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    buttonPlayPause.setBackgroundResource(R.drawable.baseline_pause_circle_filled_24);



                    textViewFileNameMusic.clearAnimation();
                    textViewFileNameMusic.startAnimation(animation);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                textViewFileNameMusic.setText(newFilePath.substring(newFilePath.lastIndexOf("/") + 1));



            }
        });

        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mediaPlayer.isPlaying())
                {
                    mediaPlayer.start();
                    buttonPlayPause.setBackgroundResource(R.drawable.baseline_pause_circle_filled_24);

                }
                else{
                    mediaPlayer.pause();
                    buttonPlayPause.setBackgroundResource(R.drawable.play_arrow);
                }

            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.reset();
                if(position == list.size() - 1){
                    position = 0;
                }
                else{
                    position++;
                }

                String newFilePath = list.get(position);

                try {
                    mediaPlayer.setDataSource(newFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    buttonPlayPause.setBackgroundResource(R.drawable.baseline_pause_circle_filled_24);


                    textViewFileNameMusic.clearAnimation();
                    textViewFileNameMusic.startAnimation(animation);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                textViewFileNameMusic.setText(newFilePath.substring(newFilePath.lastIndexOf("/") + 1));


            }
        });

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( fromUser)
                {
                    seekBarVolume.setProgress(progress);
                    float volumeLevel = progress / 100f;
                    mediaPlayer.setVolume(volumeLevel,volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                    seekBarMusic.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int cp = mediaPlayer.getCurrentPosition();
                    seekBarMusic.setProgress(cp);

                    String elapsedTime = createTimeLabel(cp);
                    textViewProgress.setText(elapsedTime);

                    String lastTime = createTimeLabel(totalTime);
                    textViewTotalTime.setText(lastTime);

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            mediaPlayer.reset();
                            if(position == list.size() - 1){
                                position = 0;
                            }
                            else{
                                position++;
                            }

                            String newFilePath = list.get(position);

                            try {
                                mediaPlayer.setDataSource(newFilePath);
                                mediaPlayer.prepare();
                                mediaPlayer.start();

                                buttonPlayPause.setBackgroundResource(R.drawable.baseline_pause_circle_filled_24);


                                textViewFileNameMusic.clearAnimation();
                                textViewFileNameMusic.startAnimation(animation);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            textViewFileNameMusic.setText(newFilePath.substring(newFilePath.lastIndexOf("/") + 1));

                        }

                    });
                    handler.postDelayed(runnable,1000);
                }


            }
        };

        handler.post(runnable);
    }

    public String createTimeLabel(int currenPosition)
    {
        String timeLabel;
        int minute, second;

        minute = currenPosition / 1000 / 60;
        second = currenPosition / 1000 % 60;

        if(second < 10){
            timeLabel = minute + ":0"+ second;
        }
        else{
            timeLabel = minute + ":"+ second;
        }
        return timeLabel;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            finish();
        }
    }

}