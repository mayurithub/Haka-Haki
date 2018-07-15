package com.mayurit.hakahaki.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.mayurit.hakahaki.ActivityPostDetail;
import com.mayurit.hakahaki.Helpers.RetrofitAPI;
import com.mayurit.hakahaki.MainActivity;
import com.mayurit.hakahaki.Model.NewsListModel;
import com.mayurit.hakahaki.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFeedback extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "toolbar_title";

    private String toolbarTitle;
    Context context;

    AQuery aQuery;

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    View view;
    EditText edt_name,edt_email,edt_feedback;
    Button submit,cancel;

    public FragmentFeedback() {
    }

    public static FragmentFeedback newInstance() {
        FragmentFeedback fragment = new FragmentFeedback();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        context = getActivity();

        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_feedback = view.findViewById(R.id.edt_feedback);
        submit = view.findViewById(R.id.btnSubmit);
        cancel = view.findViewById(R.id.btnCancel);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.btnSubmit:
                submitFeedback();
                break;
            case R.id.btnCancel:
                ((MainActivity) getActivity()).changeFragment(FragmentHome.newInstance());
                break;
            default:
                break;
        }

    }

    private void submitFeedback() {
        String name,email,message;
        name = edt_name.getText().toString();
        email = edt_email.getText().toString();
        message = edt_feedback.getText().toString();

        aQuery = new AQuery(getContext());
        String url = "http://hakahakionline.com/np/news-api/feedback/";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name",name);
        params.put("email",email);
        params.put("message",message);

        aQuery.ajax(url,params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                edt_name.setText("");
                edt_email.setText("");
                edt_feedback.setText("");
                Toast.makeText(getContext(), "Message has been sent", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
