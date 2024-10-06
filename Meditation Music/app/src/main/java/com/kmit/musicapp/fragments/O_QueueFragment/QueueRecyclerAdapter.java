package com.kmit.musicapp.fragments.O_QueueFragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmit.musicapp.R;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.customviews.CustomPlayingIndicator;
import com.kmit.musicapp.fragments.O_PlayerFragment.PlayerFragment;
import com.kmit.musicapp.imageLoader.ImageLoader;
import com.kmit.musicapp.itemtouchhelpers.ItemTouchHelperAdapter;
import com.kmit.musicapp.itemtouchhelpers.ItemTouchHelperViewHolder;
import com.kmit.musicapp.models.LocalTrack;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;

import java.util.List;

/**
 * Created by Harjot on 16-May-16.
 */


public class QueueRecyclerAdapter extends RecyclerView.Adapter<QueueRecyclerAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private final int normal;
    private List<UnifiedTrack> queue;
    Context ctx;
    ImageLoader imgLoader;

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

    private final OnDragStartListener mDragStartListener;

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {

        ImageView art;
        TextView title, artist;
        CustomPlayingIndicator indicator;
        ImageView holderImg;

        public MyViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.img);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.url);
            indicator = (CustomPlayingIndicator) view.findViewById(R.id.currently_playing_indicator);
            holderImg = (ImageView) view.findViewById(R.id.holderImage);
        }

        @Override
        public void onItemSelected() {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = ctx.getTheme();
            theme.resolveAttribute(R.attr.background_three, typedValue, true);
            int normal = typedValue.data;

            itemView.setBackgroundColor(normal);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.BLACK);
        }
    }

    public QueueRecyclerAdapter(List<UnifiedTrack> queue, Context ctx, OnDragStartListener listener) {
        this.queue = queue;
        this.ctx = ctx;
        mDragStartListener = listener;
        imgLoader = new ImageLoader(ctx);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = ctx.getTheme();
        theme.resolveAttribute(R.attr.textcolor_f, typedValue, true);
        normal = typedValue.data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_3, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (HomeActivity.queueCurrentIndex == position && !HomeActivity.isReloaded) {
            holder.title.setTextColor(HomeActivity.themeColor);
            holder.indicator.setVisibility(View.VISIBLE);
            if (PlayerFragment.mMediaPlayer != null) {
                if (PlayerFragment.mMediaPlayer.isPlaying()) {
                    holder.indicator.play();
                } else {
                    holder.indicator.pause();
                }
            }
        } else {
            holder.title.setTextColor(normal);
            holder.indicator.setVisibility(View.INVISIBLE);
        }

        holder.holderImg.setColorFilter(HomeActivity.themeColor);
        holder.indicator.setDrawColor(HomeActivity.themeColor);

        UnifiedTrack ut = queue.get(position);
        if (ut.getType()) {
            LocalTrack lt = ut.getLocalTrack();
            imgLoader.DisplayImage(lt.getPath(), holder.art);
            holder.title.setText(lt.getTitle());
            holder.artist.setText(lt.getArtist());
        } else {
            Track t = ut.getStreamTrack();
            Glide.with(ctx)
                    .load(t.getArtworkURL())
                    .override(100, 100)
                    .error(R.drawable.ic_default)
                    .placeholder(R.drawable.ic_default)
                    .into(holder.art);
            holder.title.setText(t.getTitle());
            try {
                holder.artist.setText(t.getArtists().get(0).getArtist_name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.holderImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onDragStarted(holder);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return queue.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        UnifiedTrack prev = queue.remove(fromPosition);
        queue.add(toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
        if (fromPosition == HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex = toPosition;
        } else if (fromPosition > HomeActivity.queueCurrentIndex && toPosition == HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex++;
        } else if (fromPosition < HomeActivity.queueCurrentIndex && toPosition == HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex--;
        }
        ((HomeActivity) ctx).updateVisualizerRecycler();
        new HomeActivity.SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onItemDismiss(int position) {
        UnifiedTrack ut = queue.get(position);
        if (HomeActivity.originalQueue != null)
            HomeActivity.originalQueue.removeItem(ut);
        queue.remove(position);
        if (position < HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex--;
        }
        notifyItemRemoved(position);
        ((HomeActivity) ctx).updateVisualizerRecycler();
        new HomeActivity.SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
