package abhi.example.hp.stenobano.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.BannerPagerAdapter;
import abhi.example.hp.stenobano.model.Helper_Model;
import abhi.example.hp.stenobano.other_class.GetData;
import abhi.example.hp.stenobano.sqlite_databse.DatabaseHandler;

public class Play_Offline_View extends AppCompatActivity {




    private SoundPool soundPool;
    private  int soundID;
    boolean loaded=false;
    Helper_Model helper_model;
    int rotationAngle=90;/*angle by which you want to rotate image*/
    int mCurrRotation = 0;
    MediaPlayer mPlayer;
    Button buttonPlay,play_speed;
    Button buttonStop;
    SeekBar seek_bar,seekBar2;
    TextView text,text_speed,progers_percent;
    ViewPager imgage;
    Handler seekHandler = new Handler();
    int total;
    private List<Helper_Model.Image> modelList;
    String audio="";
    String image="",image_two="";
    ProgressBar progressBar;
    ImageView right;
    File file,file2;
    int seek=50;
    int speed=50;
    DatabaseHandler databaseHandler;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play__offline__view);
        helper_model= GetData.offlineMOdel;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Offline");
        buttonPlay = (Button) findViewById(R.id.play);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);


        play_speed=findViewById(R.id.play_speed);
        text_speed=findViewById(R.id.text_speed);
        imgage=findViewById(R.id.img);
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        text = findViewById(R.id.text);
        progers_percent=findViewById(R.id.progresstext);
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        right=findViewById(R.id.right);
        Intent i=getIntent();
        /*audio=i.getStringExtra("audio");
        image=i.getStringExtra("image");
         image_two=i.getStringExtra("image_two");
        file  = getFileStreamPath(image);
        */

      //  String imagePath = file.getAbsolutePath();
      // imgage.setImageDrawable(Drawable.createFromPath(imagePath));
      // imgage.setScaleType(ImageView.ScaleType.FIT_XY);
       // databaseHandler=new DatabaseHandler(this);
        //modelList = databaseHandler.getSingleData(i.getStringExtra("id"));
        modelList=helper_model.getListImage();
       BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(modelList, Play_Offline_View.this);
        imgage.setAdapter(bannerPagerAdapter);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Play_Offline_View.this, "clicked", Toast.LENGTH_SHORT).show();
                float fromRotation = mCurrRotation;
                float toRotation = mCurrRotation += rotationAngle;

                Animation animation = new RotateAnimation(
                        fromRotation,
                        toRotation,
                        Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);


                animation.setDuration(50);
                animation.setFillAfter(true);
                imgage.startAnimation(animation);
               // imgage.setScaleType(ImageView.ScaleType.FIT_XY);
               // imgage.setAdjustViewBounds(true);



            }
        });

        //  Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        buttonPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                buttonPlay.setText("Pause");
                mPlayer = new MediaPlayer();


                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                   // mPlayer.setDataSource(audio);
                   // String filePath = Environment.getExternalStorageDirectory().toString()+"/download"+"/"+audio;
                    Log.d("audioisi",helper_model.getListAudio().get(0).getAudio());
                    File file = getApplicationContext().getFileStreamPath(helper_model.getListAudio().get(0).getAudio());
                    String filePath = file.getAbsolutePath();
                    mPlayer = new  MediaPlayer();
                    mPlayer.setDataSource(filePath);
                    mPlayer.prepare();
                    mPlayer.start();
                    // mPlayer.prepare();
                    //mPlayer.start();
                   // mPlayer.prepareAsync();
                    startPlayProgressUpdater();
                    progressBar.setVisibility(View.VISIBLE);


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
                seek_bar.setProgress(mPlayer.getCurrentPosition());

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


                final AlertDialog.Builder builder = new AlertDialog.Builder(Play_Offline_View.this);
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
                            Toast.makeText(Play_Offline_View.this, "First play the music!", Toast.LENGTH_SHORT).show();
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

//
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
