package com.princearyan.myutilityapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class CalculatorActivity extends AppCompatActivity {
    private TextView display;
    private String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        display = findViewById(R.id.calcScreen);
    }

    public void btnClick(View view) {
        Button button = (Button) view;
        input += button.getText().toString();
        display.setText(input);
    }

    public void calculate(View view) {
        try {
            double result = evaluateExpression(input);
            display.setText(String.valueOf(result));
            input = String.valueOf(result);
        } catch (Exception e) {
            display.setText("Error");
            input = "";
        }
    }

    public void clear(View view) {
        input = "";
        display.setText("");
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        char[] tokens = expression.toCharArray();

        for (int i = 0; i < tokens.length; i++) {
            char c = tokens[i];

            if (Character.isDigit(c)) {
                StringBuilder numBuffer = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    numBuffer.append(tokens[i++]);
                }
                i--;
                numbers.push(Double.parseDouble(numBuffer.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        return (op2 == '*' || op2 == '/') && (op1 == '+' || op1 == '-');
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b == 0 ? 0 : a / b;
            default: return 0;
        }
    }
}
