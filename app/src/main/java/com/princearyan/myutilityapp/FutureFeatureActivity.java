package com.princearyan.myutilityapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FutureFeatureActivity extends AppCompatActivity {
    private EditText actualPriceInput, discountInput;
    private Button calculateDiscount;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_feature);

        actualPriceInput = findViewById(R.id.actualPriceInput);
        discountInput = findViewById(R.id.discountInput);
        calculateDiscount = findViewById(R.id.calculateDiscount);
        resultText = findViewById(R.id.resultText);

        calculateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDiscountAmount();
            }
        });
    }

    private void calculateDiscountAmount() {
        String priceText = actualPriceInput.getText().toString().trim();
        String discountText = discountInput.getText().toString().trim();

        if (priceText.isEmpty() || discountText.isEmpty()) {
            resultText.setText("Please enter valid values.");
            return;
        }

        double actualPrice = Double.parseDouble(priceText);
        double discountPercent = Double.parseDouble(discountText);

        if (discountPercent < 0 || discountPercent > 100) {
            resultText.setText("Invalid discount percentage!");
            return;
        }

        double discountAmount = (actualPrice * discountPercent) / 100;
        double finalPrice = actualPrice - discountAmount;

        resultText.setText("Discount: ₹" + discountAmount + "\nFinal Price: ₹" + finalPrice);
    }
}
