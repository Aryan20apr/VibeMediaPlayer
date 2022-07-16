package com.example.videoplayer.RecyclerViewClasses;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoplayer.MediaFiles;
import com.example.videoplayer.MediaPlayer.AudioPlayerActivity;
import com.example.videoplayer.MediaPlayer.VideoPlayerActivity;
import com.example.videoplayer.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaFilesAdapter extends RecyclerView.Adapter<MediaFilesAdapter.ViewHolder> {
    private static final int RENAME_REQUEST = 1;
    private ArrayList<MediaFiles> mediaList;
    private String mediaType;
    BottomSheetDialog bottomSheetDialog;
    private int viewType;
   // Context context;

    public MediaFilesAdapter(ArrayList<MediaFiles> mediaList, Context context, String mediaType,int viewTpe) {
        this.mediaList = mediaList;
        this.context = context;
        this.mediaType = mediaType;
        this.viewType=viewTpe;

    }

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mediaName.setText(mediaList.get(holder.getBindingAdapterPosition()).getDisplayName());
        String size = mediaList.get(holder.getBindingAdapterPosition()).getSize();//in bytes

        holder.mediaSize.setText(android.text.format.Formatter
                .formatFileSize(context, Long.parseLong(size)));
        double milliSeconds = Double.parseDouble(mediaList.get(holder.getBindingAdapterPosition()).getDuration());
        holder.mediaDuration.setText(timeConversion((long) milliSeconds));
        //Load the thumbnail.
        if(mediaType.equals("video"))
        Glide.with(context).load(new File(mediaList.get(holder.getBindingAdapterPosition()).getPath())).into(holder.thumbnail);
        else if(mediaType.equals("audio"))
            Glide.with(context).load(R.drawable.ic_audio_default).into(holder.thumbnail);
        if(viewType==0)
        {
            holder.menu_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(context.getApplicationContext(), "menu more", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog=new BottomSheetDialog(context,R.style.BottomSheetTheme);
                    View bsView=LayoutInflater.from(context).inflate(R.layout.video_bs_layout, view.findViewById(R.id.bottom_sheet));
                    bsView.findViewById(R.id.bs_play).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            holder.itemView.performClick();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bsView.findViewById(R.id.bs_rename).setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
                            alertDialog.setTitle("Rename to");
                            EditText editText=new EditText(context);
                            String path=mediaList.get(holder.getAbsoluteAdapterPosition()).getPath();
                            Log.i("MediaFilesAdapter"+"#####","path is "+path);
                            final File file=new File(path);
                            String videoName=file.getName();
                            videoName=videoName.substring(0,videoName.lastIndexOf("."));
                            editText.setText(videoName);
                            alertDialog.setView(editText);
                            editText.requestFocus();

                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.R)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(TextUtils.isEmpty(editText.getText().toString()))
                                    {Toast.makeText(context,"Can't rename empty file",Toast.LENGTH_SHORT).show();
                                        return;}
                                    String onlyPath = file.getParentFile().getAbsolutePath();
                                    String ext = file.getAbsolutePath();
                                    ext = ext.substring(ext.lastIndexOf("."));
                                    String newPath = onlyPath + "/" + editText.getText().toString() + ext;
                                    File newFile = new File(newPath);
                                    boolean rename = file.renameTo(newFile);
                                    if (rename) {
                                        ContentResolver resolver = context.getApplicationContext().getContentResolver();
                                        resolver.delete(MediaStore.Files.getContentUri("external"),
                                                MediaStore.MediaColumns.DATA + "=?", new String[]
                                                        {file.getAbsolutePath()});
                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        intent.setData(Uri.fromFile(newFile));
                                        context.getApplicationContext().sendBroadcast(intent);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Video Renamed", Toast.LENGTH_SHORT).show();
                                        SystemClock.sleep(200);//https://developer.android.com/reference/android/os/SystemClock#sleep(long)
                                        ((Activity)context).recreate(); //Refresh the app without restarting the app
                                    }
                                    else
                                        Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
                                }
//                              ContentResolver resolver = context.getContentResolver();
//                              ArrayList<Uri> mediaItems =new ArrayList<>();
//                              mediaItems.add(MediaStore.Files.getContentUri("external"));
//                              PendingIntent renamePendingIntent = MediaStore.createWriteRequest(resolver,mediaItems );
//                              try {
//                                  context.startIntentSender(renamePendingIntent.getIntentSender(),null,0,0,0);
//                              } catch (IntentSender.SendIntentException e) {
//                                  e.printStackTrace();
//                              }
//                              if()
//                              File newFile=new File(newPath);
//                              Log.i("MeidaFilesAdapter"+"#####","Write permission "+file.canWrite());
//                              boolean rename=file.renameTo(newFile);
//                              Log.i("MediaFilesAdapter"+"#####","Rename "+rename);
//                              if(rename)
//                              {
//                                  ContentResolver resolver=context.getApplicationContext().getContentResolver();
//                                  resolver.delete(MediaStore.Files.getContentUri("external"),
//                                          MediaStore.MediaColumns.DATA+"=?",new String[]
//                                                  {file.getAbsolutePath()});
//                                  Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                  intent.setData(Uri.fromFile(newFile));
//                                  context.getApplicationContext().sendBroadcast(intent);
//                                  notifyDataSetChanged();
//                                  Toast.makeText(context,"Video Renamed",Toast.LENGTH_SHORT).show();
//
//                              }
//                              else
//                                  Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
//                          }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.create().show();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bsView.findViewById(R.id.bs_share).setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            //Share Video using URI
                            Uri uri=Uri.parse(mediaList.get(holder.getAbsoluteAdapterPosition()).getPath());
                            Intent shareIntent=new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("video/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                            context.startActivity(Intent.createChooser(shareIntent,"Share Video via"));
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bsView.findViewById(R.id.bs_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
                            alertDialog.setTitle("Delete");
                            alertDialog.setMessage("Do you want to delete this video");
                            alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Uri contentUri= ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,Long.parseLong(mediaList.get(holder.getAbsoluteAdapterPosition()).getId()) );
                                    File file=new File(mediaList.get(holder.getAbsoluteAdapterPosition()).getPath());
                                    boolean delete=file.delete();
                                    if(delete)
                                    {
                                        context.getContentResolver().delete(contentUri,null,null);
                                        mediaList.remove(holder.getAbsoluteAdapterPosition());
                                        notifyItemChanged(holder.getAbsoluteAdapterPosition());
                                        notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(),mediaList.size());
                                        Toast.makeText(context,"Video Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(context,"Can't delete video",Toast.LENGTH_SHORT).show();
                                }
                            });
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alertDialog.show();
                            bottomSheetDialog.dismiss();
                        }
                    });

                    bsView.findViewById(R.id.bs_properties).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alertdialog=new AlertDialog.Builder(context);
                            alertdialog.setTitle("Properties");
                            String one="File:"+mediaList.get(holder.getBindingAdapterPosition()).getDisplayName();
                            String path=mediaList.get(holder.getBindingAdapterPosition()).getPath();
                            int indexOfPath=path.lastIndexOf("/");
                            String two="Path:"+path.substring(0,indexOfPath);
                            String three="Size:"+android.text.format.Formatter.formatFileSize(context, Long.parseLong(mediaList.get(holder.getAbsoluteAdapterPosition()).getSize()));
                            String four="Length:"+timeConversion((long)milliSeconds);
                            String namewithFormat=mediaList.get(holder.getAbsoluteAdapterPosition()).getDisplayName();
                            int index=namewithFormat.lastIndexOf(".");
                            String format=namewithFormat.substring(index+1);
                            String five="Format:"+format;
                            MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(mediaList.get(holder.getAbsoluteAdapterPosition()).getPath());
                            String height=mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                            String width=mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                            String six="Resolution:"+width+"x"+height;
                            alertdialog.setMessage(one+"\n\n"+two+"\n\n"+three+"\n\n"+four+"\n\n"+five+"\n\n"+six);

                            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alertdialog.show();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bottomSheetDialog.setContentView(bsView);
                    bottomSheetDialog.show();
                }
            });
        }
        else
        {
            holder.menu_more.setVisibility(View.GONE);
            holder.mediaName.setTextColor(Color.WHITE);
            holder.mediaSize.setTextColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MediaFilesAdapter"+"!@#","Inside onClick() of holder");
                if (mediaType.equals("video")) {

                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    //Get position of video and arraylist of video
                    intent.putExtra("position", holder.getBindingAdapterPosition());//passing only 'position' gives error "Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later"
                    intent.putExtra("video_title", mediaList.get(holder.getBindingAdapterPosition()).getDisplayName());//Video name for showing video titlw in title bar
                    //For sending Videolist through intent, we use Bundle to send Video list to videoPlayerActivity
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("videoArrayList",mediaList);
                    intent.putExtras(bundle);
                    Log.i("MediaFilesAdapter"+"!@#","Starting Activity");

                    context.startActivity(intent);
                    if(viewType==1)
                    {
                        ((Activity)context).finish();
                    }


                } else {
                    Intent intent = new Intent(context, AudioPlayerActivity.class);
                    //Get position of video and arraylist of video
                    intent.putExtra("position", holder.getBindingAdapterPosition());//passing only 'position' gives error "Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later"
                    intent.putExtra("audio_title", mediaList.get(holder.getBindingAdapterPosition()).getDisplayName());//Video name for showing video titlw in title bar
                    //For sending Videolist through intent, we use Bundle to send Video list to videoPlayerActivity
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList(context.getString(R.string.NotificationServiceList), mediaList);
                    intent.putExtra(context.getString(R.string.BundleKey_NotificationService),bundle);
                    Log.i("MediaFilesAdapter"+"!@#","Starting Activity");

                    context.startActivity(intent);
                    if(viewType==1)
                    {
                        ((Activity)context).finish();
                    }
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, menu_more;
        TextView mediaName, mediaSize, mediaDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            menu_more = itemView.findViewById(R.id.media_menu_more);
            mediaName = itemView.findViewById(R.id.media_name);
            mediaSize = itemView.findViewById(R.id.media_size);
            mediaDuration = itemView.findViewById(R.id.media_duration);
        }
    }


    public String timeConversion(long value) {
        String mediaTime;
        int duration = (int) value;
        int hrs = (duration / 3600000);
        int mns = (duration / 60000) % 60000;
        int scs = duration % 60000 / 1000;
        if (hrs > 0)
            mediaTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        else
            mediaTime = String.format("%02d:%02d", mns, scs);
        return mediaTime;
    }
    void updateVideoFiles(ArrayList<MediaFiles> files)
    {
        mediaList=new ArrayList<>();
        mediaList.addAll(files);
        notifyDataSetChanged();

    }
}
