package com.deep.jscandictionary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.deep.jscandictionary.Helper.WaveHelper;
import com.deep.jscandictionary.image2text.R;
import com.gelitenight.waveview.library.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String WORD_URI = "word_uri";

    @BindView(R.id.wave_view)
    WaveView mWaveView;
    @BindView(R.id.ln_logo)
    LinearLayout lnLogo;
    @BindView(R.id.rl_main_btns)
    RelativeLayout rlMainBtns;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.btn_ocr)
    LinearLayout btnOCR;
    @BindView(R.id.btn_audio)
    LinearLayout btnAudio;
    @BindView(R.id.btn_multi_line_translate)
    LinearLayout btnMultiLineTranslate;
    @BindView(R.id.btn_favorite)
    LinearLayout btnFavorite;
    @BindView(R.id.btn_info)
    LinearLayout btnInfo;
    @BindView(R.id.btn_other)
    LinearLayout btnOther;

    private WaveHelper mWaveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initWaveUI();

        btnOCR.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        btnMultiLineTranslate.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnOther.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_ocr:
                intent = new Intent(this, MainOCRActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_audio:
                break;

            case R.id.btn_multi_line_translate:
                intent = new Intent(this, MultiTranslateActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_favorite:
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_other:
                break;

            case R.id.btn_info:
                break;
        }
    }

    private void initWaveUI(){
        mWaveHelper = new WaveHelper(mWaveView);
        mWaveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveView.setWaveColor(
                getResources().getColor(R.color.wave_behide_color),
                getResources().getColor(R.color.colorPrimary));
        mWaveView.setAlpha(0.9f);
        mWaveHelper.start();


        final Animation lnLogoFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        lnLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                lnLogo.setVisibility(View.VISIBLE);
                lnLogo.startAnimation(lnLogoFadeIn);
            }
        }, 1800);
        rlMainBtns.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMainBtns.setVisibility(View.VISIBLE);
                rlMainBtns.startAnimation(animFadeIn);
                searchView.setVisibility(View.VISIBLE);
                searchView.startAnimation(animFadeIn);
            }
        }, 2100);

    }
}