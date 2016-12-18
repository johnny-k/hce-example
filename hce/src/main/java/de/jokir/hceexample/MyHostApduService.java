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

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     * If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has explicitly selected your
     * service, either as a default or just for the next tap.
     * This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the {@link
     * #sendResponseApdu(byte[])} method later.
     *
     * @param apdu   The APDU that received from the remote device
     * @param extras A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response APDU can be sent
     * at this point.
     */
    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "processCommandApdu: " + new String(apdu));
        System.out.println("process command: " + new String(apdu));

        if (Arrays.equals(apdu, SELECT_APDU)) {
            Log.d(TAG, "Application selected");
            return ByteUtils.stringToAsciiByteArray("Hello Desktop!");
        } else {
            Log.d(TAG, "Received: " + new String(apdu));
            return ByteUtils.stringToAsciiByteArray("Message from android: " + counter++);
        }
    }

    /**
     * Called if the connection to the NFC card is lost, in order to let the application know the
     * cause for the disconnection (either a lost link, or another AID being selected by the
     * reader).
     *
     * @param reason Either DEACTIVATION_LINK_LOSS or DEACTIVATION_DESELECTED
     */
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
