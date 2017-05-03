package com.deep.jscandictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deep.jscandictionary.image2text.R;
import com.deep.jscandictionary.database.DatabaseDescription;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 24/12/2016.
 */
public class ReadingRevisionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER = 0;

    TextView txtIndexQuestion;
    TextView            txtScore;
    TextView            txtEnglish;
    TextView            txtVietnamese;
    TextView            txtCorrectAnswer;
    EditText txtWord;
    ImageButton btnNext;

    Handler handler;
    Random random;

    ArrayList<String> listWord = new ArrayList<String>();
    ArrayList<String>   listEnglish = new ArrayList<String>();
    ArrayList<String>   listVietnamese = new ArrayList<String>();

    int                 index;
    int                 score;
    int                 count;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursor = null;
        switch (id) {
            case LOADER:
                cursor = new CursorLoader(
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
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            int wordIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_WORD);
            int englishIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_ENGLISH);
            int vietnameseIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_VIETNAMEE);

            do{
                listWord.add(data.getString(wordIndex));
                listEnglish.add(data.getString(englishIndex));
                listVietnamese.add(data.getString(vietnameseIndex));
            }
            while(data.moveToNext());

            reset();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        super.onCreateView(inflater, viewGroup, bundle);

        View view = inflater.inflate(R.layout.fragment_reading_revision, viewGroup, false);

        txtIndexQuestion = (TextView)view.findViewById(R.id.txtIndexQuestion);
        txtScore = (TextView)view.findViewById(R.id.txtScore);
        txtEnglish = (TextView)view.findViewById(R.id.txtEnglish);
        txtVietnamese = (TextView)view.findViewById(R.id.txtVietnamese);
        txtCorrectAnswer = (TextView)view.findViewById(R.id.txtCorrectAnswer);
        txtWord = (EditText)view.findViewById(R.id.txtWord);
        btnNext = (ImageButton)view.findViewById(R.id.btnNext);

        handler = new Handler();
        random = new Random();

        txtWord.clearFocus();
        btnNext.setOnClickListener(btnNextClicked);

        return view;
    }

    private void reset(){
        index = random.nextInt(listWord.size());
        score = 0;
        count = 0;

        revise();
    }

    private void revise(){
        txtIndexQuestion.setText(getString(R.string.index_question, count + 1, listWord.size()));
        txtScore.setText(getString(R.string.revision_score, score));
        txtCorrectAnswer.setVisibility(View.INVISIBLE);

        txtWord.setText("");
        txtEnglish.setText(listEnglish.get(index));
        txtVietnamese.setText(listVietnamese.get(index));
    }

    private void nextQuestion(){
        count++;
        index = random.nextInt(listWord.size());
        if(count < listWord.size())
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
                if(txtWord.getText().toString().equals(listWord.get(index))){
                    score++;
                    txtCorrectAnswer.setText(
                            getContext().getString(R.string.correct)
                    );
                }
                else{
                    txtCorrectAnswer.setText(
                            getContext().getString(
                                    R.string.correct_answer,
                                    listWord.get(index))
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
}
