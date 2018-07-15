package com.mayurit.hakahaki;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mayurit.hakahaki.Helpers.Constant;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.Model.AudioModel;
import com.mayurit.hakahaki.Model.NewsListModel;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mayurit.hakahaki.AudioDetail.EXTRA_OBJC;

public class AudioActivity extends AppCompatActivity {

    String post_id;
    ImageButton btnplay, btnpause;
    private MediaPlayer mediaPlayer;
    private boolean playPause;
    TextView audio_title, audio_description, audio_date;
    private boolean initialStage = true;
    AudioModel post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        audio_title = findViewById(R.id.audio_title);
        audio_description = findViewById(R.id.audio_description);
        audio_date = findViewById(R.id.audio_date);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btnplay = findViewById(R.id.btnplay);
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playPause) {
                    btnplay.setImageResource(R.drawable.ic_pause);
                    if (initialStage) {
                        new Player().execute("http://streaming.hamropatro.com:8004/;stream.mp3&13202692901&duration=99999&id=scplayer&autostart=true");
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }
                    playPause = true;

                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnplay.setImageResource(R.drawable.ic_play);
                    }

                    playPause = false;
                }
            }
        });


    //    displayAudioDetail();
     //   fetchData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        btnplay.setImageResource(R.drawable.ic_play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp",e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public void fetchData() {

        Call<AudioModel> noticeList = RetrofitAPI.getService().getAudioDetail("audio",post.getID());
        noticeList.enqueue(new Callback<AudioModel>() {
            @Override
            public void onResponse(Call<AudioModel> call, Response<AudioModel> response) {
                post = (AudioModel) response.body();
                if (post != null) {
                    displayAudioDetail();

                } else {
                    // showNoItemView(true);
                }
            }


            @Override
            public void onFailure(Call<AudioModel> call, Throwable throwable) {
             //   Toast.makeText(AudioActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void displayAudioDetail(){
        audio_title.setText(post.getPostTitle());
        audio_date.setText(post.getPostDate());
        audio_description.setText(post.getPostContent());

    }
}
