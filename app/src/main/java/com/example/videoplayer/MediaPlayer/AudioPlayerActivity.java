
package com.example.videoplayer.MediaPlayer;

//import static com.example.videoplayer.RecyclerViewClasses.MediaFilesActivity.FOLDER_NAME_KEY;
//import static com.example.videoplayer.RecyclerViewClasses.MediaFilesActivity.MEDIA_TYPE_KEY;
//import static com.example.videoplayer.RecyclerViewClasses.MediaFilesActivity.MY_PREF;
import static com.example.videoplayer.RecyclerViewClasses.MediaFilesActivity.MY_REF;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;

import com.example.videoplayer.MediaFiles;
import com.example.videoplayer.PlayListDialog;
import com.example.videoplayer.R;
import com.example.videoplayer.RecyclerViewClasses.MediaFilesAdapter;
import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CHANNEL_ID = "Notification Channel";
    private static final int NOTIFICATION_ID = 1;
    PlayerNotificationManager.Builder playerNotificationManagerbuilder;
    PlayerNotificationManager playerNotificationManager;
    Intent serviceIntent;

    private static final String TAG = AudioPlayerActivity.class.getSimpleName();
    PlayerView playerView;
    static SimpleExoPlayer player;
    RelativeLayout root;
    ImageView audioBack, audioList, audioMore;
    ImageView nextButton, previousButton,audio_more,playButton, pauseButton;

    TextView playlistTitle, exoPosition, exoDuration, title;
    ConcatenatingMediaSource concatenatingMediaSource;
    ArrayList<MediaFiles> mAudioFilesArrayList;
    int position;
    MediaFilesAdapter mediaFilesAdapter;
    PlaybackParameters parameters;
    static String audioTitle;
    static String listTitle;

    AudioService mService;
    private boolean mBound=false;
    private ServiceConnection mConneection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.LocalBinder binder = (AudioService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            initializePlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        mBound=false;
        }
    };
    void initializePlayer()
    {
        if(mBound)
        {
            Log.i(TAG,"### InitializeePlayer() called");
            player=mService.getplayerInstance();
            //mService.getplayerInstance();
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            //player.play();
            player.seekTo(position, C.TIME_UNSET);
            playError();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_audio_player);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.e(TAG + " ###", "in VideoPlayerActivity: " + e);
        }
        playerView = findViewById(R.id.exoplayer_view);
        /**playerView.setControllerShowTimeoutMs(0);//non positive value will keep it visible
        playerView.setControllerHideOnTouch(false);*/

        root = findViewById(R.id.root_layout);
        audio_more=findViewById(R.id.audio_more);
        audioBack = findViewById(R.id.audio_back);
        audioList = findViewById(R.id.audio_list);
        audioMore = findViewById(R.id.audio_more);
        nextButton = findViewById(R.id.exo_next);
        previousButton = findViewById(R.id.exo_prev);
//        playButton=findViewById(R.id.exo_play_audio);
//        pauseButton=findViewById(R.id.exo_pause_audio);
        playlistTitle = findViewById(R.id.playlist_name);
//        exoPosition = findViewById(R.id.exo_position_audio);
//        exoDuration = findViewById(R.id.exo_duration_audio);
        title=findViewById(R.id.audio_title);

        position=getIntent().getIntExtra("position",1);
        audioTitle=getIntent().getStringExtra("audio_title");
        Bundle b = getIntent().getBundleExtra(getString(R.string.BundleKey_NotificationService));
        mAudioFilesArrayList = b.getParcelableArrayList(getString(R.string.NotificationServiceList));

        title.setText(audioTitle);
        SharedPreferences preferences=this.getSharedPreferences(MY_REF, Context.MODE_PRIVATE);
        listTitle= preferences.getString("folderName","DEFAULT_FOLDER_NAME");
        //playlistTitle.setText(listTitle);

        audioList.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        audioBack.setOnClickListener(this);
        audio_more.setOnClickListener(this);
