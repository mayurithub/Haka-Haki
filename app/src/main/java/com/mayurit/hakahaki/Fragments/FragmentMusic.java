package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.mayurit.hakahaki.R;

import at.blogc.android.views.ExpandableTextView;


public class FragmentMusic extends Fragment {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    private String mParam2;
    Context context;
    private ExpandableTextView expandableTextView;
    private ImageButton buttonToggle;
    public FragmentMusic() {
        // Required empty public constructor
    }

    public static FragmentMusic newInstance(String title) {
        FragmentMusic fragment = new FragmentMusic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_music,null);
        expandableTextView = view.findViewById(R.id.expandableTextView);
        buttonToggle = view.findViewById(R.id.button_toggle);
        expandText("देशको पूर्वी भूभाग भएर बहने नदीहरूमा बाढी आउन सक्ने संभावना बढेको छ । जल तथा मौसम विज्ञान विभागको जलविज्ञान महाशाखा तथा बाढी पूर्वानुमान शाखाले जारी गरेको बाढी बुलेटिन ");


        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle(toolbarTitle);
        super.onResume();
    }
    //getView().findViewById(R.id.expandableTextView)
    private void expandText(String information) {
        expandableTextView.setText(information);
        expandableTextView.setAnimationDuration(750L);
        expandableTextView.setInterpolator(new OvershootInterpolator());
        expandableTextView.setExpandInterpolator(new OvershootInterpolator());
        expandableTextView.setCollapseInterpolator(new OvershootInterpolator());

        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandableTextView.toggle();
                float deg = buttonToggle.getRotation() + 180F;
                buttonToggle.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

            }
        });
    }
}
