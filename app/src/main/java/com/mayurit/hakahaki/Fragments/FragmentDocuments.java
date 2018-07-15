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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentDocuments extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    Context context;

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    View view;
    LinearLayout lnr_rules_regulations,lnr_watershed_health_report,lnr_research_report;

    public FragmentDocuments() {
        // Required empty public constructor
    }

    public static FragmentDocuments newInstance() {
        FragmentDocuments fragment = new FragmentDocuments();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_documents, container, false);
        context = getActivity();

        lnr_rules_regulations = view.findViewById(R.id.lnr_rules_regulations);
        lnr_watershed_health_report = view.findViewById(R.id.lnr_watershed_health_report);
        lnr_research_report = view.findViewById(R.id.lnr_research_report);

        lnr_rules_regulations.setOnClickListener(this);
        lnr_watershed_health_report.setOnClickListener(this);
        lnr_research_report.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        toolbarTitle = (String) getResources().getText(R.string.document);
        getActivity().setTitle(toolbarTitle);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.lnr_rules_regulations:
                intent = new Intent(context, ActivityPostTypeList.class);
                intent.putExtra(EXTRA_OBJC, "document");
                startActivity(intent);
                break;
            case R.id.lnr_watershed_health_report:
                intent = new Intent(context, ActivityPostTypeList.class);
                intent.putExtra(EXTRA_OBJC, "health-report");
                startActivity(intent);
                break;
            case R.id.lnr_research_report:
                intent = new Intent(context, ActivityPostTypeList.class);
                intent.putExtra(EXTRA_OBJC, "research-report");
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
