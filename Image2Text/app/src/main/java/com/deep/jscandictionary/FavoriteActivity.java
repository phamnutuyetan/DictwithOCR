package com.deep.jscandictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.deep.jscandictionary.database.DatabaseDescription;
import com.deep.jscandictionary.image2text.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuong Nguyen Lan on 04/18/2017.
 */

public class FavoriteActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{
    public interface FavoriteActivityListener{
        void onWordSelected(Uri contactUri);
        void onListeningRevision();
        void onReadingRevision();
    }

    public static final int LOADER = 0;

    @BindView(R.id.tv_num_of_word)
    TextView tvNumOfWord;
    @BindView(R.id.rcv_favorite_list)
    RecyclerView rcvFavorite;

    private FavoriteActivityListener listener;
    private DictionaryAdapter adapter;

    CustomActionBarFragment actionBarFragment;





    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.TITLE_WITH_OPTIONS_TYPE, "FavoriteActivity");

        adapter = new DictionaryAdapter(new DictionaryAdapter.DictClickListener() {
            @Override
            public void onClick(Uri contactUri) {
                listener.onWordSelected(contactUri);
            }
        });

        rcvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rcvFavorite.setHasFixedSize(true);
        rcvFavorite.addItemDecoration(new ItemDivider(this));
        rcvFavorite.setAdapter(adapter);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ibtn_bin:

                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case LOADER:
                return new CursorLoader(
                        this,
                        DatabaseDescription.Dictionary.CONTENT_URI,
                        null,
                        DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
                        new String[]{"1"},
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        tvNumOfWord.setText(
                this.getString(R.string.num_of_words, data.getCount())
        );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.menu_reading_revision:
//                listener.onReadingRevision();
//                return true;
//            case R.id.menu_listening_revision:
//                listener.onListeningRevision();
//                return true;
//            case R.id.menu_delete_all:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage(this.getString(R.string.confirm_delete_all));
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ContentValues values = new ContentValues();
//                        values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, "0");
//                        getContentResolver().update(
//                                DatabaseDescription.Dictionary.CONTENT_URI,
//                                values,
//                                DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
//                                new String[]{"1"}
//                        );
//                    }
//                });
//                builder.setNegativeButton(android.R.string.cancel, null);
//                builder.show();
//
//                return true;
//            default:
//                return false;
//        }
//    }



}
