package com.example.projectprm392_spring2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

    private void bindingView() {
        chatgptResponseEditText = findViewById(R.id.chatgptResponseEditText);
        imagePreview = findViewById(R.id.imagePreview);
        promptUsedEditText = findViewById(R.id.promptUsedEditText);
        btnSave = findViewById(R.id.btnSave);
        btnAnotherPhoto = findViewById(R.id.btnAnotherPhoto);
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
                imagePreview.setImageURI(Uri.parse(i.getStringExtra("imageUri")));
            }
            if (i.getStringExtra("promptUsed") != null) {
                String promptUsed = i.getStringExtra("promptUsed");
                promptUsedEditText.setText(promptUsed);
            }
        }
    }

}