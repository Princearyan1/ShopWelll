package com.princearyan.myutilityapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class
ItemCalculatorActivity extends AppCompatActivity {
    EditText itemName, itemPrice, itemQuantity;
    TextView totalPrice;
    ListView historyList;
    List<String> itemList = new ArrayList<>();
    SharedPreferences prefs;
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_calculator);

        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        itemQuantity = findViewById(R.id.itemQuantity);
        totalPrice = findViewById(R.id.totalPrice);
        historyList = findViewById(R.id.historyList);
        prefs = getSharedPreferences("shopping_data", MODE_PRIVATE);

        loadHistory();

        adapter = new ItemListAdapter(this, itemList, position -> {
            itemList.remove(position);
            saveHistory();
            updateTotalPrice();
            adapter.notifyDataSetChanged();
        });

        historyList.setAdapter(adapter);

        findViewById(R.id.addItem).setOnClickListener(view -> {
            String name = itemName.getText().toString().trim();
            String priceText = itemPrice.getText().toString().trim();
            String quantityText = itemQuantity.getText().toString().trim();

            if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                itemName.setError("Required");
                itemPrice.setError("Required");
                itemQuantity.setError("Required");
                return;
            }

            try {
                float price = Float.parseFloat(priceText);
                float quantity = Float.parseFloat(quantityText);
                float cost = price * quantity;

                itemList.add(name + " - ₹" + cost);
                saveHistory();
                updateTotalPrice();
                adapter.notifyDataSetChanged();

                // ✅ Clear input fields after adding an item
                itemName.setText("");
                itemPrice.setText("");
                itemQuantity.setText("");

                // ✅ Reset focus to itemName for a new entry
                itemName.requestFocus();
            } catch (NumberFormatException e) {
                itemPrice.setError("Invalid number");
                itemQuantity.setError("Invalid number");
            }
        });

        findViewById(R.id.clearHistory).setOnClickListener(view -> {
            itemList.clear();
            saveHistory();
            updateTotalPrice();
            adapter.notifyDataSetChanged();
        });
    }

    private void updateTotalPrice() {
        float sum = 0;
        for (String item : itemList) {
            try {
                sum += Float.parseFloat(item.split("₹")[1]);
            } catch (Exception ignored) {}
        }
        totalPrice.setText("Total: ₹" + sum);
    }

    private void saveHistory() {
        prefs.edit().putStringSet("history", new HashSet<>(itemList)).apply();
    }

    private void loadHistory() {
        Set<String> savedItems = prefs.getStringSet("history", new HashSet<>());
        if (savedItems != null) {
            itemList.clear();
            itemList.addAll(savedItems);
            updateTotalPrice();
        }
    }
}
