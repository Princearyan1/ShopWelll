package com.princearyan.myutilityapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.princearyan.myutilityapp.models.ShoppingItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.princearyan.myutilityapp.adapters.ShoppingListAdapter;

public class ShoppingListActivity extends AppCompatActivity {
    private ArrayList<ShoppingItem> shoppingList = new ArrayList<>();
    private ShoppingListAdapter adapter;
    private ListView listView;
    private EditText itemInput;
    private Button addItemButton, shoppingDoneButton, clearHistoryButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        listView = findViewById(R.id.shoppingListView);
        itemInput = findViewById(R.id.itemInput);
        addItemButton = findViewById(R.id.addItemButton);
        shoppingDoneButton = findViewById(R.id.shoppingDoneButton);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);

        prefs = getSharedPreferences("ShoppingHistory", MODE_PRIVATE);
        adapter = new ShoppingListAdapter(this, shoppingList);
        listView.setAdapter(adapter);

        loadHistory();

        addItemButton.setOnClickListener(v -> addItem());
        shoppingDoneButton.setOnClickListener(v -> completeShopping());
        clearHistoryButton.setOnClickListener(v -> clearHistory());
    }

    private void addItem() {
        String itemName = itemInput.getText().toString().trim();
        if (!itemName.isEmpty()) {
            shoppingList.add(new ShoppingItem(itemName, false)); // New item is not checked
            adapter.notifyDataSetChanged();
            itemInput.setText("");
            saveToHistory();  // Save immediately
        } else {
            Toast.makeText(this, "Enter an item name", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeShopping() {
        StringBuilder pendingItems = new StringBuilder();
        for (ShoppingItem item : shoppingList) {
            if (!item.isChecked()) {
                pendingItems.append(item.getName()).append("\n");
            }
        }

        if (pendingItems.length() > 0) {
            Toast.makeText(this, "Unpurchased items:\n" + pendingItems.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Shopping Completed!", Toast.LENGTH_SHORT).show();
        }

        saveToHistory(); // Save the updated list
    }

    private void saveToHistory() {
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> historySet = new HashSet<>();

        for (ShoppingItem item : shoppingList) {
            historySet.add(item.getName() + "," + item.isChecked()); // Save item with status
        }

        editor.putStringSet("history", historySet);
        editor.apply();
    }

    private void loadHistory() {
        Set<String> historySet = prefs.getStringSet("history", new HashSet<>());

        shoppingList.clear();
        for (String entry : historySet) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                String name = parts[0];
                boolean checked = Boolean.parseBoolean(parts[1]);
                shoppingList.add(new ShoppingItem(name, checked));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void clearHistory() {
        prefs.edit().remove("history").apply();
        shoppingList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show();
    }
}
