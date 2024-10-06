package com.kmit.musicapp.fragments.O_QueueFragment;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kmit.musicapp.MusicDNAApplication;
import com.kmit.musicapp.R;
import com.kmit.musicapp.Util.Constant;
import com.kmit.musicapp.activities.HomeActivity;
import com.kmit.musicapp.clickitemtouchlistener.ClickItemTouchListener;
import com.kmit.musicapp.itemtouchhelpers.SimpleItemTouchHelperCallback;
import com.squareup.leakcanary.RefWatcher;


/**
 * A simple {@link Fragment} subclass.
 */
public class QueueFragment extends Fragment implements QueueRecyclerAdapter.OnDragStartListener {

    ImageView backBtn;
    TextView fragTitle;
    TextView clearText;

    RecyclerView queueRecycler;
    QueueRecyclerAdapter qAdapter;
    LinearLayoutManager mLayoutManager2;

    ItemTouchHelper mItemTouchHelper;

    FloatingActionButton saveQueue;

    queueCallbackListener mCallback;

    ShowcaseView showCase;

    public interface queueCallbackListener {
        void onQueueItemClicked(int position);

        void onQueueSave();

        void onQueueClear();
    }

    public QueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (queueCallbackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_queue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = (ImageView) view.findViewById(R.id.queue_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        RelativeLayout adView = (RelativeLayout) view.findViewById(R.id.adView);
/*
        if (Constant.Ads) {
            if (Constant.IS_DEBUG) {
                adView.loadAd(new AdRequest.Builder().addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB").addTestDevice(homeActivity.methods.getDeviceID(homeActivity)).build());
            } else {
                adView.loadAd(new AdRequest.Builder().build());
            }
        }
*/
/*        MobileAds.initialize(this,@string/appid);


        MobileAds.initialize(homeActivity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest  adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/


        if (Constant.Ads) {

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

//Additionally to adjust the position to Bottom
            layout.setGravity(Gravity.BOTTOM);

// Create a banner ad
            AdView mAdView = new AdView(getActivity());
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(Constant.bannerads);

// Create an ad request.
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

// Optionally populate the ad request builder.
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

// Add the AdView to the view hierarchy.
            layout.addView(mAdView);

// Start loading the ad.
            mAdView.loadAd(adRequestBuilder.build());

            adView.addView(layout);

        }

        fragTitle = (TextView) view.findViewById(R.id.queue_fragment_title);
        clearText = (TextView) view.findViewById(R.id.queue_clear_text);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onQueueClear();
            }
        });

        queueRecycler = (RecyclerView) view.findViewById(R.id.queueRecycler);

        qAdapter = new QueueRecyclerAdapter(HomeActivity.queue.getQueue(), getContext(), this);
        mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        queueRecycler.setLayoutManager(mLayoutManager2);
        queueRecycler.setItemAnimator(new DefaultItemAnimator());
        queueRecycler.setAdapter(qAdapter);

        queueRecycler.addOnItemTouchListener(new ClickItemTouchListener(queueRecycler) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                mCallback.onQueueItemClicked(position);
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        saveQueue = (FloatingActionButton) view.findViewById(R.id.save_queue);
        saveQueue.setBackgroundTintList(ColorStateList.valueOf(HomeActivity.themeColor));
        saveQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onQueueSave();
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(qAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(queueRecycler);

        Button mEndButton = new Button(getContext());
        mEndButton.setBackgroundColor(HomeActivity.themeColor);
        mEndButton.setTextColor(Color.WHITE);

        showCase = new ShowcaseView.Builder(getActivity())
                .blockAllTouches()
                .singleShot(3)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .replaceEndButton(mEndButton)
                .setContentTitlePaint(HomeActivity.tp)
                .setTarget(new ViewTarget(R.id.queue_alt_showcase, getActivity()))
                .setContentTitle("Queue")
                .setContentText("Here all songs that are currently in queue are listed." +
                        " Use handle to reorder the Queue and swipe the song to remove from queue")
                .build();
        showCase.setButtonText("Next");
        showCase.setButtonPosition(HomeActivity.lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
                        lps.setMargins(margin, margin, margin, 5 + HomeActivity.navBarHeightSizeinDp);
                        showCase.setButtonPosition(lps);
                        showCase.setTarget(new ViewTarget(saveQueue.getId(), getActivity()));
                        showCase.setContentTitle("Save Queue");
                        showCase.setContentText("Save the queue as a playlist");
                        showCase.setButtonText("Done");
                        break;
                    case 2:
                        showCase.hide();
                        break;
                }
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutManager2.scrollToPositionWithOffset(HomeActivity.queueCurrentIndex, 0);
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    public boolean isShowcaseVisible() {
        return (showCase != null && showCase.isShowing());
    }

    public void hideShowcase() {
        showCase.hide();
    }

    public void updateQueueAdapter() {
        if (qAdapter != null)
            qAdapter.notifyDataSetChanged();
    }

    public void notifyAdapterItemRemoved(int i) {
        if (qAdapter != null) {
            qAdapter.notifyItemRemoved(i);
        }
    }
}
