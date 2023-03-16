package com.example.projectprm392_spring2023;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class HistoryDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private TextView txtTitleDetail;
    private EditText edt_content;
    private ImageView img_cam_detail;
    private Button btnSave;
    private Button btnCopy;
    private Button btnDelete;

    private TextInputEditText promptUsedEditText;

    private void BindingView(){
        txtTitleDetail = findViewById(R.id.txtTitleDetail);
        edt_content = findViewById(R.id.edt_Content);
        img_cam_detail = findViewById(R.id.img_cam_detail);
//        btnSave  = findViewById(R.id.btn_Save);
        btnCopy = findViewById(R.id.btnCopy);
        btnDelete = findViewById(R.id.btn_Delete);
        navigationView =  findViewById(R.id.nav_view);
        promptUsedEditText = findViewById(R.id.promptUsedEditText);
    }
    private void BindingAction(){
        navigationView.setNavigationItemSelectedListener(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        final History history = (History) bundle.get("object_history");
        txtTitleDetail.setText(history.getTitle());
        edt_content.setText(history.getChatgptResponse());
        img_cam_detail.setImageURI(Uri.parse(history.getImageUri()));
        promptUsedEditText.setText(history.getPromptUsed());

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Save(history);
//            }
//        });

        btnCopy.setOnClickListener(view -> {
            Copy();
        });

        btnDelete.setOnClickListener(view -> {
            Delete(history);

        });

    }
//    private void Save(History history){
//        String title = txtTitleDetail.getText().toString().trim();
//        String content = edt_content.getText().toString().trim();
//
//        if(date == history.getDate() || content == history.getContent()){
//            return;
//        }
//        history.setDate(date);
//        history.setContent(content);
//
//        HistoryDatabase.getInstance(HistoryDetail.this).historyDAO().updateHistory(history);
//        Toast.makeText(HistoryDetail.this, "Saved successfully", Toast.LENGTH_SHORT).show();
//        Intent ca = new Intent(getApplicationContext(), HistoryList.class);
//        startActivity(ca);
//    }
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
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HistoryDatabase.getInstance(HistoryDetail.this).historyDAO().deleteHistory(history);
                        Toast.makeText(HistoryDetail.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
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

        // Sidebar menu
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        drawerLayout.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_new_photo: {
                Intent i = new Intent(HistoryDetail.this, TextDetectionActivity.class);
                startActivity(i);
                Log.d("thaiduongme", "New photo chose");
                break;
            }
            case R.id.nav_history: {
                Intent i = new Intent(HistoryDetail.this, HistoryList.class);
                startActivity(i);
                Log.d("thaiduongme", "New history chose");
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}