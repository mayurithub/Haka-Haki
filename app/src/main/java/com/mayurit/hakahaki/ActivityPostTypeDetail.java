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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.Model.NewsListModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPostTypeDetail extends AppCompatActivity {

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    int page_no;
    RelativeLayout rel_container;
    String post_id,post_type;
    NewsListModel post;

    TextView txt_title,txt_date,txt_like_count;
    ImageView img_full;
    WebView web_description;

    private ProgressBar spinner;
    SwipeRefreshLayout swipe_refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent intent = getIntent();
        refresh();
        loading(true);
        post_id= intent.getExtras().getString("post_id");
        post_type= intent.getExtras().getString("post_type");


        rel_container = (RelativeLayout) findViewById(R.id.rel_container);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_like_count = (TextView) findViewById(R.id.txt_like_count);
        img_full = (ImageView) findViewById(R.id.img_full);
        web_description = (WebView) findViewById(R.id.web_description);
        if(!post_type.equals("page")) {
            post = (NewsListModel) getIntent().getSerializableExtra(EXTRA_OBJC);
            displayApiResult(post);
        }

        netCheck();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.action_settings) {
            onBackPressed();
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
        Log.i("dtat",post_id+"="+post_type);
        Call<NewsListModel> noticeList = RetrofitAPI.getService().getNEEFEJDetail(post_type,post_id);
        noticeList.enqueue(new Callback<NewsListModel>() {
            @Override
            public void onResponse(Call<NewsListModel> call, Response<NewsListModel> response) {

                NewsListModel resp = response.body();
                if (resp != null) {
                    displayResult(resp);
                    loading(false);
                    swipe_refresh_layout.setRefreshing(false);

                } else {
                    netCheck();
                }


            }

            @Override
            public void onFailure(Call<NewsListModel> call, Throwable throwable) {
                Toast.makeText(ActivityPostTypeDetail.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void displayApiResult(NewsListModel posts) {
        txt_title.setText(posts.getPostTitle());
        txt_date.setText(posts.getPostDate());


        txt_like_count.setText(""+posts.getLikeCount());
        if(posts.getLikeCount()==null){
            txt_like_count.setText("");
        }
        web_description.loadData(posts.getPostExcerpt(),"text/html; charset=utf-8","utf-8");
        Glide.with(getApplicationContext()).load(posts.getImageId()).into(img_full);
    }


    private void displayResult(NewsListModel posts) {
        txt_title.setText(posts.getPostTitle());
        txt_date.setText(posts.getPostDate());


        txt_like_count.setText(""+posts.getLikeCount());
        if(posts.getLikeCount().equals("null")){
            txt_like_count.setText("");
        }

        String html_data = "<style>img{max-width:100%;height:auto; margin-bottom:10px;} iframe{width:100%;}</style> ";
        html_data += posts.getPostContent();
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

    public static void methodShare(Activity act, NewsListModel p) {
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

    public void   loading(boolean show){
        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        // spinner.setVisibility(View.GONE);

        if(show){
            spinner.setVisibility(View.VISIBLE);
        }else{
            spinner.setVisibility(View.GONE);
        }
        //b1=(Button)findViewById(R.id.button);




    }
    public void refresh(){
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(true);
                fetchData();
            }
        });

    }
}
