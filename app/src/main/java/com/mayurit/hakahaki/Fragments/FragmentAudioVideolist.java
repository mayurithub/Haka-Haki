package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mayurit.hakahaki.MainActivity;
import com.mayurit.hakahaki.R;
import com.mayurit.hakahaki.VideoDetail;


public class FragmentAudioVideolist extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    Context context;

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    View view;
    LinearLayout lnr_audio,lnr_video;

    public FragmentAudioVideolist() {
        // Required empty public constructor
    }

    public static FragmentAudioVideolist newInstance() {
        FragmentAudioVideolist fragment = new FragmentAudioVideolist();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_audio_video_list, container, false);
        context = getActivity();
        toolbarTitle = getArguments().getString("mTitle");
        getActivity().setTitle(toolbarTitle);


        lnr_audio = view.findViewById(R.id.lnr_audio);
        lnr_video = view.findViewById(R.id.lnr_video);

        lnr_audio.setOnClickListener(this);
        lnr_video.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.lnr_audio:
                intent = new Intent(context, VideoDetail.class);
                intent.putExtra(EXTRA_OBJC, 50);
                startActivity(intent);
                break;
            case R.id.lnr_video:
                intent = new Intent(context, VideoDetail.class);
                intent.putExtra(EXTRA_OBJC, 49);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
