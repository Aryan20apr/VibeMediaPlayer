package com.example.videoplayer.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.window.DialogProperties;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PictureInPictureParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bullhead.equalizer.EqualizerFragment;
import com.bullhead.equalizer.Settings;
import com.example.videoplayer.BrightnessDialog;
import com.example.videoplayer.MediaFiles;
import com.example.videoplayer.PlayListDialog;
import com.example.videoplayer.R;
import com.example.videoplayer.RecyclerViewClasses.IconModel;
import com.example.videoplayer.RecyclerViewClasses.MediaFilesAdapter;
import com.example.videoplayer.RecyclerViewClasses.PlaybackIconsAdapter;
import com.example.videoplayer.VolumeDialog;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.utils.FilePickerProvider;

import java.io.File;
import java.util.ArrayList;

/*
public class VideoPlayerActivity extends AppCompatActivity {

    String videotitle;
    StyledPlayerView palyerView;
    ExoPlayer player;
    int position;
    ArrayList<MediaFiles> mVideoFiles=new ArrayList<>();
    TextView title;

    ConcatenatingMediaSource concatenatingMediaSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Log.i("VideoPlayerActivity"+"!@#","Inside inOnCreate()");
        palyerView=findViewById(R.id.exoplayer_view);
        getSupportActionBar().hide();

        position=getIntent().getIntExtra("position",1);
        videotitle=getIntent().getStringExtra("video_title");
        mVideoFiles=getIntent().getExtras().getParcelableArrayList("videoArrayList");

        title=findViewById(R.id.video_title);
        title.setText(videotitle);
        playvideo();//Play video through URI

    }
    //Play Video through URI
    private void playvideo() {
        String path=mVideoFiles.get(position).getPath();
        Uri uri=Uri.parse(path);
        player=new ExoPlayer.Builder(this).setSeekBackIncrementMs(10000).setSeekForwardIncrementMs(10000).build();
        DefaultDataSource.Factory dataSourceFactory=new DefaultDataSource.Factory(this);//, Util.getUserAgent(this,"app"));
        concatenatingMediaSource=new ConcatenatingMediaSource();
        for(int i=0;i<mVideoFiles.size();i++)//Play Videos in loop
        {
           new File(String.valueOf(mVideoFiles.get(i)));//Create new File
            MediaSource mediaSource=new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(String.valueOf(uri))));//(MediaItem.fromUri(Uri.parse(String.valueOf(uri))));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        palyerView.setPlayer(player);
        palyerView.getKeepScreenOn();//Prevents the screen from dimming after the screen time out reach
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        //Show error if ExoPlayer ... while playing video
        playerError();

    }

    private void playerError() {
        player.addListener(new Player.Listener(){//Player.EventListener
            //@Override
            public void onPlayerError(ExoPlaybackException error)
            {
                Toast.makeText(VideoPlayerActivity.this, "Video Playing Error", Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }

    */

