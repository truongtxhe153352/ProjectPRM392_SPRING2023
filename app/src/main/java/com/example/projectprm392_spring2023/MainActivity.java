package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button btnEx;
    private EditText edtData;
    private ImageView imageView;

    private EditText editContent;
    private Button btnSend;

    public static final String CONTENT = "CONTENT";


    public MainActivity() {
    }

    private void bindingView() {
        btnEx = findViewById(R.id.GetData);
        edtData = findViewById(R.id.edtData);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        btnEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to call another activity
                Intent i = new Intent();
                i.setClass(MainActivity.this, PromptActivity.class);
                String s = edtData.getText().toString();//send to activity 2
                i.putExtra("data", s);
//                i.putExtra("image", R.drawable.test_img);
                startActivity(i);
//                startActivityForResult(i, 21);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 21:
//                if (resultCode == 8) {
//                    String result = data.getStringExtra("result");
//                    edtData.setText(result);
//                }
//        }
//    }





//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        editContent = (EditText) findViewById(R.id.edt_content);
//        btnSend = (Button) findViewById(R.id.btn_sendData);
//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = editContent.getText().toString();
//                byExtras(content);
//            }
//        });
//    }
//
//    public void byExtras(String content) {
//        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//        intent.putExtra(CONTENT, content);
//        startActivity(intent);
//    }
}
