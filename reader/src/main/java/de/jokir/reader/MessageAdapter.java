package de.jokir.reader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 18.12.2016.
 */

public class MessageAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> list = new ArrayList<>(100);
    int counter = 0;

    public MessageAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void addMessage(String msg) {
        list.add(++counter + ": " + msg);
        notifyDataSetChanged();
    }

    public void clear() {
        counter = 0;
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText((CharSequence) getItem(pos));
        return view;
    }
}
