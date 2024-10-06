package com.kmit.musicapp.adapters.horizontalrecycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmit.musicapp.Model.Own_Play_List;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.Util.RoundedImageView;
import com.kmit.musicapp.imageLoader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class Own_play_adepter extends RecyclerView.Adapter<Own_play_adepter.MyViewHolder> {

    private List<Own_Play_List> recentslyPlayed;
    Context ctx;
    ImageLoader imgLoader;
    RecycleviewClickLisetenerview recycleviewClickLisetenerview;
    ArrayList<String> image;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView iv_one;
        RoundedImageView iv_two;
        RoundedImageView iv_three;
        RoundedImageView iv_four;
        LinearLayout lil_main;

        public MyViewHolder(View view) {
            super(view);
            iv_one = (RoundedImageView) view.findViewById(R.id.iv_one);
            iv_two = (RoundedImageView) view.findViewById(R.id.iv_two);
            iv_three = (RoundedImageView) view.findViewById(R.id.iv_three);
            iv_four = (RoundedImageView) view.findViewById(R.id.iv_four);
            lil_main = (LinearLayout) view.findViewById(R.id.lil_main);
        }
    }

    public Own_play_adepter(Context ctx, List<Own_Play_List> recentslyPlayed, RecycleviewClickLisetenerview recycleviewClickLisetenerview) {
        this.recentslyPlayed = recentslyPlayed;
        this.ctx = ctx;
        this.recycleviewClickLisetenerview = recycleviewClickLisetenerview;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_own_playlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Own_Play_List ut = recentslyPlayed.get(position);
        image = ut.getImages();
        if(image.size() == 3){

            Glide.with(ctx)
                    .load(Constant.image + image.get(2))
                    .placeholder(R.drawable.ic_songs)
                    .error(R.drawable.ic_songs)
                    .into(holder.iv_three);
        }
        if(image.size() >= 2){

            Glide.with(ctx)
                    .load(Constant.image + image.get(1))
                    .placeholder(R.drawable.play_btn)
                    .error(R.drawable.ic_default)
                    .into(holder.iv_two);
        }
        if(image.size() >= 1){

            Glide.with(ctx)
                    .load(Constant.image + image.get(0))
                    .placeholder(R.drawable.play_btn)
                    .error(R.drawable.ic_default)
                    .into(holder.iv_one);
        }

        holder.lil_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Own_Play_List ut = recentslyPlayed.get(position);

                recycleviewClickLisetenerview.onitem_press(ut);
            }
        });
        holder.lil_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean  onLongClick(View v) {
                Own_Play_List ut = recentslyPlayed.get(position);

                recycleviewClickLisetenerview.onitem_longpress(ut);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }

    public interface RecycleviewClickLisetenerview {
        void onitem_press(Own_Play_List played);
        void onitem_longpress(Own_Play_List played);
    }


}
