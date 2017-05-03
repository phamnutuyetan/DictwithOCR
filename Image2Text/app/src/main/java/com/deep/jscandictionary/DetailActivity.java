package com.deep.jscandictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deep.jscandictionary.database.DatabaseDescription;
import com.deep.jscandictionary.database.DictContentProvider;
import com.deep.jscandictionary.image2text.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

/**
 * Created by Phuong Nguyen Lan on 04/28/2017.
 */

public class DetailActivity extends FragmentActivity implements View.OnClickListener ,
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final int WORD_LOADER = 0;

    @BindView(R.id.tv_synonymous)
    TextView tvSynonymous;
    @BindView(R.id.tv_english_meaning)
    TextView tvEnglishMeaning;
    @BindView(R.id.tv_vietnamese_meaning)
    TextView tvVietnameseMeaning;
    @BindView(R.id.tv_example)
    TextView tvExample;
    @BindView(R.id.ibtn_speaker)
    ImageButton btnSpeaker;
    @BindView(R.id.cb_favorite)
    CheckBox cbFavorite;

    private CustomActionBarFragment actionBarFragment;
    private Uri wordUri;    // Uri of selected contact


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.TITLE_TYPE, "DetailActivity");

        //get Uri from Main Activity
        wordUri = getIntent().getParcelableExtra(MainActivity.WORD_URI);
        getSupportLoaderManager().initLoader(WORD_LOADER, null, this);

        btnSpeaker.setOnClickListener(this);
    }

    private CompoundButton.OnCheckedChangeListener cbFavoriteCheckedChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ContentValues values = new ContentValues();
            values.put(DatabaseDescription.Dictionary.COLUMN_WORD, actionBarFragment.tvTitle.getText().toString());
            values.put(DatabaseDescription.Dictionary.COLUMN_SYNONYMOUS, tvSynonymous.getText().toString());
            values.put(DatabaseDescription.Dictionary.COLUMN_ENGLISH, tvEnglishMeaning.getText().toString());
            values.put(DatabaseDescription.Dictionary.COLUMN_VIETNAMEE, tvVietnameseMeaning.getText().toString());

            if (!isChecked)
                values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, 0);
            else
                values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, 1);

            // update to database
            int numRowUpdated = getApplicationContext().getContentResolver().update(
                    wordUri,
                    values,
                    null,
                    null
            );

            if(numRowUpdated > 0){
                String message = null;
                if(!isChecked)
                    message = getString(R.string.remove_favorite);
                else
                    message = getString(R.string.save_successfully);

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.save_unsuccessfully),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ibtn_speaker:
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;
        DictContentProvider.isQueryingExactly = true;
        switch (id) {
            case WORD_LOADER:
                cursorLoader = new CursorLoader(
                        this,
                        wordUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }


    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int wordIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_WORD);
            int synonymousIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_SYNONYMOUS);
            int englishIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_ENGLISH);
            int vietnameseIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_VIETNAMEE);
            int isFavoriteIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE);


            // fill data
            actionBarFragment.setTitle(data.getString(wordIndex));
            tvSynonymous.setText(data.getString(synonymousIndex));
            tvEnglishMeaning.setText(data.getString(englishIndex));
            tvVietnameseMeaning.setText(data.getString(vietnameseIndex));
            if (data.getInt(isFavoriteIndex) == 1) {
                cbFavorite.setChecked(true);
            } else {
                cbFavorite.setChecked(false);
            }
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {    }
}
