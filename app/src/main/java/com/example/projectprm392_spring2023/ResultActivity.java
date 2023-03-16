package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ResultActivity extends AppCompatActivity {

    private TextInputEditText chatgptResponseEditText;
    private ImageView imagePreview;
    private TextInputEditText promptUsedEditText;

    private Button btnSave;

    private Button btnAnotherPhoto;

    private void bindingView() {
        chatgptResponseEditText = findViewById(R.id.chatgptResponseEditText);
        imagePreview = findViewById(R.id.imagePreview);
        promptUsedEditText = findViewById(R.id.promptUsedEditText);
        btnSave = findViewById(R.id.btnSave);
        btnAnotherPhoto = findViewById(R.id.btnAnotherPhoto);
    }

    private void bindingAction() {
        btnSave.setOnClickListener(this::btnSaveOnClick);
        btnAnotherPhoto.setOnClickListener(this::btnAnotherPhoto);

    }

    private void btnAnotherPhoto(View view) {
        Intent i = new Intent(ResultActivity.this, TextDetectionActivity.class);
        startActivity(i);
    }

    private void btnSaveOnClick(View view) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        bindingView();
        bindingAction();
        receivingIntent();

    }

    private void receivingIntent() {
        Intent i = getIntent();
        if (i != null) {
            if (i.getStringExtra("chatgptResponse") != null) {
                String chatgptResponse = i.getStringExtra("chatgptResponse");
                chatgptResponseEditText.setText(chatgptResponse);
            }
            if (i.getStringExtra("imageUri") != null) {
                imagePreview.setImageURI(Uri.parse(i.getStringExtra("imageUri")));
            }
            if (i.getStringExtra("promptUsed") != null) {
                String promptUsed = i.getStringExtra("promptUsed");
                promptUsedEditText.setText(promptUsed);
            }
        }
    }

}