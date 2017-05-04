package com.deep.jscandictionary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deep.jscandictionary.image2text.BuildConfig;
import com.deep.jscandictionary.image2text.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuong Nguyen Lan on 05/04/2017.
 */

public class InfomationActivity extends FragmentActivity implements View.OnClickListener{
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ln_update)
    LinearLayout btnUpdate;
    @BindView(R.id.ln_rating)
    LinearLayout btnRating;
    @BindView(R.id.ln_fanpage)
    LinearLayout btnFanpage;

    private CustomActionBarFragment actionBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_infomation);
        ButterKnife.bind(this);

        tvVersion.setText(BuildConfig.VERSION_CODE + " - " + BuildConfig.VERSION_NAME);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.SIMPLE_TITLE_TYPE, "InformationActivity");
        btnUpdate.setOnClickListener(this);
        btnRating.setOnClickListener(this);
        btnFanpage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        Intent intent;


        switch (view.getId()){
            case R.id.ln_update:

            case R.id.ln_rating:
                Uri appUri = Uri.parse(getString(R.string.pre_market_uri) + getPackageName()); //old package name is "com.tuyetan.ocr.image2text"
                intent = new Intent(Intent.ACTION_VIEW, appUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                        | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                        | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.pre_google_play_uri) + getPackageName())));
                }
                break;

            case R.id.ln_fanpage:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.fanpage_url)));
                startActivity(intent);
                break;
        }
    }
}