//        playButton.setOnClickListener(this);
//      pauseButton.setOnClickListener(this);

     /**createNotificationChannel();*/

//        Intent notificationIntent=new Intent(this,AudioPlayerActivity.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
//        playerNotificationManager=new PlayerNotificationManager.Builder(this,NOTIFICATION_ID,CHANNEL_ID,new DefaultMediaDescriptionAdapter(pendingIntent));





//        playerNotificationManagerbuilder=new PlayerNotificationManager.Builder(this,NOTIFICATION_ID,CHANNEL_ID).setMediaDescriptionAdapter(new DescriptionAdapter());
//        playerNotificationManagerbuilder.setNotificationListener(new NotificationListener()
//        {
//            /**
//             * Called after the notification has been cancelled.
//             *
//             * @param notificationId  The id of the notification which has been cancelled.
//             * @param dismissedByUser {@code true} if the notification is cancelled because the user
//             */
//            @Override
//            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
//                NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
//            }
//
//            /**
//             * Called each time after the notification has been posted.
//             *
//             * <p>For a service, the {@code ongoing} flag can be used as an indicator as to whether it
//             * should be in the foreground.
//             *
//             * @param notificationId The id of the notification which has been posted.
//             * @param notification   The {@link Notification}.
//             * @param ongoing        Whether the notification is ongoing.
//             */
//            @Override
//            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
//                NotificationListener.super.onNotificationPosted(notificationId, notification, false);
//            }
//        });

        //playerNotificationManager=playerNotificationManagerbuilder.build();
        /**serviceIntent=new Intent(this,AudioService.class);*/
//        Bundle serviceBundle=new Bundle();
//        serviceBundle.putParcelableArrayList(getString(R.string.NotificationServiceList),mAudioFilesArrayList);
//        serviceIntent.putExtra(getString(R.string.BundleKey_NotificationService),serviceBundle);
//        Util.startForegroundService(this,serviceIntent);
        /**playAudio();*/
        startNotificationService();




    }
    void startNotificationService()
    {
        Log.i(TAG+" ###","entered startServiceIntent");
        serviceIntent=new Intent(this,AudioService.class);
        Log.i(TAG+"###","Position In added to intent is "+position);
        serviceIntent.putExtra(getString(R.string.Position_In_ArrayList),position);
        Bundle bundle=new Bundle();
        Log.e(TAG+"###","Putting ArrayList in Bundle "+(mAudioFilesArrayList==null));
        bundle.putParcelableArrayList(getString(R.string.NotificationServiceList),mAudioFilesArrayList);
        serviceIntent.putExtra(getString(R.string.BundleKey_NotificationService),bundle);
        Util.startForegroundService(this,serviceIntent);
        playerView.setControllerHideOnTouch(false);
        playerView.setUseController(true);
        playerView.setControllerShowTimeoutMs(0);
        playerView.showController();
        playerView.setControllerAutoShow(true);
        playerView.setControllerHideOnTouch(true);
        playerView.setControllerShowTimeoutMs(40000);
        initializePlayer();
    }


