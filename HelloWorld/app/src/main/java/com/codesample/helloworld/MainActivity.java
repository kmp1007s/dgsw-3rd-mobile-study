package com.codesample.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textView;
    private EditText editTextNum1, editTextNum2;
    private Button buttonAdd, buttonSub, buttonMul, buttonDiv;

    // Interface type의 객체 만들어 사용하기 -> Mul
    private View.OnClickListener listener = (v) -> {
        float num1 = Float.parseFloat(editTextNum1.getText().toString());
        float num2 = Float.parseFloat(editTextNum2.getText().toString());
        textView.setText(String.valueOf(num1 * num2));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("Simple Calculator!!");

        editTextNum1 = findViewById(R.id.editTextNum1);
        editTextNum2 = findViewById(R.id.editTextNum2);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSub = findViewById(R.id.buttonSub);
        buttonMul = findViewById(R.id.buttonMul);
        buttonDiv = findViewById(R.id.buttonDiv);

        buttonAdd.setOnClickListener(this);
        buttonSub.setOnClickListener(this);
        buttonMul.setOnClickListener(this);
        buttonDiv.setOnClickListener(this);

//        // 익명 함수를 이용하는 방식 -> Sub
//        buttonSub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                float num1 = Float.parseFloat(editTextNum1.getText().toString());
//                float num2 = Float.parseFloat(editTextNum2.getText().toString());
//                textView.setText(String.valueOf(num1 - num2));
//            }
//        });
//
//        buttonMul.setOnClickListener(listener);
}

    // 공동 리스너를 이용하는 방식 (v.getId()를 통해 식별 가능)
    @Override
    public void onClick(View v) {
        float num1=0, num2=0;
        int id = v.getId();

        try {
            num1 = Float.parseFloat(editTextNum1.getText().toString());
            num2 = Float.parseFloat(editTextNum2.getText().toString());
        }catch(NumberFormatException e) {
            textView.setText("Invalid Number");
            return;
        }
        if(id == R.id.buttonAdd) { num1 += num2; }
        else if(id == R.id.buttonSub) { num1 -= num2; }
        else if(id == R.id.buttonMul) { num1 *= num2; }
        else if(id == R.id.buttonDiv) {
            if (num2 == 0) {textView.setText("Divided by zero"); return; }
            else num1 /= num2;
        }
        textView.setText(String.valueOf(num1));
    }

//    public void onDivision(View v) {
//        float num1 = Float.parseFloat(editTextNum1.getText().toString());
//        float num2 = Float.parseFloat(editTextNum2.getText().toString());
//        if(num2 == 0) textView.setText("Divided by zero");
//        else textView.setText(String.valueOf(num1/num2));
//    }
}
