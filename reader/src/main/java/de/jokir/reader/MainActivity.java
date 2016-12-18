package de.jokir.reader;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "HceReader";

    @BindView(R.id.listView) ListView listView;

    private NfcAdapter nfcAdapter;
    private MessageAdapter msgAdapter;
    private IsoDepTranceiver tranceiver;

    private Unbinder unbinder;
    Thread tranceiverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        msgAdapter = new MessageAdapter(getLayoutInflater());
        listView.setAdapter(msgAdapter);

        tranceiver = new IsoDepTranceiver(isoDepListener);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        nfcAdapter.enableReaderMode(this,
                tranceiver,
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        nfcAdapter.disableReaderMode(this);
    }

    IsoDepTranceiver.Listener isoDepListener = new IsoDepTranceiver.Listener() {

        @Override
        public void onConnected() {
            Log.d(TAG, "onConnected");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgAdapter.clear();
                }
            });

            if (tranceiverThread != null) {
                tranceiverThread.interrupt();
            }
            tranceiverThread = new Thread(tranceiver);
            tranceiverThread.start();
        }

        @Override
        public void onReceive(byte[] msg) {
            Log.d(TAG, "onReceive");

            final String message = new String(msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgAdapter.addMessage(message);
                }
            });
        }

        @Override
        public void onError(String err) {
            Log.d(TAG, "onError");

            final String message = (err != null) ? err : "empty error message";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgAdapter.addMessage(message);
                }
            });
        }
    };
}
