package com.deep.jscandictionary;

/**
 * Created by An on 12/02/2016.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deep.jscandictionary.image2text.R;
import com.deep.jscandictionary.database.DatabaseDescription.Dictionary;
import com.deep.jscandictionary.database.DictContentProvider;

import java.util.Locale;

public class DetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, TextToSpeech.OnInitListener {

    private static final int WORD_LOADER = 0; // identifies the Loader

    private Uri     wordUri; // Uri of selected contact
    TextToSpeech    textToSpeech;
    boolean         isFavorite;

    private TextView wordTextView;
    private TextView synonymousTextView;
    private TextView englishTextView;
    private TextView vietnameseTextView;
    private ImageButton speakerImageButton;
    private ImageButton favoriteImageButtoon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // inflate DetailFragment's layout
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        wordTextView = (TextView) view.findViewById(R.id.labelWord);
        synonymousTextView = (TextView) view.findViewById(R.id.textSynonymous);
        englishTextView = (TextView) view.findViewById(R.id.textEnglish);
        vietnameseTextView = (TextView) view.findViewById(R.id.textVietnamese);
        speakerImageButton = (ImageButton) view.findViewById(R.id.buttonSpeaker);
        favoriteImageButtoon = (ImageButton)view.findViewById(R.id.buttonFavorite);

        speakerImageButton.setOnClickListener(buttonSpeakerClicked);
        favoriteImageButtoon.setOnClickListener(buttonFavoriteClicked);

        // get Bundle of arguments then extract the contact's Uri
        Bundle arguments = getArguments();
        if (arguments != null)
            wordUri = arguments.getParcelable(MainActivity.WORD_URI);

        // load the contact
        getLoaderManager().initLoader(WORD_LOADER, null, this);

        textToSpeech = new TextToSpeech(getContext(), this);

        return view;
    }

    private View.OnClickListener buttonSpeakerClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                if (!textToSpeech.isSpeaking() && wordTextView.getText() != null) {
                        textToSpeech.speak(
                                wordTextView.getText().toString(),
                                TextToSpeech.QUEUE_FLUSH,
                                null
                        );
                }
        }
    };

    private View.OnClickListener buttonFavoriteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ContentValues values = new ContentValues();
            values.put(Dictionary.COLUMN_WORD, wordTextView.getText().toString());
            values.put(Dictionary.COLUMN_SYNONYMOUS, synonymousTextView.getText().toString());
            values.put(Dictionary.COLUMN_ENGLISH, englishTextView.getText().toString());
            values.put(Dictionary.COLUMN_VIETNAMEE, vietnameseTextView.getText().toString());
            if(isFavorite)
                values.put(Dictionary.COLUMN_ISFAVORITE, 0);
            else
                values.put(Dictionary.COLUMN_ISFAVORITE, 1);

            // update to database
            int numRowUpdated = getActivity().getContentResolver().update(
                    wordUri,
                    values,
                    null,
                    null
            );

            if(numRowUpdated > 0){
                String message = null;
                if(isFavorite) {
                    message = getContext().getString(R.string.remove_favorite);
                    favoriteImageButtoon.setImageResource(R.drawable.blue_star);
                }
                else {
                    message = getContext().getString(R.string.save_successfully);
                    favoriteImageButtoon.setImageResource(R.drawable.red_star);
                }

                Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                // change favorite
                isFavorite = !isFavorite;
            }
            else{
                Toast toast = Toast.makeText(
                        getContext(),
                        getContext().getString(R.string.save_unsuccessfully),
                        Toast.LENGTH_LONG
                );
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
    };

    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;
        DictContentProvider.isQueryingExactly = true;
        switch (id) {
            case WORD_LOADER:
                cursorLoader = new CursorLoader(
                        getActivity(),
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
        // if the contact exists in the database, display its data
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int wordIndex = data.getColumnIndex(Dictionary.COLUMN_WORD);
            int synonymousIndex = data.getColumnIndex(Dictionary.COLUMN_SYNONYMOUS);
            int englishIndex = data.getColumnIndex(Dictionary.COLUMN_ENGLISH);
            int vietnameseIndex = data.getColumnIndex(Dictionary.COLUMN_VIETNAMEE);
            int isFavoriteIndex = data.getColumnIndex(Dictionary.COLUMN_ISFAVORITE);


            // fill data
            wordTextView.setText(data.getString(wordIndex));
            synonymousTextView.setText(data.getString(synonymousIndex));
            englishTextView.setText(data.getString(englishIndex));
            vietnameseTextView.setText(data.getString(vietnameseIndex));
            if (data.getInt(isFavoriteIndex) == 1) {
                isFavorite = true;
                favoriteImageButtoon.setImageResource(R.drawable.red_star);
            } else {
                isFavorite = false;
                favoriteImageButtoon.setImageResource(R.drawable.blue_star);
            }
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    @Override
    public void onDestroy(){
        if(textToSpeech != null)
            textToSpeech.shutdown();
        super.onDestroy();
    }

    @Override
    public void onInit(int i) {
        if(i != TextToSpeech.ERROR){
            textToSpeech.setLanguage(Locale.JAPAN);
        }
    }
}
