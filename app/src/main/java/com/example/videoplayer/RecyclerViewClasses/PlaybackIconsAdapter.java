package com.example.videoplayer.RecyclerViewClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.R;

import java.util.ArrayList;

public class PlaybackIconsAdapter extends RecyclerView.Adapter<PlaybackIconsAdapter.ViewHolder>
{
    private ArrayList<IconModel>iconmodelList;
    private Context context;
    private OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);

    }
    public void setOnItemClickListner(OnItemClickListener listener)
    {
    mListener=listener;
    }

    public PlaybackIconsAdapter(ArrayList<IconModel> iconmodelList, Context context) {
        this.iconmodelList = iconmodelList;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public PlaybackIconsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.icon_layout,parent,false);

        return new ViewHolder(view,mListener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getBindingAdapterPosition()} which
     * will have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull PlaybackIconsAdapter.ViewHolder holder, int position) {
        holder.icon.setImageResource(iconmodelList.get(holder.getBindingAdapterPosition()).getImageView());
        holder.iconName.setText(iconmodelList.get(position).getIconTitle());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return iconmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView iconName;
        ImageView icon;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            icon=itemView.findViewById(R.id.playback_icon);
            iconName=itemView.findViewById(R.id.icon_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position=getBindingAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)/*NO_POSITION is a constant whose
                        value is -1, it basically means that when don't you find the position of the model in the underlying dataset the return value of this method will be NO_POSITION*/
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
