package de.jokir.hceexample;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by johnny on 17.12.2016.
 */

public class MyHostApduService extends HostApduService {

    private static final String TAG = "HceExample";

    int counter = 0;

    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    /**
     * OSP-DEP command header to select AID.
     * [Class | Instruction | Parameter1 | Parameter2]
     * [ / | SELECT | select by name | / ]
     */
    private static final byte[] SELECT_APDU = ByteUtils.hexStringToByteArray("00A40400F0010203040506");


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");
    }

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "processCommandApdu: " + new String(apdu));
        System.out.println("process command: " + new String(apdu));

        if (Arrays.equals(apdu, SELECT_APDU)) {
            Log.d(TAG, "Application selected");
            return "hello reader".getBytes();
        } else {
            Log.d(TAG, "Received: " + new String(apdu));
            return ("message from hce: " + counter++).getBytes();
        }
    }

    @Override
    public void onDeactivated(int reason) {

        switch (reason) {
            case DEACTIVATION_LINK_LOSS:
                Log.d(TAG, "onDeactivated: DEACTIVATION_LINK_LOSS");
                break;

            case DEACTIVATION_DESELECTED:
                Log.d(TAG, "onDeactivated: DEACTIVATION_DESELECTED");
                break;

            default:
                Log.d(TAG, "onDeactivated: unknown reason 0x" + Integer.toHexString(reason));
        }
    }
}
