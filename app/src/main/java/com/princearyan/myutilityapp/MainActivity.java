package com.princearyan.myutilityapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openItemCalculator(View view) {
        startActivity(new Intent(this, ItemCalculatorActivity.class));
    }

    public void openNotebook(View view) {
        startActivity(new Intent(this, NotebookActivity.class));
    }

    public void openCalculator(View view) {
        startActivity(new Intent(this, CalculatorActivity.class));
    }

    public void openFutureFeature(View view) {
        startActivity(new Intent(this, FutureFeatureActivity.class));
    }
    public void openShoppingList(View view) {
        startActivity(new Intent(this, ShoppingListActivity.class));
    }
    //openUpcomingFeatures
    public void openUpcomingFeatures(View view) {
        startActivity(new Intent(this, UpcomingFeaturesActivity.class));
    }



}
