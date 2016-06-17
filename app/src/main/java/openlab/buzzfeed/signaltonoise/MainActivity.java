package openlab.buzzfeed.signaltonoise;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.util.Locale;

import openlab.buzzfeed.signaltonoise.StnPhoneStateListener;



public class MainActivity extends AppCompatActivity {
    static final int CHECK_TEXT_TO_SPEECH = 2;

    TelephonyManager telephonyManager;
    StnPhoneStateListener psListener;
    TextView signalStrengthView;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Intent checkTextToSpeech = new Intent();
            checkTextToSpeech.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTextToSpeech, CHECK_TEXT_TO_SPEECH);

            signalStrengthView = (TextView)findViewById(R.id.signalStrengthView);

            psListener = new StnPhoneStateListener(signalStrengthView, textToSpeech);
            telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

            signalStrengthView.setText("listening for signal changes...");
        }
        catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_TEXT_TO_SPEECH) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.US);
                            psListener.textToSpeech = textToSpeech;
                        }
                    }
                });

            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
}