/**
 * {@inheritDoc}
 * <p>
 * Dispatch onPause() to fragments.
 *//*

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
            //player.stop();
            //player.seekTo(0);
        }
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        player.setPlayWhenReady(true);
//
//}

}*///This has been commented out as the new classes introduced had some errors, Correct this.
public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
    PlayerView playerView;
    SimpleExoPlayer player;
    //StyledPlayerView playerView;
    //ExoPlayer player;
    int position;
    String videoTitle;
    ArrayList<MediaFiles> mVideoFilesArrayList;
    TextView title;
    private ControlsMode controlsMode;
     boolean isCrossChecked;
     FrameLayout eqContainer;

    public enum ControlsMode {
        LOCK, FULLSCREEN;
    }

    ImageView videoBack, lock, unlock, scaling,videoList,video_more;//VideoList is for playlist
    MediaFilesAdapter mediaFilesAdapter;
    RelativeLayout root;
    ConcatenatingMediaSource concatenatingMediaSource;
    ImageView nextButton, previousButton;
    //horizontal recyclerview variables
    private ArrayList<IconModel> iconModelArrayList = new ArrayList<>();
    PlaybackIconsAdapter playbackIconsAdapter;
    RecyclerView recyclerViewIcons;
    boolean expand = false;
    View nightMode;
    boolean dark = false;
    boolean mute=false;
    PlaybackParameters parameters;
    float speed;
    /**DialogProperties dialogProperties;
    FilePickerDialog filePickerDialog;
    Uri subtitle;*/
    PictureInPictureParams.Builder pictureInpicture;
    //horizontal recyclerview variables
    WindowInsetsControllerCompat windowInsetsControllerCompat;//Hide Navigation Bar for immersive mode
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowInsetsControllerCompat= ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        setFullScreen();
        setContentView(R.layout.activity_video_player);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.e(TAG + " ###", "in VideoPlayerActivity: " + e);
        }
        videoList=findViewById(R.id.video_list);
        playerView = findViewById(R.id.exoplayer_view);
        //Hide the action bar find alternative
        position = getIntent().getIntExtra("position", 1);
        videoTitle = getIntent().getStringExtra("video_title");
        mVideoFilesArrayList = getIntent().getExtras().getParcelableArrayList("videoArrayList");
        screenOrientation();

        nextButton = findViewById(R.id.exo_next);
        videoBack = findViewById(R.id.video_back);
        lock = findViewById(R.id.lock);
        unlock = findViewById(R.id.unlock);
        root = findViewById(R.id.root_layout);
        scaling = findViewById(R.id.scalling);
        previousButton = findViewById(R.id.exo_prev);
        eqContainer=findViewById(R.id.eqFrame);

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        videoBack.setOnClickListener(this);
        lock.setOnClickListener(this);
        unlock.setOnClickListener(this);
        videoList.setOnClickListener(this);
        scaling.setOnClickListener(firstListener);
        video_more=findViewById(R.id.video_more);
        video_more.setOnClickListener(this);

        /**dialogProperties=new DialogProperties();
        filePickerDialog=new FilePickerDialog();
        filePickerDialog.setTitle("Select A subtitle File");
        filePickerDialog.*/
        title = findViewById(R.id.video_title);
        title.setText(videoTitle);
        nightMode=findViewById(R.id.night_mode);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            //If android version greater then android oreo, we can show pip mode
            pictureInpicture=new PictureInPictureParams.Builder();
        }
        recyclerViewIcons = findViewById(R.id.recyclerview_icon);
        iconModelArrayList.add(new IconModel(R.drawable.ic_right, ""));
        iconModelArrayList.add(new IconModel(R.drawable.ic_rotate, "Rotate"));
        iconModelArrayList.add(new IconModel(R.drawable.ic_volume_off, "Mute"));
        iconModelArrayList.add(new IconModel(R.drawable.ic_pip_mode, "Popup"));
        iconModelArrayList.add(new IconModel(R.drawable.ic_night, "Night Mode"));


        playbackIconsAdapter = new PlaybackIconsAdapter(iconModelArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true);
        recyclerViewIcons.setLayoutManager(layoutManager);
        recyclerViewIcons.setAdapter(playbackIconsAdapter);
        playbackIconsAdapter.notifyDataSetChanged();
        playbackIconsAdapter.setOnItemClickListner(new PlaybackIconsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    if (expand) {
                        iconModelArrayList.clear();
                        iconModelArrayList.add(new IconModel(R.drawable.ic_right, ""));
                        iconModelArrayList.add(new IconModel(R.drawable.ic_rotate, "Rotate"));
                        iconModelArrayList.add(new IconModel(R.drawable.ic_volume_off, "Mute"));
                        iconModelArrayList.add(new IconModel(R.drawable.ic_night, "Night Mode"));
                        iconModelArrayList.add(new IconModel(R.drawable.ic_pip_mode, "Popup"));

                        playbackIconsAdapter.notifyDataSetChanged();
                        expand = false;
                    } else {
                        if (iconModelArrayList.size() == 5) {
                            iconModelArrayList.add(new IconModel(R.drawable.ic_volume, "Volume"));
                            iconModelArrayList.add(new IconModel(R.drawable.ic_brightness, "Brightness"));
                            iconModelArrayList.add(new IconModel(R.drawable.ic_equalizer, "Equalizer"));
                            iconModelArrayList.add(new IconModel(R.drawable.ic_fast_forward, "Speed"));
                            iconModelArrayList.add(new IconModel(R.drawable.ic_subtitles, "Subtitles"));

                        }
                        iconModelArrayList.set(position, new IconModel(R.drawable.ic_left, ""));
                        playbackIconsAdapter.notifyDataSetChanged();
                        expand = true;
                    }

                }
                if (position == 1)
                {

                    if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
                    {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        playbackIconsAdapter.notifyDataSetChanged();
                    }
                    else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
                    {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        playbackIconsAdapter.notifyDataSetChanged();
                    }


                }
                if (position == 2) {
                    if(mute)
                    {
                        player.setVolume(1);
                        iconModelArrayList.set(position,new IconModel(R.drawable.ic_volume_off,"Mute"));
                        playbackIconsAdapter.notifyDataSetChanged();

                        mute=false;
                    }
                    else
                    {
                        player.setVolume(0);
                        iconModelArrayList.set(position,new IconModel(R.drawable.ic_volume,"Unmute"));
                        mute=true;
                        playbackIconsAdapter.notifyDataSetChanged();
                    }
                }
                if (position == 4) {

                    if (dark) {
                        nightMode.setVisibility(View.GONE);
                        iconModelArrayList.set(position,new IconModel(R.drawable.ic_night,"Night"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        dark = false;
                    } else {
                        nightMode.setVisibility(View.VISIBLE);
                        iconModelArrayList.set(position, new IconModel(R.drawable.ic_night, "Day"));
                        playbackIconsAdapter.notifyDataSetChanged();
                        dark = true;
                    }}
                if(position==5)
                {
                    //Controlling Volume Using SeekBar
                    VolumeDialog volumeDialog=new VolumeDialog();
                    volumeDialog.show(getSupportFragmentManager(),"dialog");
                    playbackIconsAdapter.notifyDataSetChanged();
                }
                if(position==6)
                {
                    BrightnessDialog brightnessDialog=new BrightnessDialog();
                    brightnessDialog.show(getSupportFragmentManager(),"dialog");
                    playbackIconsAdapter.notifyDataSetChanged();
                }
                if(position==7)
                {
                   /* Intent intent=new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    if((intent.resolveActivity(getPackageManager())!=null))
                    {
                        startActivityForResult(intent,123);
                    }
                    else
                    {
                        Toast.makeText(VideoPlayerActivity.this,"No Equalizer Found",Toast.LENGTH_SHORT).show();
                    }
                    playbackIconsAdapter.notifyDataSetChanged();*///This code is for BuILT iN equalizer
                    //Custom Equalizer
                    if(eqContainer.getVisibility()==View.GONE)
                    {
                        eqContainer.setVisibility(View.VISIBLE);

                    }
                    final int sessionId=player.getAudioSessionId();
                    Settings.isEditing=false;
                    EqualizerFragment equalizerFragment=EqualizerFragment.newBuilder()
                            .setAccentColor(Color.parseColor(String.valueOf("#1A78F2")))
                            .setAudioSessionId(sessionId)
                            .build();
                    getSupportFragmentManager().beginTransaction().replace(R.id.eqFrame,equalizerFragment).commit();
                    playbackIconsAdapter.notifyDataSetChanged();

                }
                if(position==8)
                {
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(VideoPlayerActivity.this);
                    alertDialog.setTitle("Select Playback Speed").setPositiveButton("OK",null);
                    String[] items={"0.5x","1x Normal Speed","1.25x","1.5x","1.75x","2x"};
                    int checkedItem=-1;
                    alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            switch(which)
                            {
                                case 0:
                                    speed=0.5f;
                                    parameters=new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 1:
                                    speed=1f;
                                    parameters=new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 2:
                                    speed=1.25f;
                                    parameters=new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 3:
                                    speed=1.5f;
                                    parameters=new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 4:
                                    speed=1.75f;
                                    parameters=new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 5:
                                    speed=2.0f;
                                    parameters=new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    AlertDialog alert =alertDialog.create();
                    alert.show();
                }
                if(position==3)
                {
                    //PopUp
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                    {
                        Rational aspectRatio=new Rational(16,9);
                        /*An immutable data type representation a rational number.

Contains a pair of ints representing the numerator and denominator of a Rational number.*/
                        pictureInpicture.setAspectRatio(aspectRatio);
                        enterPictureInPictureMode(pictureInpicture.build());
                    }
                    else
                    {
                        Log.wtf("not oreo","yes");
                    }

                }
                if(position==9)
                {
                //Subtitles
                }

            }

        });
        //Play video through URI
        playVideo();


    }


    private void playVideo() {
        String path = mVideoFilesArrayList.get(position).getPath();
        Uri uri = Uri.parse(path);
        SimpleExoPlayer.Builder builder = new SimpleExoPlayer.Builder(this);
        /*
        player = new ExoPlayer.Builder(this).build();
        instead use the below method to also set the fast forward and backward
        https://stackoverflow.com/questions/69710780/how-to-resolve-exoplayer-error-aapt-error-attribute-fastforward-increment-no
         */
        builder.setSeekForwardIncrementMs(10000);
        builder.setSeekBackIncrementMs(10000);
        player = builder.build();

        //DefaultDataSourceFactory is deprecated (8:30)
        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
        //, Util.getUserAgent(this,"app"));
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < mVideoFilesArrayList.size(); i++) {
            new File(String.valueOf(mVideoFilesArrayList.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(String.valueOf(uri))));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        //Prevent screen from dimming after screen timeout reach
        playerView.setKeepScreenOn(true);
        //Set audio attributes for audio focus
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build();
        player.setAudioAttributes(audioAttributes, true);
        player.setPlaybackParameters(parameters);//Parameters that apply to playback, including speed setting. Part of ExoPlayer
        //player.prepare(concatenatingMediaSource);
        player.setMediaSource(concatenatingMediaSource);
        player.prepare();
        player.play();
        player.seekTo(position, C.TIME_UNSET);
        //show error if media does not play
        playError();

    }
    private void screenOrientation()
    {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Bitmap bitmap;
            String path = mVideoFilesArrayList.get(position).getPath();
            Uri uri = Uri.parse(path);
            retriever.setDataSource(this, uri);
            bitmap = retriever.getFrameAtTime();
            int videoWidth = bitmap.getWidth();
            int videoHeight = bitmap.getHeight();
            if (videoWidth > videoHeight) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
        catch (Exception e)
        {
            Log.e("VideoPlayerActivity","screenOrientation: "+e);
        }
    }

    private void playError() {
        //Player.EventListener is deprecated
        player.addListener(new Player.Listener() {


            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this, "Video Playing Error", Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        if (player.isPlaying())
//            player.stop();//stop if back button is pressed
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.eqFrame);
        if(eqContainer.getVisibility()==View.GONE)
        {
            super.onBackPressed();
        }
        else
        {
            if(fragment.isVisible()&&eqContainer.getVisibility()==View.VISIBLE)
            {
                eqContainer.setVisibility(View.GONE);
            }
            else{
                if(player!=null)
                {
                    player.release();
                }
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
            if(isInPictureInPictureMode())
            {
                player.setPlayWhenReady(true);
            }
            else
            {
                player.setPlayWhenReady(false);
                player.getPlaybackState();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    private void setFullScreen()//To hide status bar when video is playing
    {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// https://stackoverflow.com/questions/12680055/what-is-the-purpose-of-requestwindowfeature
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
//                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(windowInsetsControllerCompat==null)
            return;
        windowInsetsControllerCompat.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
//        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.);
        // Hide both the status bar and the navigation bar
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_back:
                if (player != null) {
                    player.release();
                }
                finish();
                break;
            case R.id.video_list:

                PlayListDialog playListDialog=new PlayListDialog(mVideoFilesArrayList,mediaFilesAdapter);
                playListDialog.show(getSupportFragmentManager(),playListDialog.getTag());
                break;
            case R.id.lock:
                controlsMode = ControlsMode.FULLSCREEN;
                root.setVisibility(View.VISIBLE);
                lock.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Unlocked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.unlock:
                controlsMode = ControlsMode.LOCK;
                root.setVisibility(View.INVISIBLE);
                lock.setVisibility(View.VISIBLE);
                break;
            case R.id.video_more:
                PopupMenu popupMenu =new PopupMenu(this,video_more);
                MenuInflater inflater=popupMenu.getMenuInflater();
                inflater.inflate(R.menu.action_video,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id=menuItem.getItemId();
                        switch(id)
                        {
                            case R.id.share_file:
                                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                                String filepath=mVideoFilesArrayList.get(position).getPath();
                                Uri uri=Uri.parse(filepath);
                                shareIntent.setType("*/*");
                                shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                                startActivity(Intent.createChooser(shareIntent,"Share File using"));
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();

            case R.id.exo_play:
                player.play();
                break;
            case R.id.exo_pause:
                player.pause();
                break;
            case R.id.exo_next:
                try {
                    player.stop();
                    position++;
                    videoTitle = mVideoFilesArrayList.get(position).getTitle();
                    title.setText(videoTitle);
                    playVideo();
                } catch (Exception e) {
                    Toast.makeText(this, "No next Video", Toast.LENGTH_SHORT).show();
                    position--;
                    finish();//
                }
                break;
            case R.id.exo_prev:
                try {
                    player.stop();
                    position--;
                    videoTitle = mVideoFilesArrayList.get(position).getTitle();
                    title.setText(videoTitle);
                    playVideo();
                } catch (Exception e) {
                    Toast.makeText(this, "No previous Video", Toast.LENGTH_SHORT).show();
                    position++;
                    finish();
                }
                break;
        }
    }

    View.OnClickListener firstListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.fullscreen);
            Toast.makeText(VideoPlayerActivity.this, "Full Screen", Toast.LENGTH_SHORT).show();
            scaling.setOnClickListener(secondListener);
        }
    };
    View.OnClickListener secondListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.zoom);
            Toast.makeText(VideoPlayerActivity.this, "Zoom", Toast.LENGTH_SHORT).show();
            scaling.setOnClickListener(thirdListener);

        }
    };
    View.OnClickListener thirdListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.fit);
            Toast.makeText(VideoPlayerActivity.this, "Fit", Toast.LENGTH_SHORT).show();
            scaling.setOnClickListener(firstListener);

        }
    };

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        isCrossChecked=isInPictureInPictureMode;
        if(isInPictureInPictureMode)
        {
            playerView.hideController();
        }
        else
        {
            playerView.showController();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(isCrossChecked)
        {
            player.release();
            finish();
        }
    }
}