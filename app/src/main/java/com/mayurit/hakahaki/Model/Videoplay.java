package com.mayurit.hakahaki.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mayurit.hakahaki.Config;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.R;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Videoplay extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    //changes
    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";
    int page_no;
    RelativeLayout rel_container;
    String post_id;
    VideoModel post;
    TextView vid_title1, expandableTextView_description, vid_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);



        //changes
        vid_title1 = (TextView) findViewById(R.id.vid_title1);
        expandableTextView_description = (TextView) findViewById(R.id.expandableTextView_description);
        vid_date = (TextView) findViewById(R.id.vid_date);
        Intent intent = getIntent();
        post = (VideoModel) getIntent().getSerializableExtra(EXTRA_OBJC);
        displayResult(post);

        netCheck();
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //trying to implement the video play




    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(post.getYoutube_id()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }


    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
           // String error = String.format(getString(R.string.player_error), errorReason.toString());
           // Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    //changes
    public void netCheck() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            fetchData();
        } else {
            Snackbar snackbar = Snackbar.make(rel_container, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    netCheck();
                }
            });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

    public void fetchData() {
        Call<VideoModel> noticeList = RetrofitAPI.getService().getVideoDetail("video", post.getID());
        noticeList.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {

                VideoModel resp = response.body();
                if (resp != null) {
                    displayResult1(resp);

                } else {
                    showNoItemView(true);
                }
            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable throwable) {

            }
        });
    }
    private void displayResult(VideoModel posts) {
        vid_title1.setText(posts.getPostTitle());
        vid_date.setText(posts.getPostDate());
        expandableTextView_description.setText(posts.getPostExcerpt());
    }

    private void displayResult1(VideoModel posts) {
        vid_title1.setText(posts.getPostTitle());
        vid_date.setText(posts.getPostDate());
        expandableTextView_description.setText(posts.getPostContent());
    }
    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) findViewById(R.id.lyt_no_item_category);
        ((TextView) findViewById(R.id.no_item_message)).setText(R.string.no_category);
        if (show) {
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            lyt_no_item.setVisibility(View.GONE);
        }
    }
    public static void directLinkToBrowser(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_LONG).show();
        }
    }
}

