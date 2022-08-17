

package abhi.example.hp.stenobano.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import abhi.example.hp.stenobano.Dashboard.User_Home;
import abhi.example.hp.stenobano.Interface.SetSpeed;
import abhi.example.hp.stenobano.R;
import abhi.example.hp.stenobano.adapter.AudioPlay_Adapter;
import abhi.example.hp.stenobano.config.BaseUrl;
import abhi.example.hp.stenobano.databinding.ActivityPlayAudioImageBinding;
import abhi.example.hp.stenobano.model.ModelCategoryDetail;
import abhi.example.hp.stenobano.notification.ResultNotification;
import abhi.example.hp.stenobano.other_class.GetData;
import abhi.example.hp.stenobano.other_class.RecyclerViewDisabler;
import abhi.example.hp.stenobano.player.PlayAudioAndImageView;
import abhi.example.hp.stenobano.player.PlaySpeedAdpater;
import abhi.example.hp.stenobano.session.SesssionManager;

public class PlayAudioImage extends AppCompatActivity  implements Player.EventListener,SetSpeed, View.OnClickListener {
    private PlaybackStateListener playbackStateListener;
    List<ModelCategoryDetail.Result> model;
    List<ModelCategoryDetail.Result> model2;
    private ActivityPlayAudioImageBinding binding;
    private LinearLayoutManager  layoutManager;
    int currentPage = -1;
    int swipe_count = 0;
    boolean is_visible_to_user;
    PlayerView playerView;
    SimpleExoPlayer player;
    SimpleExoPlayer privious_player;
    int p=0;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    DefaultTrackSelector trackSelector;
    String default_play="once";
    private BottomSheetDialog mBottomSheetDialog,
            mBottomSheetDialog_speed;

