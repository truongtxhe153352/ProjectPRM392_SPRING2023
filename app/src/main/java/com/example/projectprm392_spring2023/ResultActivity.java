package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    String[] items = {"Summary", "Give the answer", "Rewrite the passage", "Custom"};

    AutoCompleteTextView autoCompleteTxt;

    ArrayAdapter<String> arrayAdapterItems;

    private TextView tvContent;

    private EditText edtCustom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_api);

//        tvContent = (TextView) findViewById(R.id.tv_content);
//
//        // Get data form textView to editText
//        edtCustom = (EditText)findViewById(R.id.edt_customContent);
//
//        setDataByExtra();
//
//        // show select item
//        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
//
//        arrayAdapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
//
//        autoCompleteTxt.setAdapter(arrayAdapterItems);
//
//        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
//
//                // Display content to text View custom
//                if (item.equals("Custom")){
//                    edtCustom.setText(tvContent.getText().toString());
//                }
//            }
//        });
    }

    public void setDataByExtra() {
        Intent intent = getIntent();
        String content = intent.getStringExtra(MainActivity.CONTENT);

        tvContent.setText(content);
    }
}