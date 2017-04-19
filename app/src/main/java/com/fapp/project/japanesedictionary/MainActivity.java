package com.fapp.project.japanesedictionary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fapp.project.japanesedictionary.New.FavoriteActivity;
import com.gelitenight.waveview.library.WaveView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
//        NavigationView.OnNavigationItemSelectedListener,
//        Home.DictHomeListener, FavoriteFragment.FavoriteFragmentListener
{
    // key for storing a contact's Uri in a Bundle passed to a fragment
    public static final String WORD_URI = "word_uri";
    private Home home;

    private WaveHelper mWaveHelper;
    private SearchView searchView;
    private LinearLayout btnOCR;
    private LinearLayout btnAudio;
    private LinearLayout btnMultiLineTranslate;
    private LinearLayout btnFavorite;
    private LinearLayout btnInfo;
    private LinearLayout btnOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_2);

        btnOCR = (LinearLayout) findViewById(R.id.btn_ocr);
        btnAudio = (LinearLayout) findViewById(R.id.btn_audio);
        btnMultiLineTranslate = (LinearLayout) findViewById(R.id.btn_multi_line_translate);
        btnFavorite = (LinearLayout) findViewById(R.id.btn_favorite);
        btnOther = (LinearLayout) findViewById(R.id.btn_other);
        btnInfo = (LinearLayout) findViewById(R.id.btn_info);

        initUI();


        btnOCR.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        btnMultiLineTranslate.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnOther.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setItemIconTintList(null);
//
//
//        if(savedInstanceState == null)
            //DisplayHomepage();
    }


    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_ocr:

                break;

            case R.id.btn_audio:
                break;

            case R.id.btn_multi_line_translate:
                break;

            case R.id.btn_favorite:
                displayFavorite(R.id.fragmentContainer);
                break;

            case R.id.btn_other:
                break;

            case R.id.btn_info:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            DisplayHomepage();
//        } else if (id == R.id.nav_fav) {
//            displayFavorite(R.id.fragmentContainer);
//
//        } else if (id == R.id.nav_meaning) {
//            displayReadingRevision(R.id.fragmentContainer);
//
//        } else if (id == R.id.nav_listening) {
//            displayListeningRevision(R.id.fragmentContainer);
//
//        } else if (id == R.id.nav_trans) {
//            displayTranslate(R.id.fragmentContainer);
//
//        } else if (id == R.id.nav_ocr) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//    public void DisplayHomepage() {
//
//        // add the fragment to the FrameLayout
//        home = new Home();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.fragmentContainer, home);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    private void displayWord(Uri contactUri, int viewID) {
//        DetailFragment detailFragment = new DetailFragment();
//
//        // specify contact's Uri as an argument to the DetailFragment
//        Bundle arguments = new Bundle();
//        arguments.putParcelable(WORD_URI, contactUri);
//        detailFragment.setArguments(arguments);
//
//        // use a FragmentTransaction to display the DetailFragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        transaction.replace(viewID, detailFragment);
//        transaction.addToBackStack(null);
//        transaction.commit(); // causes DetailFragment to display
//    }

    private void displayFavorite(int viewID){
//        FavoriteFragment fragment = new FavoriteFragment();
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(viewID, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    private void displayListeningRevision(int viewID){
        ListeningRevisionFragment fragment = new ListeningRevisionFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayReadingRevision(int viewID){
        ReadingRevisionFragment fragment = new ReadingRevisionFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayTranslate(int viewID){
        TranslateFragment fragment = new TranslateFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    @Override
//    public void onWordSelected(Uri contactUri) {
//        if (findViewById(R.id.fragmentContainer) != null)
//            displayWord(contactUri, R.id.fragmentContainer);
//    }
//
//    @Override
//    public void onListeningRevision() {
//        if(findViewById(R.id.fragmentContainer) != null)
//            displayListeningRevision(R.id.fragmentContainer);
//    }
//
//    @Override
//    public void onReadingRevision() {
//        if(findViewById(R.id.fragmentContainer) != null)
//            displayReadingRevision(R.id.fragmentContainer);
//    }


    private void initUI(){
        final WaveView mWaveView = (WaveView) findViewById(R.id.wave_view);
        final LinearLayout lnLogo = (LinearLayout) findViewById(R.id.ln_logo);
        final RelativeLayout rlMainBtns = (RelativeLayout) findViewById(R.id.rl_main_btns);
        searchView = (SearchView) findViewById(R.id.search_view);


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
        }, 2300);

    }
}
