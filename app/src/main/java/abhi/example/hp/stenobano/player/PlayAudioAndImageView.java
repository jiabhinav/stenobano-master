package abhi.example.hp.stenobano.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.R;

public class PlayAudioAndImageView extends AppCompatActivity implements View.OnClickListener {

    private PlaybackStateListener playbackStateListener;
    private static final String TAG = PlayAudioAndImageView.class.getName();

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    Button speed;
    DefaultTrackSelector trackSelector;

    String audio="";
    String image="";
    private ImageView imgage;
    private   BottomSheetDialog mBottomSheetDialog,
            mBottomSheetDialog_speed;
    private List<Float>speed_list;
    private float current_speed=1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio_and_image_view);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Read and play ");

        playerView = findViewById(R.id.video_view);
        imgage=findViewById(R.id.img);
        speed=findViewById(R.id.speed);
        speed.setOnClickListener(this);
        Intent i=getIntent();
        audio=i.getStringExtra("audio");
        image=i.getStringExtra("image");
        speed_list=new ArrayList<>();
        playbackStateListener = new PlaybackStateListener();
        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.steno_play_page)
                .into(imgage);
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
            player.addListener(playbackStateListener);
             player.prepare(mediaSource, false, true);
           // player.prepare(mediaSource);
            player.seekTo(currentWindow, playbackPosition);
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
           // initializePlayer();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            //releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
           // releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
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
        PlaySpeedAdpater adpater = new PlaySpeedAdpater(speed_list, this, current_speed,false);
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