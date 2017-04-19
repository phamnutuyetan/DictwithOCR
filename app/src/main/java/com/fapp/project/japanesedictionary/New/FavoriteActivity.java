package com.fapp.project.japanesedictionary.New;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fapp.project.japanesedictionary.Database.DatabaseDescription;
import com.fapp.project.japanesedictionary.DictionaryAdapter;
import com.fapp.project.japanesedictionary.FavoriteFragment;
import com.fapp.project.japanesedictionary.ItemDivider;
import com.fapp.project.japanesedictionary.R;

import static java.security.AccessController.getContext;

/**
 * Created by Phuong Nguyen Lan on 04/18/2017.
 */

public class FavoriteActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{
    public static final int LOADER = 0;

    FavoriteFragment.FavoriteFragmentListener listener;
    DictionaryAdapter adapter;
    TextView numOfWordTextView;
    RecyclerView favoriteRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_favorite);

        CustomActionBarFragment actionBarFragment = new CustomActionBarFragment(CustomActionBarFragment.TITLE_TYPE);
        getFragmentManager().beginTransaction().add(R.id.fr_action_bar, actionBarFragment).commit();



        numOfWordTextView = (TextView)findViewById(R.id.numOfWordTextView);
        favoriteRecyclerView = (RecyclerView)findViewById(R.id.favoriteRecyclerView);

        adapter = new DictionaryAdapter(new DictionaryAdapter.DictClickListener() {
            @Override
            public void onClick(Uri contactUri) {
                listener.onWordSelected(contactUri);
            }
        });

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecyclerView.setHasFixedSize(true);
        favoriteRecyclerView.addItemDecoration(new ItemDivider(this));
        favoriteRecyclerView.setAdapter(adapter);

    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceSate)
//    {
//        super.(savedInstanceSate);
//        getLoaderManager().initLoader(LOADER, null, this);
//    }
//
//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//        listener = (FavoriteFragment.FavoriteFragmentListener)context;
//    }
//
//    @Override
//    public void onDetach(){
//        super.onDetach();
//        listener = null;
//    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

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
        numOfWordTextView.setText(
                this.getString(R.string.num_of_words, data.getCount())
        );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_reading_revision:
                listener.onReadingRevision();
                return true;
            case R.id.menu_listening_revision:
                listener.onListeningRevision();
                return true;
            case R.id.menu_delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(this.getString(R.string.confirm_delete_all));
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, "0");
                        getContentResolver().update(
                                DatabaseDescription.Dictionary.CONTENT_URI,
                                values,
                                DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
                                new String[]{"1"}
                        );
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();

                return true;
            default:
                return false;
        }
    }

}
