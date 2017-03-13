package com.fapp.project.japanesedictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fapp.project.japanesedictionary.Database.DatabaseDescription;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by User on 24/12/2016.
 */
public class ListeningRevisionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LOADER = 0;

    TextView txtIndexQuestion;
    TextView txtScore;
    ImageView imgSpeaker;
    TextView  txtCorrectAnswer;
    EditText txtWord;
    ImageButton btnNext;

    Handler handler;
    TextToSpeech tts;
    ArrayList<String> listFavorite = new ArrayList<String>();
    Random random;

    int                     index;
    int                     score;
    int                     count;


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        super.onCreateView(inflater, viewGroup, bundle);

        View view = inflater.inflate(R.layout.fragment_listening_revision, viewGroup, false);

        txtIndexQuestion = (TextView)view.findViewById(R.id.txtIndexQuestion);
        txtScore = (TextView)view.findViewById(R.id.txtScore);
        imgSpeaker = (ImageView)view.findViewById(R.id.imgSpeaker);
        txtCorrectAnswer = (TextView)view.findViewById(R.id.txtCorrectAnswer);
        txtWord = (EditText)view.findViewById(R.id.txtWord);
        btnNext = (ImageButton)view.findViewById(R.id.btnNext);

        handler = new Handler();

            tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if (i != TextToSpeech.ERROR)
                        tts.setLanguage(Locale.JAPANESE);
                }
            });

        random = new Random();

        txtWord.clearFocus();
        imgSpeaker.setOnClickListener(imgSpeakerClicked);
        btnNext.setOnClickListener(btnNextClicked);

        return view;
    }

    private View.OnClickListener imgSpeakerClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                if (!tts.isSpeaking()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(
                                listFavorite.get(index),
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                        );
                    }
                }

        }
    };

    private View.OnClickListener btnNextClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(txtWord.getText().toString().length() == 0){
                Toast toast = Toast.makeText(
                        getContext(),
                        getContext().getString(R.string.empty_word),
                        Toast.LENGTH_LONG
                );
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else{
                if(txtWord.getText().toString().equals(listFavorite.get(index))){
                    score++;
                    txtCorrectAnswer.setText(
                            getContext().getString(R.string.correct)
                    );
                }
                else{
                    txtCorrectAnswer.setText(
                            getContext().getString(
                                    R.string.correct_answer,
                                    listFavorite.get(index))
                    );

                }
                txtCorrectAnswer.setVisibility(View.VISIBLE);
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                nextQuestion();
                            }
                        },
                        2000
                );
            }
        }
    };

    private void reset(){
        index = random.nextInt(listFavorite.size());
        score = 0;
        count = 0;

        revise();
    }

    private void nextQuestion(){
        count++;
        index = random.nextInt(listFavorite.size());
        if(count < listFavorite.size())
            revise();
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String s = getString(R.string.revision_result, count,score / (double)count * 100);
            builder.setMessage(s);
            builder.setPositiveButton(R.string.revision_reset, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reset();
                }
            });
            builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            builder.show();
        }
    }

    private void revise(){
        txtIndexQuestion.setText(getString(R.string.index_question, count + 1, listFavorite.size()));
        txtScore.setText(getString(R.string.revision_score, score));
        txtCorrectAnswer.setVisibility(View.INVISIBLE);
        txtWord.setText("");

            if (!tts.isSpeaking())
                tts.speak(listFavorite.get(index), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;

        switch (id){
            case LOADER:
                cursorLoader = new CursorLoader(
                        getActivity(),
                        DatabaseDescription.Dictionary.CONTENT_URI,
                        null,
                        DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
                        new String[]{"1"},
                        null
                );
                break;
            default:
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            int wordIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_WORD);
            listFavorite.clear();
            do{
                listFavorite.add(data.getString(wordIndex));
            }
            while (data.moveToNext());

            reset();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public void onDestroy(){
        if(tts != null)
            tts.shutdown();
        super.onDestroy();
    }
}
