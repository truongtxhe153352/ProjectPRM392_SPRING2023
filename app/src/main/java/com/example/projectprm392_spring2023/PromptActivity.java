package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PromptActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText edtData2;
    private ImageView imageView;
    public static int return_fromActivity1 = 1000;

    private void bindingView() {
        btnSave = findViewById(R.id.btnSave);
        edtData2 = findViewById(R.id.edtData2);
        imageView = findViewById(R.id.imageView2);
    }


    String[] items = {"Summary", "Give the answer", "Rewrite the passage", "Custom"};

    AutoCompleteTextView autoCompleteTxt;

    ArrayAdapter<String> arrayAdapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_api);
        bindingView();
        bindingAction();
        receivingIntent();


        // show select item
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        arrayAdapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);

        autoCompleteTxt.setAdapter(arrayAdapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void bindingAction() {
        btnSave.setOnClickListener(this::onBtnSaveClick);
    }

    private void onBtnSaveClick(View view) {
        // Send a ChatGPTRequest
        // When successfully, show the ChatGPTActivity screen and have the "Save" note button
        // Gọi chatGPTTask
        // Thành công thì startShowAfterScanActivity
        AskChatGptTask askChatGptTask = new AskChatGptTask();
        askChatGptTask.execute();

        String result = edtData2.getText().toString();
        Intent i = new Intent();
        i.putExtra("result", result);
        setResult(8, i);
        finish();
    }
    class AskChatGptTask extends AsyncTask<Void, Void, Void> {
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PromptActivity.this,
                    "Ask ChatGPT",
                    "Waiting for response...");
        }
        @Override
        protected Void doInBackground(Void... voids) {
            response = "Hello, I'm ChatGPT. Here's my response.";
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            startResultActivity(response);
        }
    }
    private void startResultActivity(String response) {
        Intent intent = new Intent(PromptActivity.this, ResultActivity.class);
//        intent.putExtra("imageUri", imageUri.toString());
//        intent.putExtra("detectedText", detectedText);
        startActivity(intent);
    }

    private void receivingIntent() {
        Intent i = getIntent();
        if (i != null) {
            imageView.setImageURI(Uri.parse(i.getStringExtra("imageUri")));
            String data = i.getStringExtra("detectedText");
            edtData2.setText(data);
            int inputManage=i.getIntExtra("image",1);
//            imageView.setImageResource(inputManage);
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_data_api);
//        bindingView();
//        bindingAction();
//        receivingIntent();
//    }
}