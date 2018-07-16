package com.mayurit.hakahaki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.Model.ProjectModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProjectDetail extends AppCompatActivity {

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    int page_no;
    RelativeLayout rel_container;
    String post_id;
    ProjectModel post;

    TextView txt_title,txt_date,txt_like_count;
    ImageView img_full;
    WebView web_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        post = (ProjectModel) getIntent().getSerializableExtra(EXTRA_OBJC);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Log.i("postdatax1",post.getPostTitle());
        rel_container = (RelativeLayout) findViewById(R.id.rel_container);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_like_count = (TextView) findViewById(R.id.txt_like_count);
        img_full = (ImageView) findViewById(R.id.img_full);
        web_description = (WebView) findViewById(R.id.web_description);
        displayApiResult(post);

        netCheck();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == android.R.id.home) {
            onBackPressed();
        } else if (item_id == R.id.action_share) {
            methodShare(ActivityProjectDetail.this, post);
        }  else if (item_id == R.id.action_browser) {
//            directLinkToBrowser(this, post.url);
            Toast.makeText(this, "link to browser", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

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

//        Call<List<ProjectModel>> noticeList = RetrofitAPI.getService().getPostDetail("7300");
        Call<List<ProjectModel>> noticeList = RetrofitAPI.getService().getProjectDetail(post.getID());
        noticeList.enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, Response<List<ProjectModel>> response) {

                List<ProjectModel> resp = response.body();
                if (resp != null) {
                    displayResult(resp.get(0));

                } else {
                    showNoItemView(true);
                }


            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable throwable) {
                Toast.makeText(ActivityProjectDetail.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void displayApiResult(ProjectModel posts) {
        txt_title.setText(posts.getPostTitle());
        txt_date.setText(posts.getPostDate());


        txt_like_count.setText(""+posts.getLikeCount());
        if(posts.getLikeCount()==null){
            txt_like_count.setText("");
        }
//        web_description.loadData(posts.getPostExcerpt(),"text/html; charset=utf-8","utf-8");
        Glide.with(getApplicationContext()).load(posts.getImageId()).into(img_full);
    }


    private void displayResult(ProjectModel posts) {
        txt_title.setText(posts.getPostTitle());
        txt_date.setText(posts.getPostDate());


        txt_like_count.setText(""+posts.getLikeCount());
        if(posts.getLikeCount().equals("null")){
            txt_like_count.setText("");
        }

        String html_data = "<style>img{max-width:100%;height:auto; margin-bottom:10px;} iframe{width:100%;}</style> ";
        html_data += posts.getPost_content();
        web_description.getSettings().setJavaScriptEnabled(true);
        web_description.getSettings();
        web_description.getSettings().setBuiltInZoomControls(true);
        web_description.setBackgroundColor(Color.TRANSPARENT);
        web_description.setWebChromeClient(new WebChromeClient());
        web_description.loadData(html_data, "text/html; charset=UTF-8", "utf-8");
        // disable scroll on touch
        web_description.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        Glide.with(getApplicationContext()).load(posts.getImageId()).into(img_full);
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

    public static void methodShare(Activity act, ProjectModel p) {
        Uri uri = Uri.parse(p.getImageId());

        // string to share
        StringBuilder sb = new StringBuilder();
        sb.append("Read Article \'" + p.getPostTitle() + "\'\n");
        sb.append("Using app \'" + act.getString(R.string.app_name) + "\'\n");
//        sb.append("Source : " + p.url + "");

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, act.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        //sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        act.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }
}
