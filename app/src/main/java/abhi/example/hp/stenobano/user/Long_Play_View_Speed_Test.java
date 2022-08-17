package abhi.example.hp.stenobano.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.ImageSliderAdapter;
import abhi.example.hp.stenobano.model.Search_Model;

public class Long_Play_View_Speed_Test extends AppCompatActivity  {
    private ViewPager viewPager;
    int rotationAngle=90;/*angle by which you want to rotate image*/
    int mCurrRotation = 0;
    private MediaPlayer mPlayer;
    private Button buttonPlay,play_speed;
    Button buttonStop;
    private SeekBar seek_bar,seekBar2;
    private TextView text,text_speed,progers_percent;
    ProgressBar progressBar;
    Handler seekHandler = new Handler();

    int seek=50;
    private int page = 0;
    int speed=50;
    int total;
    String audio="";
    String image="";
    String image2="";
    int position;
    private List<Search_Model> sliderImg;
    ImageSliderAdapter imageSliderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long__play__view__speed__test);
        buttonPlay = (Button) findViewById(R.id.play);
        play_speed=findViewById(R.id.play_speed);
        text_speed=findViewById(R.id.text_speed);
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        text = findViewById(R.id.text);
        progers_percent=findViewById(R.id.progresstext);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        viewPager=findViewById(R.id.viewPager);
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        sliderImg = new ArrayList<>();
        Intent i=getIntent();
        audio=i.getStringExtra("audio");
        image=i.getStringExtra("image");
        image2=i.getStringExtra("image2");
        getImage();
        imageSliderAdapter = new ImageSliderAdapter(sliderImg, Long_Play_View_Speed_Test.this);
        viewPager.setAdapter(imageSliderAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state<sliderImg.size())
                {
                    //intro_images is viewpager
                    //  viewPager.setCurrentItem(0,true);
                }
            }
        });
        buttonPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

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
                            if (percent==100)
                            {
                                progers_percent.setText("");
                                progressBar.setVisibility(View.GONE);
                            }
                            else
                            {
                                progers_percent.setText(Integer.toString(percent));
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
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


                // Start
                buttonPlay.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

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
                            startPlayProgressUpdater();
                        }
                    }
                });


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


                final AlertDialog.Builder builder = new AlertDialog.Builder(Long_Play_View_Speed_Test.this);
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
                            Toast.makeText(Long_Play_View_Speed_Test.this, "First play the music!", Toast.LENGTH_SHORT).show();
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
                mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setSpeed(speed));

            } else {
                mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setSpeed(speed));
                mPlayer.pause();
            }
        }
    }

    private void getImage()
    {


        for (int i=0;i<2;i++) {
            if (i==0) {
                Search_Model model=new Search_Model();
                model.setImage(image);
                sliderImg.add(model);
            }
            else {
                Search_Model model=new Search_Model();
                model.setImage(image2);
                sliderImg.add(model);
            }


        }

    }


    @Override
    public void onBackPressed ()
    {
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
