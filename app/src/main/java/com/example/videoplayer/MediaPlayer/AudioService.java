package com.example.videoplayer.MediaPlayer;

//import android.app.Notification;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Binder;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationCompat.Builder;
//
//import com.example.videoplayer.MediaFiles;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.ui.PlayerNotificationManager;
//
//import java.util.ArrayList;

/*public class AudioService extends Service {

    private final IBinder binder= new LocalBinder();
    SimpleExoPlayer player;
    ArrayList<MediaFiles> audioList;
    PlayerNotificationManager.Builder playerNotificationManagerbuilder;
    PlayerNotificationManager playerNotificationManager;
    String audioTitle;
    String FolderTitle;
    private static final String CHANNEL_ID = "Notification Channel";
    private static final int NOTIFICATION_ID = 1;
    @Override
    public void onCreate() {
        super.onCreate();

        playerNotificationManagerbuilder=new PlayerNotificationManager.Builder(this,NOTIFICATION_ID,CHANNEL_ID).setMediaDescriptionAdapter(new DescriptionAdapter());
        playerNotificationManagerbuilder.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            *//**
             * Called after the notification has been cancelled.
             *
             * @param notificationId  The id of the notification which has been cancelled.
             * @param dismissedByUser {@code true} if the notification is cancelled because the user
             *//*
            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                stopSelf();
            }

            *//**
             * Called each time after the notification has been posted.
             *
             * <p>For a service, the {@code ongoing} flag can be used as an indicator as to whether it
             * should be in the foreground.
             *
             * @param notificationId The id of the notification which has been posted.
             * @param notification   The {@link Notification}.
             * @param ongoing        Whether the notification is ongoing.
             *//*
            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {

                startForeground(notificationId,notification);
            }
        });

        playerNotificationManager=playerNotificationManagerbuilder.build();
        playerNotificationManager.setUsePlayPauseActions(true);
        playerNotificationManager.setUsePreviousAction(true);
        playerNotificationManager.setUseNextAction(true);
        playerNotificationManager.setUseFastForwardAction(true);
        playerNotificationManager.setUseRewindAction(true);

        playerNotificationManager.setPlayer(AudioPlayerActivity.player);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }
    private class DescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {

        *//**
         * Gets the content title for the current media item.
         *
         * <p>See {@link Builder#setContentTitle(CharSequence)}.
         *
         * @param player The {@link Player} for which a notification is being built.
         * @return The content title for the current media item.
         *//*
        @Override
        public CharSequence getCurrentContentTitle(Player player) {

            return AudioPlayerActivity.audioTitle;
        }

        *//**
         * Creates a content intent for the current media item.
         *
         * <p>See {@link Builder#setContentIntent(PendingIntent)}.
         *
         * @param player The {@link Player} for which a notification is being built.
         * @return The content intent for the current media item, or null if no intent should be fired.
         *//*
        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            Intent notificationIntent=new Intent(getApplicationContext(),AudioPlayerActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,PendingIntent.FLAG_MUTABLE);
            return pendingIntent;
        }

        *//**
         * Gets the content text for the current media item.
         *
         * <p>See {@link Builder#setContentText(CharSequence)}.
         *
         * @param player The {@link Player} for which a notification is being built.
         * @return The content text for the current media item, or null if no context text should be
         * displayed.
         *//*
        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return null;
        }

        *//**
         * Gets the content sub text for the current media item.
         *
         * <p>See {@link Builder#setSubText(CharSequence)}.
         *
         * @param player The {@link Player} for which a notification is being built.
         * @return The content subtext for the current media item, or null if no subtext should be
         * displayed.
         *//*
        @Nullable
        @Override
        public CharSequence getCurrentSubText(Player player) {
//            return PlayerNotificationManager.MediaDescriptionAdapter.super.getCurrentSubText(player);
            return "Description";
        }

        *//**
         * Gets the large icon for the current media item.
         *
         * <p>When a bitmap needs to be loaded asynchronously, a placeholder bitmap (or null) should be
         * returned. The actual bitmap should be passed to the {@link PlayerNotificationManager.BitmapCallback} once it has been
         * loaded. Because the adapter may be called multiple times for the same media item, bitmaps
         * should be cached by the app and returned synchronously when possible.
         *
         * <p>See {@link Builder#setLargeIcon(Bitmap)}.
         *
         * @param player   The {@link Player} for which a notification is being built.
         * @param callback A {@link PlayerNotificationManager.BitmapCallback} to provide a {@link Bitmap} asynchronously.
         * @return The large icon for the current media item, or null if the icon will be returned
         * through the {@link PlayerNotificationManager.BitmapCallback} or if no icon should be displayed.
         *//*
        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            return null;
        }
    }



    @Override
    public void onDestroy() {
//        playerNotificationManager.setPlayer(null);
//        if(AudioPlayerActivity.player!=null);
//        AudioPlayerActivity.player.release();
        releasePlayer();
        super.onDestroy();
    }
    private void releasePlayer()
    {
        if(player!=null)
        {
            playerNotificationManager.setPlayer(null);
            player.release();
            player=null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    public class LocalBinder extends Binder{
        AudioService getService()
        {
            return AudioService.this;
        }
    }
}*/


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.videoplayer.MediaFiles;
import com.example.videoplayer.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