/**    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }*/

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /**private void playAudio() {
        String path = mAudioFilesArrayList.get(position).getPath();
        Uri uri = Uri.parse(path);
//        PlayerView.Builder builder = new ExoPlayer.Builder(this);
//        builder.setSeekForwardIncrementMs(5000);
//        builder.setSeekBackIncrementMs(5000);
//        player = builder.build();
        SimpleExoPlayer.Builder builder=new SimpleExoPlayer.Builder(this);
        builder.setSeekForwardIncrementMs(10000);
        builder.setSeekBackIncrementMs(10000);
        player = builder.build();

        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);

        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < mAudioFilesArrayList.size(); i++) {
            new File(String.valueOf(mAudioFilesArrayList.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(String.valueOf(uri))));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        //Set audio attributes for audio focus
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes, true);
        player.setPlaybackParameters(parameters);
        //player.prepare(concatenatingMediaSource);
        player.setMediaSource(concatenatingMediaSource);
        player.prepare();

       // playerNotificationManager.setPlayer(player);

//        Bundle serviceBundle=new Bundle();
//        serviceBundle.putParcelableArrayList(getString(R.string.NotificationServiceList),mAudioFilesArrayList);
//        serviceIntent.putExtra(getString(R.string.BundleKey_NotificationService),serviceBundle);
//        Util.startForegroundService(this,serviceIntent);

        player.play();
        player.seekTo(position, C.TIME_UNSET);
        //show error if media does not play
        playError();



    }*/

    private void playError() {
        //Player.EventListener is deprecated
        player.addListener(new Player.Listener() {


            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                Toast.makeText(AudioPlayerActivity.this, "Audio Playing Error", Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }

 /**   @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player.isPlaying())
            playerNotificationManager.setPlayer(null);
            player.stop();//stop if back button is pressed
    }*/


//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (player != null) {
//            player.setPlayWhenReady(false);
//            player.getPlaybackState();
////            if (isInPictureInPictureMode()) {
////                player.setPlayWhenReady(true);
////            } else {
////                player.setPlayWhenReady(false);
////                player.getPlaybackState();
////            }
//        }
//    }
    @Override
    protected  void onStart()
    {
        super.onStart();
        /*Log.i(TAG+" ###","onStart() called");
        bindService(serviceIntent,mConneection,Context.BIND_AUTO_CREATE);
        playAudio();*/

        Log.i(TAG+" ###","onStart() called");
        getApplicationContext().bindService(serviceIntent,mConneection,Context.BIND_AUTO_CREATE);
        if(player!=null)
            player.release();
        initializePlayer();
        //setUI();
    }
//    void setUI()
//    {
//        title.setText(audioTitle);
//        playlistTitle.setText(listTitle);
//        playerView.setDefaultArtwork(AppCompatResources.getDrawable(this,R.drawable.ic_sharp_music_note_24));
//
//    }


    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        if (player.isPlaying())
            stopService(serviceIntent);
        player.stop();
        player.release();
        finish();//stop if back button is pressed

    }

    /**    @Override
    protected void onResume() {
        super.onResume();

        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }*/

    @Override
    protected void onStop() {
        Log.i(TAG+"###","Enetered onStop() called,unbinding  service!");
        getApplicationContext().unbindService(mConneection);
        mBound=false;
        finish();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        /**Log.i("AudioPlayerActivity123", "Called onDestroy() and setting player of notificationManager to null");
        Intent myService=new Intent(this,AudioService.class);
        stopService(myService);*/
//        Intent myService=new Intent(this,AudioService.class);
//        stopService(myService);
        super.onDestroy();

    }

   /** @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exo_play:
                player.play();
                break;
            case R.id.exo_pause:
                player.pause();
                break;
            case R.id.exo_next:
                try {
                    /**player.stop();
                    position++;
                    audioTitle = mAudioFilesArrayList.get(position).getTitle();
                    title.setText(audioTitle);
                    playAudio();*/

                    stopService(serviceIntent);
                    position++;
                    Log.i(TAG+"###","CLICKED NEXT BUTTON IN APP Position="+position);
                    audioTitle=mAudioFilesArrayList.get(position).getTitle();
                    title.setText(audioTitle);
                    startNotificationService();
                    //initializePlayer();
                    //player.play();

                } catch (Exception e) {
                    Toast.makeText(this, "No next audio file", Toast.LENGTH_SHORT).show();
                    position--;
                }
                break;
            case R.id.exo_prev:
                try {
                   /** player.stop();
                    position--;
                    audioTitle = mAudioFilesArrayList.get(position).getTitle();
                    title.setText(audioTitle);
                    playAudio();*/
                    player.stop();
//                    Intent intent=new Intent(AudioActivity.this,AudioService.class);
                    stopService(serviceIntent);
                    position--;
                    audioTitle = mAudioFilesArrayList.get(position).getTitle();
                    title.setText(audioTitle);
                    //initializePlayer();
                    startNotificationService();
                } catch (Exception e) {
                    Toast.makeText(this, "No previous Video", Toast.LENGTH_SHORT).show();
                    position++;
                }
                break;
            case R.id.audio_back:
                if (player != null)
//                    playerNotificationManager.setPlayer(null);
                    //player.release();
                finish();
                break;
            case R.id.audio_list:
                PlayListDialog playlistDialog = new PlayListDialog(mAudioFilesArrayList, mediaFilesAdapter);
                playlistDialog.show(getSupportFragmentManager(), playlistDialog.getTag());
                break;

            case R.id.audio_more:
                PopupMenu popupMenu = new PopupMenu(this, audio_more);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.action_video, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.share_file:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                String filepath = mAudioFilesArrayList.get(position).getPath();
                                Uri uri = Uri.parse(filepath);
                                shareIntent.setType("*/*");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(shareIntent, "Share File using"));
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

                break;
        }
    }



