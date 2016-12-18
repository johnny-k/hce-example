package de.jokir.hceexample;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by johnny on 17.12.2016.
 */

public class MyHostApduService extends HostApduService {

    private static final String TAG = "HceExample";

    int counter = 0;

    private static final byte[] SELECT_APDU = ByteUtils.hexStringToByteArray("00A40400F0010203040506");


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");
    }

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "processCommandApdu: " + new String(apdu));

        Intent intent;

        if (apdu.length >= 2 && apdu[0] == 0x00 && apdu[1] == 0xA4) {
            Log.d(TAG, "Application selected");

            intent = new Intent(MainActivity.ACTION_HELLO);
            sendBroadcast(intent);

            return "hello reader".getBytes();
        } else {
            Log.d(TAG, "Received: " + new String(apdu));

            intent = new Intent(MainActivity.ACTION_MESSAGE);
            intent.putExtra(MainActivity.EXTRA_MESSAGE, new String(apdu));
            sendBroadcast(intent);

            return ("message from hce: " + counter++).getBytes();
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Intent intent = new Intent(MainActivity.ACTION_ERROR);
        switch (reason) {
            case DEACTIVATION_LINK_LOSS:
                Log.d(TAG, "onDeactivated: DEACTIVATION_LINK_LOSS");
                intent.putExtra(MainActivity.EXTRA_MESSAGE, "DEACTIVATION_LINK_LOSS");
                break;

            case DEACTIVATION_DESELECTED:
                Log.d(TAG, "onDeactivated: DEACTIVATION_DESELECTED");
                intent.putExtra(MainActivity.EXTRA_MESSAGE, "DEACTIVATION_DESELECTED");
                break;

            default:
                Log.d(TAG, "onDeactivated: unknown reason 0x" + Integer.toHexString(reason));
                intent.putExtra(MainActivity.EXTRA_MESSAGE, "onDeactivated: unknown reason 0x" + Integer.toHexString(reason));
        }

        sendBroadcast(intent);
    }


}
