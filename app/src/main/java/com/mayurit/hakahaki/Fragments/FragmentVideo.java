package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mayurit.hakahaki.ActivityPostDetail;
import com.mayurit.hakahaki.ActivityPostTypeDetail;
import com.mayurit.hakahaki.ActivityPostTypeList;
import com.mayurit.hakahaki.Adapters.CategoryNewsListAdapter;
import com.mayurit.hakahaki.Helpers.Constant;
import com.mayurit.hakahaki.Helpers.RecyclerItemClickListener;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.MainActivity;
import com.mayurit.hakahaki.Model.NewsListModel;
import com.mayurit.hakahaki.NEEFEJDetail;
import com.mayurit.hakahaki.ProjectDetail;
import com.mayurit.hakahaki.R;
import com.mayurit.hakahaki.VideoDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentVideo extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    Context context;

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    View view;
    LinearLayout lnr_pani,lnr_ankhijhyal,lnr_naya_pusta,lnr_documentary;

    public FragmentVideo() {
        // Required empty public constructor
    }

    public static FragmentVideo newInstance() {
        FragmentVideo fragment = new FragmentVideo();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        context = getActivity();

        lnr_pani = view.findViewById(R.id.lnr_pani);
        lnr_ankhijhyal = view.findViewById(R.id.lnr_ankhijhyal);
        lnr_naya_pusta = view.findViewById(R.id.lnr_naya_pusta);
        lnr_documentary = view.findViewById(R.id.lnr_documentary);

        lnr_pani.setOnClickListener(this);
        lnr_ankhijhyal.setOnClickListener(this);
        lnr_naya_pusta.setOnClickListener(this);
        lnr_documentary.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        toolbarTitle = (String) getResources().getText(R.string.video);
        getActivity().setTitle(toolbarTitle);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.lnr_pani:
                intent = new Intent(context, VideoDetail.class);
                intent.putExtra(EXTRA_OBJC, 50);
                startActivity(intent);
                break;
            case R.id.lnr_ankhijhyal:
                intent = new Intent(context, VideoDetail.class);
                intent.putExtra(EXTRA_OBJC, 49);
                startActivity(intent);
                break;
            case R.id.lnr_naya_pusta:
                intent = new Intent(context, VideoDetail.class);
                intent.putExtra(EXTRA_OBJC, 51);
                startActivity(intent);
                break;
            case R.id.lnr_documentary:
                intent = new Intent(context, VideoDetail.class);
                intent.putExtra(EXTRA_OBJC, 52);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
