package com.example.opilane.workwithspeech;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageButton mic;
    TextView resultText;
    TextToSpeech toSpeech;
    int result;
    EditText speakerText;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mic = (ImageButton)findViewById(R.id.micButton);
        resultText = (TextView)findViewById(R.id.resultText);
        speakerText = (EditText)findViewById(R.id.speakerText);

/*Android allows you to convert your text into voice. Not only you can convert it,
but it also allows you to speak text in variety of different languages.
Android provides TextToSpeech class for this purpose. To use this class, you
need to instantiate an object of this class and also specify the initListener. In this listener, you have to specify the properties for TextToSpeech object, such
as its language, pitch etc. Once you have set the language, you can call speak method of the class to
speak the text. */

        toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if (status==TextToSpeech.SUCCESS){
                    result = toSpeech.setLanguage(Locale.getDefault());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void micClick(View view) { promptSpeechInput();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); /* ACTION_RECOGNIZE_SPEECH - Starts an activity that will prompt the user for speech and send it through a speech recognizer. The results will be returned via activity results (in onActivityResult(int, int, Intent),*/
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); /*EXTRA_LANGUAGE_MODEL - Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH.
        LANGUAGE_MODEL_FREE_FORM - Use a language model based on free-form speech recognition. This is a value to use for EXTRA_LANGUAGE_MODEL.*/
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");/* EXTRA_PROMPT - Optional text prompt to show to the user when asking them to speak.*/
        try {
            startActivityForResult(intent, 100);
        }
        catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Sorry your device doesn't support speech language!", Toast.LENGTH_LONG).show();
        }
    }
    public void onActivityResult (int request_code, int result_code, Intent intent){
        super.onActivityResult(request_code, result_code, intent);
        switch (request_code){
            case 100: if(result_code == RESULT_OK && intent != null)
            {
                ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultText.setText(result.get(0));
            }
                break;
        }
    }

    public void TTS(View view) {
        switch (view.getId())
        {
            case R.id.Speakbtn:
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your device!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    text = speakerText.getText().toString();
                    toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null); /*Queue mode where all entries in the playback queue (media to be played and text to be synthesized) are dropped and replaced by the new entry.*/
                }
                break;
            case R.id.Stopbtn:
                if (toSpeech != null)
                {
                    toSpeech.stop(); /*Lõpetab tegevuse*/
                }
                break;
        }
    }

    @Override
    protected void onDestroy() { /*onDestroy() is a method called by the framework when your activity is closing down. It is called to allow your activity to do any shut-down operations it may wish to do.*/
        super.onDestroy();
        if (toSpeech != null)
        {
            toSpeech.stop(); /*Lõpetab tegevuse*/
            toSpeech.shutdown(); /*Releases the resources used by the TextToSpeech engine.
            It is good practice for instance to call this method in the onDestroy() method of an Activity so the TextToSpeech engine can be cleanly stopped.*/
        }
    }
    //Kui vajutad nupule clear kustutab ära kasutaja sisestatud teksti
    public void Clear(View view) {
        speakerText.setText("");    /* SpeakerText nimeline muutuja, mis on tüübilt EditText -> kui vaatad activity_main.xml faili siis sealt on selle id-ks @+id/speakerText.
        setText määrab ära uueks tekstiks mis sinna kirjutatakse programmi poolt "" .*/
    }
}

