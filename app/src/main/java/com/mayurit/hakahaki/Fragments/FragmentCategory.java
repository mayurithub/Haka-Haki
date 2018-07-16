package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mayurit.hakahaki.Adapters.CategoryAdapter;
import com.mayurit.hakahaki.CategoryDetail;
import com.mayurit.hakahaki.Helpers.Constant;
import com.mayurit.hakahaki.Helpers.Helper;
import com.mayurit.hakahaki.Helpers.RecyclerItemClickListener;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.MainActivity;
import com.mayurit.hakahaki.Model.CategoryModel;
import com.mayurit.hakahaki.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCategory extends Fragment {
    private static final String ARG_PARAM1 = "toolbar_title";

    ArrayList<CategoryModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    CategoryAdapter mAdapter;

    private SwipeRefreshLayout swipe_refresh;
    private String toolbarTitle;
    Context context;
    View view;

    public FragmentCategory() {
        // Required empty public constructor
    }

    public static FragmentCategory newInstance() {
        FragmentCategory fragment = new FragmentCategory();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(ARG_PARAM1);
            getActivity().setTitle(toolbarTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_activity_category_list, container, false);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_category);
        RecyclerWithListner();
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                mAdapter.notifyDataSetChanged();
                requestAction();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_refresh) {
            ((MainActivity) getActivity()).changeFragment(FragmentCategory.newInstance());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void RecyclerWithListner() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CategoryAdapter(list);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        CategoryModel singleItem = list.get(position);
                        Intent intent = new Intent(context, CategoryDetail.class);
                        intent.putExtra("category_id",singleItem.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(final View view, final int position) {

                    }
                })
        );

        requestAction();

    }

    public void fetchData(){
        Log.i("fragmentCategory","fetchdata 1");
        Call<List<CategoryModel>> noticeList = RetrofitAPI.getService().getCategoryList();
        noticeList.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {

                swipeProgress(false);
                List<CategoryModel> resp = response.body();
                if (resp != null) {
                    displayApiResult(response.body());
                    Log.i("checkx","ayo");
                   /* list.addAll(response.body());
                    mAdapter.notifyDataSetChanged();*/
                } else {
                    showNoItemView(true);
                }

            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable throwable) {
                if (!call.isCanceled()) onFailRequest();
            }
        });

    }

    private void displayApiResult(final List<CategoryModel> categories) {
        mAdapter.setListData(categories);
        list.addAll(categories);
        for(CategoryModel data: list){
            Log.i("checkx","data="+data.getName());
        }
        mAdapter.notifyDataSetChanged();
        swipeProgress(false);
        if (categories.size() == 0) {
            showNoItemView(true);
        }
    }

    @Override
    public void onResume() {
        toolbarTitle = (String) getResources().getText(R.string.category);
        getActivity().setTitle(toolbarTitle);
        super.onResume();
    }

    private void onFailRequest() {
        swipeProgress(false);
        if (Helper.isNetworkConnect(getActivity())) {
            showFailedView(true, getString(R.string.no_category));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void showFailedView(boolean flag, String message) {
        View lyt_failed = (View) view.findViewById(R.id.lyt_failed_category);
        ((TextView) view.findViewById(R.id.failed_message)).setText(message);
        if (flag) {
            recyclerView.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) view.findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction();
            }
        });

    }

    private void requestAction() {
        showFailedView(false, "");
        swipeProgress(true);
        showNoItemView(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("fragmentCategory","fetchdata s");
                fetchData();
            }
        }, Constant.DELAY_TIME);
    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) view.findViewById(R.id.lyt_no_item_category);
        ((TextView) view.findViewById(R.id.no_item_message)).setText(R.string.no_category);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }


}
