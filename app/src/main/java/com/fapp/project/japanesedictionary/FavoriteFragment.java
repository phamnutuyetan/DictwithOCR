package com.fapp.project.japanesedictionary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.fapp.project.japanesedictionary.Database.DatabaseDescription;
import com.fapp.project.japanesedictionary.Database.DictContentProvider;

/**
 * Created by User on 24/12/2016.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER = 0;

    public interface FavoriteFragmentListener{
        void onWordSelected(Uri contactUri);
        void onListeningRevision();
        void onReadingRevision();
    }

    FavoriteFragmentListener listener;
    DictionaryAdapter adapter;

    TextView numOfWordTextView;
    RecyclerView favoriteRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_favorite_result, container, false);
        setHasOptionsMenu(true);

        numOfWordTextView = (TextView)view.findViewById(R.id.numOfWordTextView);
        favoriteRecyclerView = (RecyclerView)view.findViewById(R.id.favoriteRecyclerView);

        adapter = new DictionaryAdapter(new DictionaryAdapter.DictClickListener() {
            @Override
            public void onClick(Uri contactUri) {
                listener.onWordSelected(contactUri);
            }
        });

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteRecyclerView.setHasFixedSize(true);
        favoriteRecyclerView.addItemDecoration(new ItemDivider(getContext()));
        favoriteRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceSate)
    {
        super.onActivityCreated(savedInstanceSate);
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (FavoriteFragmentListener)context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case LOADER:
                return new CursorLoader(
                        getContext(),
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
                getContext().getString(R.string.num_of_words, data.getCount())
        );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorite, menu);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getContext().getString(R.string.confirm_delete_all));
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, "0");
                            getContext().getContentResolver().update(
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
