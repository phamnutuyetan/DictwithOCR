package com.fapp.project.japanesedictionary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fapp.project.japanesedictionary.Database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   Home.DictHomeListener,
                    FavoriteFragment.FavoriteFragmentListener{

    // key for storing a contact's Uri in a Bundle passed to a fragment
    public static final String WORD_URI = "word_uri";
    Home home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        if(savedInstanceState == null)
            DisplayHomepage();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            DisplayHomepage();
        } else if (id == R.id.nav_fav) {
            displayFavorite(R.id.fragmentContainer);

        } else if (id == R.id.nav_meaning) {
            displayReadingRevision(R.id.fragmentContainer);

        } else if (id == R.id.nav_listening) {
            displayListeningRevision(R.id.fragmentContainer);

        } else if (id == R.id.nav_trans) {
            displayTranslate(R.id.fragmentContainer);

        } else if (id == R.id.nav_ocr) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void DisplayHomepage() {

        // add the fragment to the FrameLayout
        home = new Home();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, home);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayWord(Uri contactUri, int viewID) {
        DetailFragment detailFragment = new DetailFragment();

        // specify contact's Uri as an argument to the DetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(WORD_URI, contactUri);
        detailFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
    }

    private void displayFavorite(int viewID){
        FavoriteFragment fragment = new FavoriteFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
    @Override
    public void onWordSelected(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null)
            displayWord(contactUri, R.id.fragmentContainer);
    }

    @Override
    public void onListeningRevision() {
        if(findViewById(R.id.fragmentContainer) != null)
            displayListeningRevision(R.id.fragmentContainer);
    }

    @Override
    public void onReadingRevision() {
        if(findViewById(R.id.fragmentContainer) != null)
            displayReadingRevision(R.id.fragmentContainer);
    }
}
