package com.kmit.musicapp.adapters.horizontalrecycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmit.musicapp.R;
import com.kmit.musicapp.imageLoader.ImageLoader;
import com.kmit.musicapp.models.LocalTrack;
import com.kmit.musicapp.models.Track;
import com.kmit.musicapp.models.UnifiedTrack;

import java.util.List;

/**
 * Created by Harjot on 15-May-16.
 */
public class RecentsListHorizontalAdapter extends RecyclerView.Adapter<RecentsListHorizontalAdapter.MyViewHolder> {

    private List<UnifiedTrack> recentslyPlayed;
    Context ctx;
    ImageLoader imgLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView art;
        TextView title, artist;
        RelativeLayout bottomHolder;

        public MyViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.backImage);
            title = (TextView) view.findViewById(R.id.card_title);
            artist = (TextView) view.findViewById(R.id.card_artist);
            bottomHolder = (RelativeLayout) view.findViewById(R.id.bottomHolder);
        }
    }

    public RecentsListHorizontalAdapter(List<UnifiedTrack> recentslyPlayed, Context ctx) {
        this.recentslyPlayed = recentslyPlayed;
        this.ctx = ctx;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_layout2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        UnifiedTrack ut = recentslyPlayed.get(position);
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
            holder.artist.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }


}
