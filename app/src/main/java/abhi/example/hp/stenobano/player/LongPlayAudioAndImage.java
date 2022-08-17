package abhi.example.hp.stenobano.player;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.ImageSliderAdapter;
import abhi.example.hp.stenobano.model.Search_Model;

public class LongPlayAudioAndImage extends AppCompatActivity implements View.OnClickListener {

    private PlaybackStateListener playbackStateListener;
    private static final String TAG = PlayAudioAndImageView.class.getName();

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    Button speed;
    DefaultTrackSelector trackSelector;

    private BottomSheetDialog mBottomSheetDialog,
            mBottomSheetDialog_speed;
    private List<Float>speed_list;
    private float current_speed=1f;
    private int page = 0;


    private ViewPager viewPager;
    String audio="";
    String image="";
    String image2="";
    private List<Search_Model> sliderImg;
    ImageSliderAdapter imageSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long__player);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Read and play ");
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        playerView = findViewById(R.id.video_view);
        speed=findViewById(R.id.speed);
        speed.setOnClickListener(this);
        viewPager=findViewById(R.id.viewPager);
        sliderImg = new ArrayList<>();
        Intent i=getIntent();
        audio=i.getStringExtra("audio");
        image=i.getStringExtra("image");
        image2=i.getStringExtra("image2");
        speed_list=new ArrayList<>();
        playbackStateListener = new LongPlayAudioAndImage.PlaybackStateListener();
        getImage();
        imageSliderAdapter = new ImageSliderAdapter(sliderImg, LongPlayAudioAndImage.this);
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
        initializePlayer();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void initializePlayer() {

        if (player == null) {

            trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            // playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            playerView.setPlayer(player);

            String url="http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws";
            // Uri uri = Uri.parse(url);
            MediaSource mediaSource = buildMediaSource(audio);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.addListener(playbackStateListener);
            // player.prepare(mediaSource, false, true);

            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            playerView.setUseController(true);
//======================================

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            // initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
          //  initializePlayer();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
           // releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
           // releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.speed)
        {
            openMenu();
        }
        else if (view.getId()==R.id.llplayspeed)
        {
            mBottomSheetDialog.dismiss();
            speed_option_menu();

        }

    }


    private MediaSource buildMediaSource(String uri) {
        // These factories are used to construct two media sources below
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        ProgressiveMediaSource.Factory mediaSourceFactory =
                new ProgressiveMediaSource.Factory(dataSourceFactory);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        extractorsFactory.setConstantBitrateSeekingEnabled(true);

        // Create a media source using the supplied URI
        //  MediaSource mediaSource1 = mediaSourceFactory.createMediaSource(uri);
        Uri audioUri = Uri.parse(uri);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(extractorsFactory)
                .createMediaSource(audioUri);

        MediaSource mediaSource2 = mediaSourceFactory.createMediaSource(audioUri);

        return new ConcatenatingMediaSource(videoSource);



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




    private class PlaybackStateListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }

            Log.d("playingststusis",stateString);
     /* Log.d(TAG, "changed state to " + stateString
              + " playWhenReady: " + playWhenReady);*/
        }
    }


    private void openMenu()
    {
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.player_bottom_menu, null);
        LinearLayout llplayspeed=sheetView.findViewById(R.id.llplayspeed);
        llplayspeed.setOnClickListener(this);
        TextView sp=sheetView.findViewById(R.id.currentspeed);
        if (current_speed==1f)
        {
            sp.setText("100 wpm(Normal)");
        }
        else
        {
            sp.setText(String.valueOf(current_speed));
        }

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }


    private void speed_option_menu() {
        speed_list.clear();
        speed_list.add(0.25f);
        speed_list.add(0.50f);
        speed_list.add(0.75f);
        speed_list.add(1f);
        speed_list.add(1.25f);
        speed_list.add(1.5f);
        speed_list.add(1.75f);
        speed_list.add(2f);
        mBottomSheetDialog_speed = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.speed_option_menu, null);
        RecyclerView recyclerView = sheetView.findViewById(R.id.recyclerview);
        mBottomSheetDialog_speed.setContentView(sheetView);
        mBottomSheetDialog_speed.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        LongPalySpeedAdpater adpater = new LongPalySpeedAdpater(speed_list, this, current_speed);
        recyclerView.setAdapter(adpater);


    }

    public void setPlaySpeed(float speed)
    {
        current_speed=speed;
        PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
        player.setPlaybackParameters(playbackParameters);
        mBottomSheetDialog_speed.dismiss();

    }





}
