package com.example.projectprm392_spring2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class ResultActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private TextInputEditText chatgptResponseEditText;
    private ImageView imagePreview;
    private TextInputEditText promptUsedEditText;

    private Button btnSave;

    private Button btnAnotherPhoto;

    private String noteTitle = "";
    private String imageUri = "";

    private void bindingView() {
        chatgptResponseEditText = findViewById(R.id.edt_Content);
        imagePreview = findViewById(R.id.img_cam_detail);
        promptUsedEditText = findViewById(R.id.promptUsedEditText);
        btnSave = findViewById(R.id.btnCopy);
        btnAnotherPhoto = findViewById(R.id.btn_Delete);
        navigationView =  findViewById(R.id.nav_view);
    }

    private void bindingAction() {
        btnSave.setOnClickListener(this::btnSaveOnClick);
        btnAnotherPhoto.setOnClickListener(this::btnAnotherPhoto);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void btnAnotherPhoto(View view) {
        Intent i = new Intent(ResultActivity.this, TextDetectionActivity.class);
        startActivity(i);
    }

    private void btnSaveOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteTitle = input.getText().toString();
                if (noteTitle.isEmpty()) {
                    Toast.makeText(ResultActivity.this, "Title is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    History newHistory = new History( chatgptResponseEditText.getText().toString(), promptUsedEditText.getText().toString(), imageUri, noteTitle);
                    HistoryDatabase.getInstance(ResultActivity.this).historyDAO().insertHistory(newHistory);
                    Toast.makeText(ResultActivity.this, "Saved history", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        bindingView();
        bindingAction();
        receivingIntent();

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
                Intent i = new Intent(ResultActivity.this, TextDetectionActivity.class);
                startActivity(i);
                Log.d("thaiduongme", "New photo chose");
                break;
            }
            case R.id.nav_history: {
                Intent i = new Intent(ResultActivity.this, HistoryList.class);
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


    private void receivingIntent() {
        Intent i = getIntent();
        if (i != null) {
            if (i.getStringExtra("chatgptResponse") != null) {
                String chatgptResponse = i.getStringExtra("chatgptResponse");
                chatgptResponseEditText.setText(chatgptResponse);
            }
            if (i.getStringExtra("imageUri") != null) {
                imageUri = i.getStringExtra("imageUri");
                imagePreview.setImageURI(Uri.parse(i.getStringExtra("imageUri")));
            }
            if (i.getStringExtra("promptUsed") != null) {
                String promptUsed = i.getStringExtra("promptUsed");
                promptUsedEditText.setText(promptUsed);
            }
        }
    }

}