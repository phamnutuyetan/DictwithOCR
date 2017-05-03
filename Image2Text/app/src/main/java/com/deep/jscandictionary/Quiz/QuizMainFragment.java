package com.deep.jscandictionary.Quiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.deep.jscandictionary.image2text.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuong Nguyen Lan on 05/04/2017.
 */

public class QuizMainFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.ln_definition_quiz)
    LinearLayout btnDefinitionQuiz;
    @BindView(R.id.ln_synonymous_quiz)
    LinearLayout btnSynonymousQuiz;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_quiz_main, parent, false);
        ButterKnife.bind(this, view);

        btnDefinitionQuiz.setOnClickListener(this);
        btnSynonymousQuiz.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ln_definition_quiz:
                ((QuizActivity)getActivity()).replaceFragment(new DefinitionQuizFragment());
                break;

            case R.id.ln_synonymous_quiz:
                ((QuizActivity)getActivity()).replaceFragment(new SynonymousQuizFragment());
                break;
        }
    }
}