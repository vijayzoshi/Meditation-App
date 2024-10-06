package com.kmit.musicapp.adapters.horizontalrecycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kmit.musicapp.Model.Category;
import com.kmit.musicapp.Model.Popular_Albums;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.imageLoader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryAdepter extends RecyclerView.Adapter<AllCategoryAdepter.MyViewHolder> {

    private final int type;
    public List<Category> recentslyPlayed;
    Context ctx;
    ImageLoader imgLoader;
    RecycleviewClickLisetenerview recycleviewClickLisetenerview;

    private final List<Category> fullList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView art;
        TextView title;
        RelativeLayout bottomHolder;
        RelativeLayout rlitem;

        public MyViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.backImage);
            title = (TextView) view.findViewById(R.id.card_title);
            bottomHolder = (RelativeLayout) view.findViewById(R.id.bottomHolder);
            rlitem = (RelativeLayout) view.findViewById(R.id.rlitem);
        }
    }

    public AllCategoryAdepter(Context ctx,int type,List<Category> recentslyPlayed, RecycleviewClickLisetenerview recycleviewClickLisetenerview) {
        this.type = type;
        this.fullList.addAll(recentslyPlayed);
        this.recentslyPlayed = recentslyPlayed;
        this.ctx = ctx;
        this.recycleviewClickLisetenerview = recycleviewClickLisetenerview;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (type) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_home_category, parent, false);
                break;
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_all_category, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_all_category, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Category ut = recentslyPlayed.get(position);


        Glide.with(ctx).load(Constant.image + ut.getCategory_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default)
                .placeholder(R.raw.music_loading)
//                .thumbnail(Glide.with(ctx).load(String.valueOf(R.raw.music_loading)))
                .dontAnimate()
                .into(holder.art);

        holder.title.setText(ut.getCategory_name());
        holder.rlitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category ut = recentslyPlayed.get(position);

                recycleviewClickLisetenerview.onitem(ut);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }

    public interface RecycleviewClickLisetenerview {
        void onitem(Category popular_albums);
    }


}

