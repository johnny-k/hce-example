package de.jokir.hceexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_HELLO = "de.jokir.hceexample.ACTION.HELLO";
    public static final String ACTION_MESSAGE = "de.jokir.hceexample.ACTION.MESSAGE";
    public static final String ACTION_ERROR = "de.jokir.hceexample.ACTION.ERROR";
    public static final String EXTRA_MESSAGE = "de.jokir.hceexample.MESSAGE";

    @BindView(R.id.listView)
    ListView listView;

    private MessageAdapter msgAdapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        msgAdapter = new MessageAdapter(getLayoutInflater());
        listView.setAdapter(msgAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_HELLO);
        filter.addAction(ACTION_MESSAGE);
        filter.addAction(ACTION_ERROR);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            switch (intent.getAction()) {
                case ACTION_HELLO:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msgAdapter.clear();
                        }
                    });
                    break;

                case ACTION_MESSAGE: // fall through
                case ACTION_ERROR:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msgAdapter.addMessage(intent.getStringExtra(EXTRA_MESSAGE));
                        }
                    });
                    break;
            }
        }
    };
}
