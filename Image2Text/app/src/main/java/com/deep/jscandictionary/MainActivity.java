package com.deep.jscandictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.deep.jscandictionary.Helper.WaveHelper;
import com.deep.jscandictionary.Quiz.QuizActivity;
import com.deep.jscandictionary.image2text.R;
import com.gelitenight.waveview.library.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String WORD_URI = "word_uri";
    public static final String WORD_QUERY = "word_query";


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
    @BindView(R.id.btn_quiz)
    LinearLayout btnQuiz;
    @BindView(R.id.btn_multi_line_translate)
    LinearLayout btnMultiLineTranslate;
    @BindView(R.id.btn_favorite)
    LinearLayout btnFavorite;
    @BindView(R.id.btn_info)
    LinearLayout btnInfo;
    @BindView(R.id.btn_setting)
    LinearLayout btnSetting;

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
        btnQuiz.setOnClickListener(this);
        btnMultiLineTranslate.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        searchView.setOnQueryTextListener(searchViewOnQueryTextListener);
    }

    private SearchView.OnQueryTextListener searchViewOnQueryTextListener
            = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (!query.trim().isEmpty()){
                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra(WORD_QUERY, query);
                //searchView.setQuery("", true);
                startActivity(intent);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_ocr:
                intent = new Intent(this, OCRActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_quiz:
                intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_multi_line_translate:
                intent = new Intent(this, MultiTranslateActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_favorite:
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_setting:
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