package com.fapp.project.japanesedictionary.New;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.fapp.project.japanesedictionary.Home;
import com.fapp.project.japanesedictionary.R;

/**
 * Created by Phuong Nguyen Lan on 04/19/2017.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInsanceState){
        super.onCreate(savedInsanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);

        Home fragment = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }
}
