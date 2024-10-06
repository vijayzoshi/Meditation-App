package com.kmit.musicapp.adapters.horizontalrecycleradapters;


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
import com.kmit.musicapp.Model.Played;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.asyncTask.Asy_Like_Unlile_Addplaylist;
import com.kmit.musicapp.imageLoader.ImageLoader;
import com.kmit.musicapp.models.Track;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class Top_play_adepter extends RecyclerView.Adapter<Top_play_adepter.MyViewHolder> implements Filterable {

    private final int type;
    public List<Played> recentslyPlayed;
    private List<Track> recentslyPlayed1;
    Context ctx;
    ImageLoader imgLoader;
    RecycleviewClickLisetenerview recycleviewClickLisetenerview;

    private final List<Played> fullList = new ArrayList<>();

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) { // if your editText field is empty, return full list of FriendItem
                    results.count = fullList.size();
                    results.values = fullList;
                } else {
                    List<Played> filteredList = new ArrayList<>();

                    constraint = constraint.toString().toLowerCase(); // if we ignore case
                    for (Played item : fullList) {
                        String firstName = item.getCategory_name().toLowerCase(); // if we ignore case
                        if (firstName.contains(constraint.toString())) {
                            filteredList.add(item); // added item witch contains our text in EditText
                        }
                    }

                    results.count = filteredList.size(); // set count of filtered list
                    results.values = filteredList; // set filtered list
                }
                return results; // return our filtered list
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                recentslyPlayed = (List<Played>) results.values; // replace list to filtered list

                recentslyPlayed1.clear();
                for (int i = 0; i < recentslyPlayed.size(); i++) {
                    Played example = recentslyPlayed.get(i);
                    String time = example.getMusic_duration(); //mm:ss
                    String[] units = time.split(":"); //will break the string up into an array
                    int minutes = Integer.parseInt(units[0]); //first element
                    int seconds = Integer.parseInt(units[1]); //second element
                    int duration = (60 * minutes + seconds) * 1000; //add up our values
                    Track track = new Track(example.getMusic_title()
                            , Integer.parseInt(example.getMusic_id())
                            , duration
                            , Constant.image + example.getMusic_file()
                            , Constant.image + example.getMusic_image()
                    );
                    track.setCategory_id(example.getCategory_id());
                    track.setPlayCount(example.getPlayCount());
                    track.setCategory_name(example.getCategory_name());
                    track.setIs_liked(example.getIs_liked());
                    track.setIs_in_playlist(example.getIs_in_playlist());
                    track.setLike_count(example.getLike_count());

                    recentslyPlayed1.add(track);
                }
                notifyDataSetChanged(); // refresh adapter
            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView art;
        TextView title;
        RelativeLayout bottomHolder;
        RelativeLayout rlitem;
        LikeButton iv_like;

        public MyViewHolder(View view) {
            super(view);
            iv_like = (LikeButton) view.findViewById(R.id.iv_like);
            art = (ImageView) view.findViewById(R.id.backImage);
            title = (TextView) view.findViewById(R.id.card_title);
            bottomHolder = (RelativeLayout) view.findViewById(R.id.bottomHolder);
            rlitem = (RelativeLayout) view.findViewById(R.id.rlitem);
        }
    }

    public Top_play_adepter(Context ctx, int type, List<Played> recentslyPlayed, RecycleviewClickLisetenerview recycleviewClickLisetenerview) {
        this.recentslyPlayed = recentslyPlayed;
        this.type = type;
        this.ctx = ctx;
        this.fullList.addAll(recentslyPlayed);

        this.recycleviewClickLisetenerview = recycleviewClickLisetenerview;
        imgLoader = new ImageLoader(ctx);
        recentslyPlayed1 = new ArrayList<>();
        for (int i = 0; i < recentslyPlayed.size(); i++) {
            Played example = recentslyPlayed.get(i);
            String time = example.getMusic_duration(); //mm:ss
            String[] units = time.split(":"); //will break the string up into an array
            int minutes = Integer.parseInt(units[0]); //first element
            int seconds = Integer.parseInt(units[1]); //second element
            int duration = (60 * minutes + seconds) * 1000; //add up our values
            Track track = new Track(example.getMusic_title()
                    , Integer.parseInt(example.getMusic_id())
                    , duration
                    , Constant.image + example.getMusic_file()
                    , Constant.image + example.getMusic_image()
            );
            track.setCategory_id(example.getCategory_id());
            track.setPlayCount(example.getPlayCount());
            track.setCategory_name(example.getCategory_name());
            track.setIs_liked(example.getIs_liked());
            track.setIs_in_playlist(example.getIs_in_playlist());
            track.setLike_count(example.getLike_count());

            recentslyPlayed1.add(track);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (type) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_home_top_play, parent, false);
                break;
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_search_music, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_home_most_play, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Played ut = recentslyPlayed.get(position);
/*
        Glide.with(ctx)
                .load(ut.getMusic_image())
                .override(100, 100)
                .error(R.drawable.ic_default)
                .placeholder(R.drawable.ic_default)
                .into(holder.art);
*/

        if (ut.getIs_liked().equals("1")) {
            holder.iv_like.setLiked(true);
        }

        holder.iv_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                new Asy_Like_Unlile_Addplaylist(HomeActivity.methods.getAPIRequest(Constant.METHOD_Like, Constant.get_profile_id(ctx), Constant.like_Music, ut.getMusic_id(), "", "")
                        , Constant.Like
                        , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd(String success) {

                        if (success.equals("0")) {
                            holder.iv_like.setLiked(false);
                        }
                    }
                }).execute();
            }


            @Override
            public void unLiked(LikeButton likeButton) {
                new Asy_Like_Unlile_Addplaylist(HomeActivity.methods.getAPIRequest(Constant.METHOD_Like, Constant.get_profile_id(ctx), Constant.like_Music, ut.getMusic_id(), "", "")
                        , Constant.Unlike
                        , new Asy_Like_Unlile_Addplaylist.Listener_Artist() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd(String success) {
                        if (success.equals("0")) {
                            holder.iv_like.setLiked(true);
                        }
                    }
                }
                ).execute();
            }

        });

        Glide.with(ctx)
                .load(Constant.image + ut.getMusic_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default)
                .placeholder(R.drawable.ic_default)
                .thumbnail(Glide.with(ctx).load(R.raw.music_loading))
                .dontAnimate()
                .into(holder.art);

        holder.title.setText(ut.getMusic_title());
        holder.rlitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track ut = recentslyPlayed1.get(position);

                recycleviewClickLisetenerview.onitem(ut, position, recentslyPlayed1);

            }
        });

    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }

    public interface RecycleviewClickLisetenerview {
        void onitem(Track played, int position, List<Track> tracks);
    }


}
