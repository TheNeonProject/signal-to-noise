package openlab.buzzfeed.signaltonoise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

/**
 * Created by westleyhennigh on 6/30/16.
 */
public class StnWifiReceiver extends BroadcastReceiver {
    WifiManager wifi;
    TextView wifiStrengthView;
    TextToSpeech textToSpeech;

    StnWifiReceiver(WifiManager manager, TextView view) {
        wifi = manager;
        wifiStrengthView = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiInfo info = wifi.getConnectionInfo();
        if(info != null) {
            int strength = info.getRssi();

            wifiStrengthView.setText("Wifi Strength: " + Integer.toString(strength));

            if (textToSpeech != null) {
                String speech = Integer.toString(strength);
                if (strength < 0) {
                    speech = "negative" + speech;
                }

                speech = "Wifi " + speech;

                textToSpeech.speak(speech, TextToSpeech.QUEUE_ADD, null);
            }
        }

    }
}
