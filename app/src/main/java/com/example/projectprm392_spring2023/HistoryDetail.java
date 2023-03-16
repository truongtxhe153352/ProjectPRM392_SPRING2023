package com.example.projectprm392_spring2023;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryDetail extends AppCompatActivity {

    private EditText txtTitleDetail;
    private EditText edt_content;
    private ImageView img_cam_detail;
    private Button btnSave;
    private Button btnCopy;
    private Button btnDelete;

    private void BindingView(){
        txtTitleDetail = findViewById(R.id.txtTitleDetail);
        edt_content = findViewById(R.id.edt_Content);
        img_cam_detail = findViewById(R.id.img_cam_detail);
        btnSave  = findViewById(R.id.btn_Save);
        btnCopy = findViewById(R.id.btnCopy);
        btnDelete = findViewById(R.id.btn_Delete);
    }
    private void BindingAction(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        final History history = (History) bundle.get("object_history");
        txtTitleDetail.setText(history.getDate());
        edt_content.setText(history.getContent());
        img_cam_detail.setImageResource(history.getResourceId());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save(history);
            }
        });

        btnCopy.setOnClickListener(view -> {
            Copy();
        });

        btnDelete.setOnClickListener(view -> {
            Delete(history);

        });

    }
    private void Save(History history){
        String date = txtTitleDetail.getText().toString().trim();
        String content = edt_content.getText().toString().trim();

        if(date == history.getDate() || content == history.getContent()){
            return;
        }
        history.setDate(date);
        history.setContent(content);

        HistoryDatabase.getInstance(HistoryDetail.this).historyDAO().updateHistory(history);
        Toast.makeText(HistoryDetail.this, "Save successfully", Toast.LENGTH_SHORT).show();
        Intent ca = new Intent(getApplicationContext(), HistoryList.class);
        startActivity(ca);
    }
    private void Copy(){
        String content = edt_content.getText().toString();
        if(content.isEmpty()){
            Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
        }else{
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("MyData", content);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }
    private void Delete(History history){
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete user")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HistoryDatabase.getInstance(HistoryDetail.this).historyDAO().deleteHistory(history);
                        Toast.makeText(HistoryDetail.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                        Intent ca = new Intent(getApplicationContext(), HistoryList.class);
                        startActivity(ca);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        BindingView();
        BindingAction();

    }
}