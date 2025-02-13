package com.princearyan.myutilityapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.BaseAdapter;
import com.princearyan.myutilityapp.R;
import com.princearyan.myutilityapp.models.ShoppingItem;
import java.util.ArrayList;

public class ShoppingListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ShoppingItem> shoppingList;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingItem> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_list_row, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.itemName);
        CheckBox itemCheck = convertView.findViewById(R.id.itemCheck);
        Button deleteItemButton = convertView.findViewById(R.id.deleteItemButton);

        ShoppingItem item = shoppingList.get(position);
        itemName.setText(item.getName());
        itemCheck.setChecked(item.isChecked());

        itemCheck.setOnCheckedChangeListener((buttonView, isChecked) -> item.setChecked(isChecked));

        deleteItemButton.setOnClickListener(v -> {
            shoppingList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
