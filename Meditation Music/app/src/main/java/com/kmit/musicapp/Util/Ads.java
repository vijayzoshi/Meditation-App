package com.kmit.musicapp.Util;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Ads {
    public static InterstitialAd mInterstitialAd;
    public static OnClose  onClose;

    public static void Load(Context context){

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(Constant.interstitialads);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public static void lisoner(OnClose onClos){
        onClose = onClos;
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                onClose.onItemClick();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        mInterstitialAd.show();
    }



    public interface OnClose {
        void onItemClick();
    }

}
