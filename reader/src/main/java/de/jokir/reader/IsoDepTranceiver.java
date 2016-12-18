package de.jokir.reader;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;

/**
 * Created by johnny on 18.12.2016.
 */

public class IsoDepTranceiver implements NfcAdapter.ReaderCallback, Runnable {

    private static final byte[] CLA_INS_P1_P2 = ByteUtils.hexStringToByteArray("00A40400");
    public static final byte[] ANDROID_AID = ByteUtils.hexStringToByteArray("F0010203040506");

    IsoDep isoDep;
    private Listener listener;

    public IsoDepTranceiver(Listener listener) {
        this.listener = listener;
    }


    @Override
    public void run() {
        Log.d(MainActivity.TAG, "start tranceive");

        int messageCounter = 0;
        try {
            isoDep.connect();

           Log.d(MainActivity.TAG, (new String(createSelectAidApdu(ANDROID_AID))));
            isoDep.transceive(createSelectAidApdu(ANDROID_AID));
            byte[] response;
            while (isoDep.isConnected() && !Thread.interrupted()) {
                String message = "Message from android reader " + messageCounter++;
                response = isoDep.transceive(message.getBytes());
                listener.onReceive(response);
            }
            isoDep.close();
        } catch (IOException ex) {
            ex.printStackTrace();

            listener.onError(ex.getMessage());
        }
    }

    private byte[] createSelectAidApdu(byte[] aid) {
        byte[] result = new byte[6 + aid.length];

        System.arraycopy(CLA_INS_P1_P2, 0, result, 0, CLA_INS_P1_P2.length);
        result[4] = (byte) aid.length;
        System.arraycopy(aid, 0, result, 5, aid.length);
        result[result.length - 1] = 0;

        return result;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.d(MainActivity.TAG, "onTagDiscovered");

        this.isoDep = IsoDep.get(tag);
        listener.onConnected();
    }

    /**
     * Listener interface
     */
    public interface Listener {
        void onConnected();

        void onReceive(byte[] msg);

        void onError(String err);
    }
}
