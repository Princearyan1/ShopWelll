package com.princearyan.myutilityapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.BaseAdapter;
import java.util.List;

public class ItemListAdapter extends BaseAdapter {
    private Context context;
    private List<String> itemList;
    private ItemDeleteListener listener;

    public ItemListAdapter(Context context, List<String> itemList, ItemDeleteListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_row, parent, false);
        }

        TextView itemText = convertView.findViewById(R.id.itemText);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        itemText.setText(itemList.get(position));
        deleteButton.setOnClickListener(v -> listener.onItemDeleted(position));

        return convertView;
    }

    public interface ItemDeleteListener {
        void onItemDeleted(int position);
    }
}