//    private class DescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {
//
//        /**
//         * Gets the content title for the current media item.
//         *
//         * <p>See {@link NotificationCompat.Builder#setContentTitle(CharSequence)}.
//         *
//         * @param player The {@link Player} for which a notification is being built.
//         * @return The content title for the current media item.
//         */
//        @Override
//        public CharSequence getCurrentContentTitle(Player player) {
//
//            return audioTitle;
//        }
//
//        /**
//         * Creates a content intent for the current media item.
//         *
//         * <p>See {@link NotificationCompat.Builder#setContentIntent(PendingIntent)}.
//         *
//         * @param player The {@link Player} for which a notification is being built.
//         * @return The content intent for the current media item, or null if no intent should be fired.
//         */
//        @Nullable
//        @Override
//        public PendingIntent createCurrentContentIntent(Player player) {
//            Intent notificationIntent=new Intent(getApplicationContext(),AudioPlayerActivity.class);
//           PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,PendingIntent.FLAG_MUTABLE);
//           return pendingIntent;
//        }
//
//        /**
//         * Gets the content text for the current media item.
//         *
//         * <p>See {@link NotificationCompat.Builder#setContentText(CharSequence)}.
//         *
//         * @param player The {@link Player} for which a notification is being built.
//         * @return The content text for the current media item, or null if no context text should be
//         * displayed.
//         */
//        @Nullable
//        @Override
//        public CharSequence getCurrentContentText(Player player) {
//            return null;
//        }
//
//        /**
//         * Gets the content sub text for the current media item.
//         *
//         * <p>See {@link NotificationCompat.Builder#setSubText(CharSequence)}.
//         *
//         * @param player The {@link Player} for which a notification is being built.
//         * @return The content subtext for the current media item, or null if no subtext should be
//         * displayed.
//         */
//        @Nullable
//        @Override
//        public CharSequence getCurrentSubText(Player player) {
////            return PlayerNotificationManager.MediaDescriptionAdapter.super.getCurrentSubText(player);
//            return "Description";
//        }
//
//        /**
//         * Gets the large icon for the current media item.
//         *
//         * <p>When a bitmap needs to be loaded asynchronously, a placeholder bitmap (or null) should be
//         * returned. The actual bitmap should be passed to the {@link PlayerNotificationManager.BitmapCallback} once it has been
//         * loaded. Because the adapter may be called multiple times for the same media item, bitmaps
//         * should be cached by the app and returned synchronously when possible.
//         *
//         * <p>See {@link NotificationCompat.Builder#setLargeIcon(Bitmap)}.
//         *
//         * @param player   The {@link Player} for which a notification is being built.
//         * @param callback A {@link PlayerNotificationManager.BitmapCallback} to provide a {@link Bitmap} asynchronously.
//         * @return The large icon for the current media item, or null if the icon will be returned
//         * through the {@link PlayerNotificationManager.BitmapCallback} or if no icon should be displayed.
//         */
//        @Nullable
//        @Override
//        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
//            return null;
//        }
//    }
}
