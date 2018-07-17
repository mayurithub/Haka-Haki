package com.mayurit.hakahaki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.mayurit.hakahaki.Adapters.CategoryNewsListAdapter;
import com.mayurit.hakahaki.Helpers.Constant;
import com.mayurit.hakahaki.Helpers.RecyclerItemClickListener;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.Model.NewsListModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPostDetail extends AppCompatActivity {

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    AQuery aQuery;
    int page_no;
    RelativeLayout rel_container;
    String post_id;
    NewsListModel post;

    TextView txt_title, txt_date, txt_like_count,date,txt_guest_author;
    ImageView img_full,ic_fb,ic_tweet,like;
    WebView web_description;
    private ProgressBar spinner;
    Button b1;
    SwipeRefreshLayout swipe_refresh_layout;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_post_detail);
        Intent intent = getIntent();
        refresh();
        loading(true);
        post = (NewsListModel) getIntent().getSerializableExtra(EXTRA_OBJC);

        Log.i("postdatax1", post.getPostTitle());
        rel_container = (RelativeLayout) findViewById(R.id.rel_container);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_like_count = (TextView) findViewById(R.id.txt_like_count);
        txt_guest_author = (TextView) findViewById(R.id.txt_guest_author);
        date = (TextView) findViewById(R.id.date);
        img_full = (ImageView) findViewById(R.id.img_full);

        //facebook share
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        ic_fb = (ImageView) findViewById(R.id.ic_fb);
        ic_tweet = (ImageView) findViewById(R.id.ic_tweet);
        like = (ImageView) findViewById(R.id.like);
        web_description = (WebView) findViewById(R.id.web_description);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseLike();
            }
        });
        ic_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(post.getPost_url()))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(linkContent);
                }
            }
        });

        ic_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                        urlEncode(post.getPostTitle()),
                        urlEncode(post.getPost_url()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                        intent.setPackage(info.activityInfo.packageName);
                    }
                }

                startActivity(intent);
            }
        });

        displayApiResult(post);

        netCheck();
        printKEyHash();
    }

    private void printKEyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.mayurit.hakahaki",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void increaseLike() {
        aQuery = new AQuery(this);
        String url = "http://hakahakionline.com/np/news-api/thumblikeadd/";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",post.getID());
        params.put("like",post.getLikeCount());

        aQuery.ajax(url,params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);

              //  Toast.makeText(ActivityPostDetail.this, "response"+object, Toast.LENGTH_SHORT).show();
            }
        });
        int like_count = Integer.parseInt(post.getLikeCount().toString())+1;
        txt_like_count.setText(like_count+"");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == android.R.id.home) {
            onBackPressed();
        } else if (item_id == R.id.action_share) {
            methodShare(ActivityPostDetail.this, post);
        } else if (item_id == R.id.action_browser) {
            directLinkToBrowser(this, post.getPost_url());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_post_details, menu);

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

//        Call<List<NewsListModel>> noticeList = RetrofitAPI.getService().getPostDetail("7300");
        Call<NewsListModel> noticeList = RetrofitAPI.getService().getPostDetail(post.getID());
        noticeList.enqueue(new Callback<NewsListModel>() {
            @Override
            public void onResponse(Call<NewsListModel> call, Response<NewsListModel> response) {

                 post = response.body();
                if (post != null) {
                    displayResult(post);
                    loading(false);
                    swipe_refresh_layout.setRefreshing(false);

                } else {
//                    showNoItemView(true);
                }


            }

            @Override
            public void onFailure(Call<NewsListModel> call, Throwable throwable) {
                Toast.makeText(ActivityPostDetail.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayApiResult(NewsListModel posts) {
        txt_title.setText(posts.getPostTitle());
        txt_date.setText(posts.getPostDate());


        txt_like_count.setText("" + posts.getLikeCount());
        if (posts.getLikeCount() == null) {
            txt_like_count.setText("");
        }
        web_description.loadData(posts.getPostExcerpt(), "text/html; charset=utf-8", "utf-8");
        Glide.with(getApplicationContext()).load(posts.getImageId()).into(img_full);

    }


    private void displayResult(NewsListModel posts) {
        txt_title.setText(posts.getPostTitle());
        txt_date.setText(posts.getPostDate());
        date.setText(posts.getPostDate());
        if(!posts.getGuest_author().equals("")){
            txt_guest_author.setText(posts.getGuest_author());
        }


        txt_like_count.setText("" + posts.getLikeCount());
        if (posts.getLikeCount().equals("null")) {
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
      //  Toast.makeText(activity, "url = "+url, Toast.LENGTH_SHORT).show();
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
        sb.append("Source : " + p.getPost_url() + "");

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, act.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        //sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        act.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }


    public void loading(boolean show) {
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        if (show) {
            spinner.setVisibility(View.VISIBLE);
        } else {
            spinner.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(true);
                fetchData();
            }
        });

    }

    public String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Toast.makeText(ActivityPostDetail.this, "Sorry something went wrong!!!", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }
}
