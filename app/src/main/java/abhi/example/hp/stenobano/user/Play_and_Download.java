package abhi.example.hp.stenobano.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import abhi.example.hp.stenobano.R;

import java.io.IOException;

public class Play_and_Download extends AppCompatActivity {

    SoundPool soundPool;
    int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;
    float playbackSpeed=1.5f;

    int playcount=0;
    int rotationAngle=90;/*angle by which you want to rotate image*/
    int mCurrRotation = 0;
    private  MediaPlayer mPlayer;
    private  Button buttonPlay,play_speed;
    private Button buttonStop;
    private SeekBar seek_bar,seekBar2;
    private  TextView text,text_speed,progers_percent;
    private ImageView imgage;
    Handler seekHandler = new Handler();
    int total;

    String audio="";
    String image="";
    String image2="";
    private  ProgressBar progressBar;
    private Button right;
    int seek=50;
    int speed=50;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            if (mPlayer!=null)
            {
                if(mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                //mPlayer.release();
                mPlayer.reset();
                mPlayer.release();
                finish();
            }
            else
            {
                finish();
            }



            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__speed__test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonPlay = (Button) findViewById(R.id.play);
        play_speed=findViewById(R.id.play_speed);
        text_speed=findViewById(R.id.text_speed);
        imgage=findViewById(R.id.img);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        text = findViewById(R.id.text);
        progers_percent=findViewById(R.id.progresstext);
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        right=findViewById(R.id.right);
        Intent i=getIntent();
        audio=i.getStringExtra("audio");
        image=i.getStringExtra("image");



        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.preview)
                .centerCrop()
                .into(imgage);



        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float fromRotation = mCurrRotation;
                float toRotation = mCurrRotation += rotationAngle;

                Animation animation = new RotateAnimation(
                        fromRotation,
                        toRotation,
                        Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);


                animation.setDuration(50);
                animation.setFillAfter(true);
                imgage.startAnimation(animation);
                imgage.setScaleType(ImageView.ScaleType.FIT_XY);



            }
        });

        //  Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        buttonPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {





                if (playcount == 0) {
                    playcount++;


                    buttonPlay.setText("Pause");
                    mPlayer = new MediaPlayer();

                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mPlayer.setDataSource(audio);
                        // mPlayer.prepare();
                        //mPlayer.start();
                        mPlayer.prepareAsync();
                        startPlayProgressUpdater();
                        progressBar.setVisibility(View.VISIBLE);

                        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                            @Override
                            public void onBufferingUpdate(MediaPlayer mp, int percent) {

                                if (percent == 100) {
                                    progers_percent.setText("");
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progers_percent.setText(Integer.toString(percent));
                                    progressBar.setVisibility(View.VISIBLE);
                                }

                            }
                        });


                        try {
                            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    progressBar.setVisibility(View.GONE);
                                    mPlayer.start();
                                    startPlayProgressUpdater();
                                    seek_bar.setMax(mPlayer.getDuration());
                                    seek_bar.setOnTouchListener(new View.OnTouchListener() {
                                        public boolean onTouch(View v, MotionEvent event) {
                                            UpdateseekChange(v);
                                            startPlayProgressUpdater();
                                            return false;
                                        }
                                    });
                                }
                            });
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }






                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    seek_bar.setMax(mPlayer.getDuration());
                    seek_bar.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            UpdateseekChange(v);
                            startPlayProgressUpdater();

                            return false;
                        }
                    });


                    //   final Button btn1 = (Button) findViewById(R.id.button1); // Start


                }
                else
                {
                    if (mPlayer.isPlaying()) {

                        //text.setText("pause : music.mp3....");
                        mPlayer.pause();
                        buttonPlay.setText("Play");
                    } else {
                        // text.setText("Playing : music.mp3....");
                        mPlayer.start();
                        buttonPlay.setText("Pause");
                        progressBar.setVisibility(View.GONE);
                        progers_percent.setText("");
                        // startPlayProgressUpdater();
                    }

                }


            }


            private void UpdateseekChange(View v) {
                if (mPlayer.isPlaying()) {
                    SeekBar sb = (SeekBar) v;
                    mPlayer.seekTo(sb.getProgress());
                }
            }

            public void startPlayProgressUpdater() {

                //seek_bar.setProgress(mPlayer.getCurrentPosition());
                if (mPlayer!=null)
                {



                    if (mPlayer.isPlaying()) {
                        Runnable notification = new Runnable() {
                            public void run() {

                                // startPlayProgressUpdater();
                                try {

                                    total = mPlayer.getCurrentPosition();
                                    seek_bar.setProgress(mPlayer.getCurrentPosition());
                                    int hours = (int) (total / (1000 * 60 * 60));
                                    int minutes = (int) (total % (1000 * 60 * 60)) / (1000 * 60);
                                    int seconds = (int) ((total % (1000 * 60 * 60)) % (1000 * 60) / 1000);
                                    text.setText(String.valueOf(hours) + ":" + String.valueOf(minutes) + ":" + String.valueOf(seconds));
                                    // Toast.makeText(MainActivity.this, ""+hours+":"+minutes+":"+seconds, Toast.LENGTH_SHORT).show();
                                    seekHandler.postDelayed(this, 400);

                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }

                        };
                        seekHandler.postDelayed(notification, 1000);

                    }
                    else {

                        if (mPlayer.isPlaying()) {

                            //text.setText("pause : music.mp3....");
                            mPlayer.pause();
                            buttonPlay.setText("Play");
                        } else {
                            // text.setText("Playing : music.mp3....");
                            mPlayer.start();
                            buttonPlay.setText("Pause");
                            progressBar.setVisibility(View.GONE);
                            progers_percent.setText("");
                            // startPlayProgressUpdater();
                        }


                    }
                }
                else
                {

                }

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    mPlayer.seekTo(progress * 1000);
                }


            }
        });
        play_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(Play_and_Download.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.alert_play_speed, null);
                builder.setView(view);
                seekBar2=view.findViewById(R.id.seek_bar2);
                text_speed=view.findViewById(R.id.text_speed);
                seekBar2.setProgress(seek);
                text_speed.setText(Integer.toString(speed));

                seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mPlayer != null) {
                            changeplayerSpeed((float) progress / 50);
                            seek=progress;
                            speed=progress;
                            text_speed.setText(Integer.toString(progress));
                            // seekBar2.setProgress(Integer.parseInt(seek));
                            // text_speed.setText(speed);
                        }

                        else
                        {
                            Toast.makeText(Play_and_Download.this, "First play the music!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });



                builder.create();
                builder.show();

            }
        });
    }

    private void changeplayerSpeed(float speed) {

        // this checks on API 23 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPlayer.isPlaying()) {
                try
                {
                    mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setSpeed(speed));
                }
                catch (Exception ex)
                {

                }


            } else {
                Toast.makeText(this, "This Feature Supported Above API 23 level (5.0 version ) mobile  ", Toast.LENGTH_SHORT).show();

                //mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setSpeed(speed));
                // mPlayer.pause();

                /*
                 */

            }
        }
        else
        {

        }
    }
    @Override
    public void onBackPressed () {
        releaseMediaPlayer();

    }


    private void releaseMediaPlayer() {

        if (mPlayer!=null)
        {
            if(mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            //mPlayer.release();
            mPlayer.reset();
            mPlayer.release();
            finish();
        }
        else
        {
            finish();
        }


    }











}
