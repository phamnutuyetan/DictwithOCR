package com.deep.jscandictionary;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deep.jscandictionary.image2text.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuong Nguyen Lan on 04/19/2017.
 */

public class CustomActionBarFragment extends Fragment implements View.OnClickListener{
    public static final int SEARCH_TITLE_TYPE = 0;          // for: DetailActivity
    public static final int TITLE_TYPE = 1;                //  for: OCRActivity, MultiTranslateActivity,...
    public static final int TITLE_WITH_OPTIONS_TYPE = 2;  //   for: FavoriteActivity,...

    @BindView(R.id.ibtn_back)
    ImageButton btnBack;
    @BindView(R.id.ibtn_bin)
    ImageButton btnBin;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_favorite)
    CheckBox cbFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_custom_action_bar, container, false);
        ButterKnife.bind(this, view);

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

    private void setInvisibleAll(){
        btnBin.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.INVISIBLE);
        cbFavorite.setVisibility(View.INVISIBLE);
    }

    public void setActionBarType(int type, String title){
        setInvisibleAll();
        switch (type){
            case SEARCH_TITLE_TYPE:
                searchView.setVisibility(View.VISIBLE);
                cbFavorite.setVisibility(View.VISIBLE);
                break;

            case TITLE_WITH_OPTIONS_TYPE:
                btnBin.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                break;

            case TITLE_TYPE:
            default:
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
                break;
        }
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }
}
