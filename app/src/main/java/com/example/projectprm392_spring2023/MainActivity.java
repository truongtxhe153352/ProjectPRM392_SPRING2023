package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editContent;
    private Button btnSend;

    public static final String CONTENT = "CONTENT";


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editContent = (EditText) findViewById(R.id.edt_content);
        btnSend = (Button) findViewById(R.id.btn_sendData);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editContent.getText().toString();
                byExtras(content);
            }
        });
    }

    public void byExtras(String content) {
        Intent intent = new Intent(MainActivity.this, ShowAfterScan.class);
        intent.putExtra(CONTENT, content);
        startActivity(intent);
    }
}