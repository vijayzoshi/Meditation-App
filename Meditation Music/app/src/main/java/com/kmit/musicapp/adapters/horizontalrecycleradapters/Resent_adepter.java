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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kmit.musicapp.Model.Play_Artist;
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

public class Resent_adepter extends RecyclerView.Adapter<Resent_adepter.MyViewHolder> {

    public ArrayList<Played> recentslyPlayed;
    private List<Track> recentslyPlayed1;
    Context ctx;
    ImageLoader imgLoader;
    RecycleviewClickLisetenerview recycleviewClickLisetenerview;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LikeButton iv_like;
        ImageView img_2;
        TextView title_2, url_2, tv_lick;
        RelativeLayout rlitem;

        public MyViewHolder(View view) {
            super(view);
            iv_like = (LikeButton) view.findViewById(R.id.iv_like);
            img_2 = (ImageView) view.findViewById(R.id.img_2);
            title_2 = (TextView) view.findViewById(R.id.title_2);
            url_2 = (TextView) view.findViewById(R.id.url_2);
            tv_lick = (TextView) view.findViewById(R.id.tv_lick);
            rlitem = (RelativeLayout) view.findViewById(R.id.rlitem);
        }
    }

    public Resent_adepter(Context ctx, ArrayList<Played> recentslyPlayed, RecycleviewClickLisetenerview recycleviewClickLisetenerview) {
        this.recentslyPlayed = recentslyPlayed;
        this.ctx = ctx;
        this.recycleviewClickLisetenerview = recycleviewClickLisetenerview;
        imgLoader = new ImageLoader(ctx);
        recentslyPlayed1 = new ArrayList<>();
        for(int i = 0; i<recentslyPlayed.size(); i++){
            Played example = recentslyPlayed.get(i);
            String time = example.getMusic_duration(); //mm:ss
            String[] units = time.split(":"); //will break the string up into an array
            int minutes = Integer.parseInt(units[0]); //first element
            int seconds = Integer.parseInt(units[1]); //second element
            int duration = (60 * minutes + seconds)*1000; //add up our values

            Track track = new Track(example.getMusic_title()
                    ,Integer.parseInt(example.getMusic_id())
                    ,duration
                    ,Constant.image+example.getMusic_file()
                    ,Constant.image+example.getMusic_image()
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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_resent, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Played ut = recentslyPlayed.get(position);
        if(ut.getIs_liked().equals("1")){
            holder.iv_like.setLiked(true);
        }

        holder.iv_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (Constant.get_profile_id(ctx).equals("0")) {
                    holder.iv_like.setLiked(false);
                    recycleviewClickLisetenerview.chack();
                } else {
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

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (Constant.get_profile_id(ctx).equals("0")) {
                    holder.iv_like.setLiked(true);
                    recycleviewClickLisetenerview.chack();
                } else {
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

            }
        });
        Glide.with(ctx)
                .load(Constant.image + ut.getMusic_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default)
                .placeholder(R.drawable.ic_default)
                .thumbnail(Glide.with(ctx).load(R.raw.music_loading))
                .dontAnimate()
                .into(holder.img_2);

        holder.tv_lick.setText("(" + ut.getLike_count() + ")");

        holder.title_2.setText(ut.getMusic_title());
        holder.rlitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track ut = recentslyPlayed1.get(position);

                recycleviewClickLisetenerview.onitem(ut,position,recentslyPlayed1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }

    public interface RecycleviewClickLisetenerview {
        void onitem(Track played , int position, List<Track> tracks);
        void chack();
    }


}