import java.io.File;
import java.util.ArrayList;

public class AudioService extends Service {
    private static final String TAG = AudioService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private SimpleExoPlayer player;
    private int position;
    PlaybackParameters parameters;
    private ArrayList<MediaFiles> mAudioFilesArrayList;
    private PlayerNotificationManager.Builder playerNotificationManagerBuilder;
    private PlayerNotificationManager playerNotificationManager;
    ConcatenatingMediaSource concatenatingMediaSource;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG+" ###","onDestroy() called");
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {

        if (player != null) {
            Log.i(TAG+" ###","Player!=null and releasePlayer called");
            playerNotificationManager.setPlayer(null);
            player.release();
            player = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public SimpleExoPlayer getplayerInstance() {
        if (player == null) {
            startPlayer();
        }
        return player;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Bundle b = intent.getBundleExtra(getString(R.string.BundleKey_NotificationService));
            mAudioFilesArrayList = b.getParcelableArrayList(getString(R.string.NotificationServiceList));
            Log.e(TAG+"#####","mAudioFilesArrayList is null"+(mAudioFilesArrayList==null));
        }
        catch(Exception e)
        {
            Log.e(AudioService.class.getSimpleName()+" ###",e.toString());
            stopSelf();
        }
        position = intent.getIntExtra(getString(R.string.Position_In_ArrayList), 1);
        Log.e(AudioService.class.getSimpleName()+"###","Position received in intent="+position);
//
        player=null;
        if(player==null)
            startPlayer();
        return START_STICKY;
    }

    private void startPlayer() {
        Log.i(TAG+" ###","startPlayer() called");
        createNotificationChannel();
        final Context context = this;

        playerNotificationManagerBuilder = new PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID);
//        playerNotificationManagerBuilder.setPlayActionIconResourceId(R.drawable.ic_play);
//        playerNotificationManagerBuilder.setPauseActionIconResourceId(R.drawable.ic_pause);
//
//        playerNotificationManagerBuilder.setFastForwardActionIconResourceId(R.drawable.ic_fast_forward);
        playerNotificationManagerBuilder.setMediaDescriptionAdapter(new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public CharSequence getCurrentContentTitle(Player player) {
                return mAudioFilesArrayList.get(position).getTitle();
            }

            @SuppressLint("UnspecifiedImmutableFlag")
            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                Intent intent = new Intent(context, AudioPlayerActivity.class);
                intent.putExtra(getString(R.string.Position_In_ArrayList), position);
                Bundle bundle = new Bundle();
                Log.e(TAG+"###","ArrayList Passed on tapping the notification is null or not "+(mAudioFilesArrayList==null));
                bundle.putParcelableArrayList(getString(R.string.NotificationServiceList), mAudioFilesArrayList);
                intent.putExtra(getString(R.string.BundleKey_NotificationService),bundle);
                Log.i(TAG+"###","In createCurrentContentIntent" );
                return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
            }

            @Nullable
            @Override
            public CharSequence getCurrentContentText(Player player) {
                return "Summary";
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return null;
            }
        });
        playerNotificationManagerBuilder.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                Log.i(TAG+" ###","onNotificationCancelled called!");
                stopSelf();

            }

            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                startForeground(notificationId, notification);
            }
        });
        playerNotificationManager=playerNotificationManagerBuilder.build();
        playerNotificationManager.setUseNextAction(true);
        playerNotificationManager.setUsePreviousAction(true);
        String path = mAudioFilesArrayList.get(position).getPath();
        Uri uri = Uri.parse(path);
//        PlayerView.Builder builder = new ExoPlayer.Builder(this);
//        builder.setSeekForwardIncrementMs(5000);
//        builder.setSeekBackIncrementMs(5000);
//        player = builder.build();
        SimpleExoPlayer.Builder builder = new SimpleExoPlayer.Builder(this);
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
        player.setMediaSource(concatenatingMediaSource);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes, true);
        player.setPlaybackParameters(parameters);

        player.prepare();
        //player.setPlayWhenReady(true);
        //player.seekTo(position, C.TIME_UNSET);
        playerNotificationManager.setPlayer(player);
    }

    private void createNotificationChannel() {
        Log.i(TAG+" ###","createNotificationChannel()");
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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }
}
