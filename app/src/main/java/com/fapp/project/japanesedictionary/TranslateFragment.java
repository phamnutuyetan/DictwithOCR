package com.fapp.project.japanesedictionary;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TranslateFragment extends Fragment {

    EditText textToTrans;
    EditText textResult;
    Button   buttonTrans;

    String text;
    public TranslateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_translate, container, false);

        textToTrans=(EditText) view.findViewById(R.id.textToTrans);
        textResult=(EditText)view.findViewById(R.id.textResult);
        buttonTrans=(Button)view.findViewById(R.id.buttonTrans);

        buttonTrans.setOnClickListener(onButtonTransClick);
        //text=textToTrans.getText().toString();
        return view;
    }

    private View.OnClickListener onButtonTransClick =new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            text=textToTrans.getText().toString();
            class bgStuff extends AsyncTask<Void, Void, Void> {

                String translatedText = "";
                @Override
                protected Void doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    try {

                        translatedText = translate(text);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        translatedText = e.toString();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    // TODO Auto-generated method stub
                    textResult.setText(translatedText);
                    super.onPostExecute(result);
                }


            }

            textResult.setText("");
            new bgStuff().execute();

        }
    };


    public String translate(String text) throws Exception{


        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("FAPP_AN");
        Translate.setClientSecret("Oi5YL6qmXAVVxMvJt/rWqlYdTjKW71LDFz5bMFlC370=");

        String translatedText = "";

        translatedText = Translate.execute(text,Language.JAPANESE);

        return translatedText;
    }
}
