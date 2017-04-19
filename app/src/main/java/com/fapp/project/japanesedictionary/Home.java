package com.fapp.project.japanesedictionary;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fapp.project.japanesedictionary.Database.DatabaseDescription;
import com.fapp.project.japanesedictionary.Database.DictContentProvider;
import com.gelitenight.waveview.library.WaveView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment
                implements  LoaderManager.LoaderCallbacks<Cursor>{

    EditText            textLookUpWord;
    RecyclerView        recyclerView;

    Uri                 uriSearch;

    public Home() {
        // Required empty public constructor
    }
    public interface DictHomeListener {
        // called when contact selected
        void onWordSelected(Uri contactUri);
    }

    private static final int DICT_LOADER_A = 0; // identifies Loader
    private static final int DICT_LOADER_ALL = 1;

    // used to inform the MainActivity when a contact is selected
    private DictHomeListener listener;

    private DictionaryAdapter dictAdapter; // adapter for recyclerView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(false); // fragment has menu items to display

        // inflate GUI and get reference to the RecyclerView
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textLookUpWord = (EditText)view.findViewById(R.id.textLookUpWord);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // text watcher
        textLookUpWord.addTextChangedListener(textLookUpWordChangedListener);

        // recyclerView should display items in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        // create recyclerView's adapter and item click listener
        dictAdapter = new DictionaryAdapter(
                    new DictionaryAdapter.DictClickListener() {
                        @Override
                        public void onClick(Uri contactUri) {
                            listener.onWordSelected(contactUri);
                        }
                    }
            );
        recyclerView.setAdapter(dictAdapter); // set the adapter

        // attach a custom ItemDecorator to draw dividers between list items
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        // improves performance if RecyclerView's layout size never changes
        recyclerView.setHasFixedSize(true);

        return view;
    }

    private TextWatcher textLookUpWordChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length() == 0)
                getLoaderManager().initLoader(DICT_LOADER_ALL, null, Home.this);
            else{
                uriSearch = DatabaseDescription.Dictionary.buildContactUri(charSequence.toString());
                getLoaderManager().restartLoader(DICT_LOADER_A, null, Home.this);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DictHomeListener) context;
    }

    // remove ContactsFragmentListener when Fragment detached
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        // set query
        DictContentProvider.isQueryingExactly = false;
        // load all words in the first time created
        if(savedInstanceState == null)
            getLoaderManager().initLoader(DICT_LOADER_ALL, null, Home.this);
        else{
            // load after the cases: rotate screen, etc
            String uri = savedInstanceState.getString("URI");
            if(uri == null)
                getLoaderManager().initLoader(DICT_LOADER_ALL, null, Home.this);
            else {
                uriSearch = DatabaseDescription.Dictionary.buildContactUri(uri);
                getLoaderManager().initLoader(DICT_LOADER_A, null, Home.this);
            }
        }
    }

    // save current state to reuse when activity created
    @Override
    public void onSaveInstanceState(Bundle outState){
        if(uriSearch == null)
            outState.putString("URI", null);
        else
            outState.putString("URI", uriSearch.getLastPathSegment());
    }

    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case DICT_LOADER_A:
                return new CursorLoader(
                        getActivity(),
                        uriSearch, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        DatabaseDescription.Dictionary.COLUMN_WORD +
                                " COLLATE NOCASE ASC"); // sort order
            case DICT_LOADER_ALL:
                return new CursorLoader(
                        getActivity(),
                        DatabaseDescription.Dictionary.CONTENT_URI, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        DatabaseDescription.Dictionary.COLUMN_WORD +
                                " COLLATE NOCASE ASC");
            default:
                return null;
        }
    }

    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dictAdapter.swapCursor(data);
        if(data.getCount() == 0){
            Toast toast = Toast.makeText(
                    getContext(),
                    getString(R.string.not_found, textLookUpWord.getText().toString()),
                    Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dictAdapter.swapCursor(null);
    }


}
