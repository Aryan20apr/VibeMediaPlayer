package com.example.videoplayer.RecyclerViewClasses;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoplayer.MediaFiles;
import com.example.videoplayer.R;

import java.util.ArrayList;

public class MediaFoldersAdapter extends RecyclerView.Adapter<MediaFoldersAdapter.ViewHolder> {
    private final ArrayList<MediaFiles> mediaFiles;
    private final ArrayList<String> folderPath;
    private final Context mContext;
    private final String mediaType;

    public MediaFoldersAdapter(ArrayList<MediaFiles> mediaFiles, ArrayList<String> folderPath, Context mContext,String mediaType) {
        this.mediaFiles = mediaFiles;
        this.folderPath = folderPath;
        this.mContext = mContext;
        this.mediaType=mediaType;
    }

    /**
     * Creates a new ViewHolder whenever the RecyclerView needs a new one
     * Whenever we download a video from a source, a new ViewHolder will be created
     * @param parent:
     * @param viewType:
     * @return a ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.folder_item,parent,false);
            return new ViewHolder(view);
    }

    /**
     * Update the recycler view and show the actual data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //lastIndexOf returns the position of the last / from
        //a path like /storage/Media/Videos
        int indexPath=folderPath.get(position).lastIndexOf("/");
        String nameOfFolder= folderPath.get(position).substring(indexPath+1);
        holder.folderName.setText(nameOfFolder);
        holder.folderPath.setText(folderPath.get(position));

        holder.noOfFiles.setText(noOfFiles(folderPath.get(holder.getAbsoluteAdapterPosition()))+" "+mediaType.toUpperCase());
        if(mediaType=="audio")
        Glide.with(mContext).load(R.drawable.ic_audioitem_clipart).into(holder.folderimage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to the next activity when clicked on the folder item
                Intent intent=new Intent(mContext, MediaFilesActivity.class);
                intent.putExtra("folderName",nameOfFolder);
                intent.putExtra("mediaType",mediaType);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * Return the total number of items in the adapter
     * @return
     */
    @Override
    public int getItemCount() {
       // Log.e("MediaFoldersAdapter"+"@!#","Number of items="+folderPath.size());
        return folderPath.size();

    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName, folderPath, noOfFiles;
        ImageView folderimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName=itemView.findViewById(R.id.folderName);
            folderPath=itemView.findViewById(R.id.folderPath);
            noOfFiles=itemView.findViewById(R.id.noOfFiles);
            folderimage=itemView.findViewById(R.id.Folder_image);

        }
    }
    int noOfFiles(String folder_name)
    {
        int files_no=0;
        for(MediaFiles mediaFiles:mediaFiles)
        {
            if(mediaFiles.getPath().substring(0,mediaFiles.getPath().lastIndexOf("/")).endsWith(folder_name))
            files_no++;
        }
        return files_no;
    }
}
