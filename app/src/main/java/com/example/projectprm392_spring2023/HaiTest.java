package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class HaiTest extends AppCompatActivity {
    private Button btnSave;
    private EditText edtData2;
    private ImageView imageView;
    public static int return_fromActivity1 = 1000;

    private void bindingView() {
        btnSave = findViewById(R.id.btnSave);
        edtData2 = findViewById(R.id.edtData2);
        imageView = findViewById(R.id.imageView2);
    }

    private void bindingAction() {
        btnSave.setOnClickListener(this::onBtnSaveClick);
    }

    private void onBtnSaveClick(View view) {
        String result = edtData2.getText().toString();
        Intent i = new Intent();
        i.putExtra("result", result);
        setResult(8, i);
        finish();
    }

    private void receivingIntent() {
        Intent i = getIntent();
        if (i != null) {
            String data = i.getStringExtra("data");
            edtData2.setText(data);
            int inputManage=i.getIntExtra("image",1);
            imageView.setImageResource(inputManage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_api);
        bindingView();
        bindingAction();
        receivingIntent();
    }
}