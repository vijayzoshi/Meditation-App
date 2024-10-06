package com.kmit.musicapp.adapters.horizontalrecycleradapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kmit.musicapp.Model.Recommended;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.imageLoader.ImageLoader;

import java.util.List;

public class Fav_reco_adepter extends RecyclerView.Adapter<Fav_reco_adepter.MyViewHolder> {

    public List<Recommended> recentslyPlayed;
    Context ctx;
    ImageLoader imgLoader;
    RecycleviewClickLisetenerview recycleviewClickLisetenerview;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView backImage;
        TextView card_title;
        RelativeLayout rlitem;

        public MyViewHolder(View view) {
            super(view);
            backImage = (ImageView) view.findViewById(R.id.backImage);
            card_title = (TextView) view.findViewById(R.id.card_title);
            rlitem = (RelativeLayout) view.findViewById(R.id.rlitem);
        }
    }

    public Fav_reco_adepter(Context ctx, List<Recommended> recentslyPlayed,RecycleviewClickLisetenerview recycleviewClickLisetenerview) {
        this.recentslyPlayed = recentslyPlayed;
        this.ctx = ctx;
        this.recycleviewClickLisetenerview = recycleviewClickLisetenerview;
        imgLoader = new ImageLoader(ctx);
        notifyDataSetChanged();
        Log.e("fdghflh",""+recentslyPlayed.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fav_reco, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Recommended ut = recentslyPlayed.get(position);

        Log.e("fdghflh",""+ut.getImage());

        Glide.with(ctx)
                .load(Constant.image+ut.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default)
                .placeholder(R.drawable.ic_default)
                .thumbnail(Glide.with(ctx).load(R.raw.music_loading))
                .dontAnimate()
                .into(holder.backImage);


        holder.card_title.setText(ut.getName());
        holder.rlitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recommended ut = recentslyPlayed.get(position);

                recycleviewClickLisetenerview.onitem(ut,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }

    public interface RecycleviewClickLisetenerview {
        void onitem(Recommended played,int i);
    }


}