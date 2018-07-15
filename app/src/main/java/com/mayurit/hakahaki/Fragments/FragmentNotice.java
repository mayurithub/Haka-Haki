package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mayurit.hakahaki.R;


public class FragmentNotice extends Fragment {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    private String mParam2;
    Context context;

    public FragmentNotice() {
        // Required empty public constructor
    }

    public static FragmentNotice newInstance() {
        FragmentNotice fragment = new FragmentNotice();

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
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onResume() {
        toolbarTitle = (String) getResources().getText(R.string.notice);
        getActivity().setTitle(toolbarTitle);
        super.onResume();
    }
}
