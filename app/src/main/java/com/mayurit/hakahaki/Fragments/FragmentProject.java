package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mayurit.hakahaki.MainActivity;
import com.mayurit.hakahaki.R;
import com.mayurit.hakahaki.VideoDetail;


public class FragmentProject extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    Context context;

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    View view;
    LinearLayout lnr_pani,lnr_sp_project,lnr_crtn_project;

    public FragmentProject() {
        // Required empty public constructor
    }

    public static FragmentProject newInstance() {
        FragmentProject fragment = new FragmentProject();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project, container, false);
        context = getActivity();

        lnr_pani = view.findViewById(R.id.lnr_pani);
        lnr_sp_project = view.findViewById(R.id.lnr_sp_project);
        lnr_crtn_project = view.findViewById(R.id.lnr_crtn_project);

        lnr_pani.setOnClickListener(this);
        lnr_sp_project.setOnClickListener(this);
        lnr_crtn_project.setOnClickListener(this);

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
                ((MainActivity) getActivity()).changeFragment(FragmentAudioVideolist.newInstance(),getString(R.string.pani));
                break;
            case R.id.lnr_sp_project:
                ((MainActivity) getActivity()).changeFragment(FragmentAudioVideolist.newInstance(),getString(R.string.sp_project));
                break;
            case R.id.lnr_crtn_project:
                ((MainActivity) getActivity()).changeFragment(FragmentAudioVideolist.newInstance(),getString(R.string.crtn_project));
                break;
            default:
                break;
        }

    }
}
