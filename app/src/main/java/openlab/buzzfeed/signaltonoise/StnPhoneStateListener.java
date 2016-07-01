package openlab.buzzfeed.signaltonoise;

import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;
import android.widget.TextView;

import java.util.function.IntConsumer;

/**
 * Created by westleyhennigh on 6/16/16.
 */
public class StnPhoneStateListener extends PhoneStateListener {
    int strength;
    TextView signalStrengthView;
    TextToSpeech textToSpeech;

    StnPhoneStateListener(TextView view, TextToSpeech tts) {
        signalStrengthView = view;
        textToSpeech = tts;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        strength = signalStrength.getGsmSignalStrength();
        strength = (2 * strength) - 113; // -> dBm

        signalStrengthView.setText("Cell Signal Strength: " + Integer.toString(strength));
        if (textToSpeech != null) {
            String speech = Integer.toString(strength);
            if (strength < 0) {
                speech = "negative" + speech;
            }

            textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}
