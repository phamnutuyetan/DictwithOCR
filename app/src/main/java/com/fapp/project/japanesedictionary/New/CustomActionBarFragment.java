package com.fapp.project.japanesedictionary.New;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fapp.project.japanesedictionary.R;

/**
 * Created by Phuong Nguyen Lan on 04/19/2017.
 */

public class CustomActionBarFragment extends Fragment implements View.OnClickListener{
    public static final int SEARCH_TYPE = 0;
    public static final int TITLE_TYPE = 1;
    int type;


    ImageButton btnBack;
    ImageButton btnBin;
    SearchView searchView;
    TextView tvTitle;

    public CustomActionBarFragment() { }

    public CustomActionBarFragment(int type){

        this.type = SEARCH_TYPE;
        if (type == TITLE_TYPE)
            this.type = TITLE_TYPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_custom_action_bar, container, false);

        btnBack = (ImageButton) view.findViewById(R.id.ibtn_back);
        btnBin = (ImageButton) view.findViewById(R.id.ibtn_bin);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        //checking UI
        if (type == SEARCH_TYPE){
            btnBin.setVisibility(View.INVISIBLE);
            tvTitle.setVisibility(View.INVISIBLE);

            searchView.setVisibility(View.VISIBLE);
        }
        else{
            btnBin.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);

            searchView.setVisibility(View.INVISIBLE);
        }

        btnBack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ibtn_back:
                getActivity().onBackPressed();
                break;
        }
    }


}
