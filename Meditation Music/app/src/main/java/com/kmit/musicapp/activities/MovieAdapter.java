package com.kmit.musicapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.islamkhsh.CardSliderAdapter;
import com.kmit.musicapp.Model.Banner_Slide;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class MovieAdapter extends CardSliderAdapter<MovieAdapter.MovieViewHolder> {

    ArrayList<Banner_Slide> arrayListFeatured;
    Context context;
    public MovieAdapter(Context context,ArrayList<Banner_Slide> arrayListFeatured) {
        this.context =context;
        this.arrayListFeatured =arrayListFeatured;
    }

    @SuppressLint("ResourceType")
    @Override
    public void bindVH(@NotNull MovieViewHolder movieViewHolder, int i) {
        Banner_Slide ut = arrayListFeatured.get(i);
        movieViewHolder.card_name.setText(ut.getCategory_name());
        movieViewHolder.card_bannertext.setText(ut.getCategory_banner_text());
        Glide.with(context)
                .load(Constant.BASE_URL+ ut.getCategory_banner())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default)
                .placeholder(R.raw.music_loading)
                .dontAnimate()
                .into(movieViewHolder.img_image);

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_layout, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return arrayListFeatured.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView img_image;
        TextView card_name;
        TextView card_bannertext;

        public MovieViewHolder(View view) {
            super(view);
            img_image = (ImageView) view.findViewById(R.id.img_image);
            card_name = (TextView) view.findViewById(R.id.card_name);
            card_bannertext = (TextView) view.findViewById(R.id.card_bannertext);
        }
    }
}