    private List<Float>speed_list;
    public float current_speed=1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_play_audio_image);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playbackStateListener = new PlaybackStateListener();
        model= GetData.modelAllDetail;
        model2=new ArrayList<>();
        p=getIntent().getIntExtra("pos",0);
        Log.d("dsdsdwfwadd", "onCreate: "+GetDataNew.type_id+"==="+getType());
      /*  ModelCategoryDetail.Result modellist=GetData.modelAllDetail.get(p);
        model2.add(modellist);
      */
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        AudioPlay_Adapter adapter=new AudioPlay_Adapter(model,this,this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView( binding.recyclerView);
        layoutManager.scrollToPositionWithOffset(p, 0);
        speed_list=new ArrayList<>();
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number

                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;
                Log.d("page_no", String.valueOf(page_no));

                if (page_no != currentPage) {
                    currentPage = page_no;
                    binding.search.setText(model.get(currentPage).getTitle());
                    Release_Privious_Player();
                    Set_Player(currentPage);
                }
            }
        });

    }


    public void Set_Player(final int currentPage) {
        p=p+1;
        final ModelCategoryDetail.Result item = model.get(currentPage);
        //=====================================================
        View layout = layoutManager.findViewByPosition(currentPage);
        playerView = layout.findViewById(R.id.video_view);
        playerView.setPlayer(player);

        trackSelector = new DefaultTrackSelector();
        trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd());
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        // playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        playerView.setPlayer(player);

        String url="http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws";
        // Uri uri = Uri.parse(url);
        binding.search.setText(item.getTitle());
        MediaSource mediaSource = buildMediaSource(BaseUrl.IMAGE_URL +item.getAudio().get(0).getName());
        player.addListener(playbackStateListener);
        player.prepare(mediaSource, false, true);
        // player.prepare(mediaSource);
        player.seekTo(currentWindow, playbackPosition);
        player.setPlayWhenReady(true);
       // if (new SesssionManager(PlayAudioImage.this).getDefaultPlay().equals("repeat"))

        if (default_play.equals("repeat"))
        {
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
        }
        else
        {
            player.setRepeatMode(Player.REPEAT_MODE_OFF);
        }
        playerView.setUseController(true);
        privious_player = player;

       float speed= Float.parseFloat(new SesssionManager(PlayAudioImage.this).getSpeed());
        PlaybackParameters playbackParameters = new PlaybackParameters(current_speed, 1.0F);
        player.setPlaybackParameters(playbackParameters);

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
                    stateString = "ExoPlayer.STATE_IDLE -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY-";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED -";
                    //if (new SesssionManager(PlayAudioImage.this).getDefaultPlay().equals("next"))
                    if (default_play.equals("next"))
                    {
                        Log.d("pis",p+"");
                        if (model.size() >= p)
                        {
                            Toast.makeText(PlayAudioImage.this, "Next", Toast.LENGTH_SHORT).show();
                            layoutManager.scrollToPositionWithOffset(p, 0);
                        }

                    }

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


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.getPlaybackParameters();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Release_Privious_Player();
    }

    public void Release_Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(PlayAudioImage.this);
            privious_player.release();
        }
    }


    @Override
    public void onClick(View view) {

         if (view.getId()==R.id.llplayspeed)
        {
            mBottomSheetDialog.dismiss();
            speed_option_menu();

        }
    }


    @Override
    public void speed() {
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
        speed_list.add(0.20f);
        speed_list.add(0.30f);
        speed_list.add(0.40f);
        speed_list.add(0.50f);
        speed_list.add(0.60f);
        speed_list.add(0.70f);
        speed_list.add(0.80f);
        speed_list.add(0.90f);
        speed_list.add(1f);
        speed_list.add(1.10f);
        speed_list.add(1.20f);
        speed_list.add(1.30f);
        speed_list.add(1.40f);
        speed_list.add(1.50f);
        speed_list.add(1.60f);
        speed_list.add(1.70f);
        speed_list.add(1.80f);
        speed_list.add(1.90f);
        speed_list.add(2f);
        mBottomSheetDialog_speed = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.speed_option_menu, null);
        RecyclerView recyclerView = sheetView.findViewById(R.id.recyclerview);
        mBottomSheetDialog_speed.setContentView(sheetView);
        mBottomSheetDialog_speed.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PlaySpeedAdpater adpater = new PlaySpeedAdpater(speed_list, this, current_speed,getType());
        recyclerView.setAdapter(adpater);

    }


    public void setPlaySpeed(float speed)
    {
        current_speed=speed;
        new SesssionManager(PlayAudioImage.this).setSpeed(String.valueOf(speed));
        PlaybackParameters playbackParameters = new PlaybackParameters(current_speed, 1.0F);
        player.setPlaybackParameters(playbackParameters);
        mBottomSheetDialog_speed.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.setting) {
            alertSetPlaySetting();
            return true;
        }
       else if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        else if (item.getItemId()==R.id.speed)
        {
            speed_option_menu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alertSetPlaySetting()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Default Play");
        final View customLayout = getLayoutInflater().inflate(R.layout.set_default_play, null);
        builder.setView(customLayout);
        RadioGroup radioGroup=customLayout.findViewById(R.id.radiogroup);
        RadioButton repeat=customLayout.findViewById(R.id.repeat);
        RadioButton next=customLayout.findViewById(R.id.next);
        RadioButton once=customLayout.findViewById(R.id.once);
       // if (new SesssionManager(PlayAudioImage.this).getDefaultPlay().equals("repeat"))
        Log.d("saddada",default_play);
        if (default_play.equals("repeat"))
        {
            repeat.setChecked(true);
        }
        else if (default_play.equals("once"))
        {
            once.setChecked(true);
        }
        else
        {
            next.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.next)
                {
                    default_play="next";
                    new SesssionManager(PlayAudioImage.this).setPlay(default_play);
                    Toast.makeText(PlayAudioImage.this, "Next", Toast.LENGTH_SHORT).show();
                }

               else if (i==R.id.once)
                {
                    default_play="once";
                    new SesssionManager(PlayAudioImage.this).setPlay(default_play);
                    Toast.makeText(PlayAudioImage.this, "once", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    default_play="repeat";
                    new SesssionManager(PlayAudioImage.this).setPlay(default_play);
                    Toast.makeText(PlayAudioImage.this, "Repeat", Toast.LENGTH_SHORT).show();
                }

                if (default_play.equals("repeat"))
                {
                    player.setRepeatMode(Player.REPEAT_MODE_ALL);
                }
                else
                {
                    player.setRepeatMode(Player.REPEAT_MODE_OFF);
                }
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean getType()
    {
        boolean exist=false;
       int id= Integer.parseInt(GetDataNew.type_id);
       if (id==102)
       {
          exist=true;
       }
        if (id==103)
        {
            exist=true;
        }
        if (id==104)
        {
            exist=true;
        }
        if (id==105)
        {
            exist=true;
        }
        if (id==112)
        {
            exist=true;
        }
        if (id==113)
        {
            exist=true;
        }
        if (id==114)
        {
            exist=true;
        }
        if (id==115)
        {
            exist=true;
        }


        return exist;
    }



}