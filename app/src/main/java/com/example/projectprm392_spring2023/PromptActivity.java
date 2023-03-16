package com.example.projectprm392_spring2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PromptActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button btnSave;
    private TextInputEditText detectedTextEditText;
    private TextInputEditText customPromptEditText;
    private TextInputLayout customPromptLayout;
    private ImageView imageView;
    private String currentImageUri;

    public static int return_fromActivity1 = 1000;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private void bindingView() {
        btnSave = findViewById(R.id.btnCopy);
        imageView = findViewById(R.id.img_cam_detail);
        customPromptLayout = findViewById(R.id.customPromptLayout);
        detectedTextEditText = findViewById(R.id.detectedTextEditText);
        customPromptEditText = findViewById(R.id.customPromptEditText);
        navigationView =  findViewById(R.id.nav_view);
    }


    String[] items = {"Summarize", "Give me the answer", "Paraphrase", "Give me some important keywords", "Custom"};

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
                if (item == "Custom") {
                    customPromptLayout.setVisibility(View.VISIBLE);
                } else {
                    customPromptLayout.setVisibility(View.GONE);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
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
                Intent i = new Intent(PromptActivity.this, TextDetectionActivity.class);
                startActivity(i);
                Log.d("thaiduongme", "New photo chose");
                break;
            }
            case R.id.nav_history: {
                Intent i = new Intent(PromptActivity.this, HistoryList.class);
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


    private void bindingAction() {
        btnSave.setOnClickListener(this::onBtnSaveClick);
    }

    private void onBtnSaveClick(View view) {
        // Get current detected text
        String detectedText= detectedTextEditText.getText().toString();
        if (detectedText.isEmpty()) {
            Toast.makeText(PromptActivity.this, "Detected text is empty!" , Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current prompt
        String currentPrompt = autoCompleteTxt.getText().toString();
        if (currentPrompt.isEmpty()) {
            Toast.makeText(PromptActivity.this, "You must select a prompt before asking ChatGPT!" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentPrompt.equalsIgnoreCase("Custom")) {
            currentPrompt = customPromptEditText.getText().toString();
        }
        String prompt = currentPrompt + "\n" + '"' + detectedText +'"';

        String secretKey = "thaiduongdeptrai";
        ChatGptRequest chatGptRequest = new ChatGptRequest(prompt, secretKey);

        AskChatGptTask askChatGptTask = new AskChatGptTask();
        askChatGptTask.execute(chatGptRequest);

    }
    class AskChatGptTask extends AsyncTask<ChatGptRequest, Void, ChatGptResponse> {
        private static final String API_ENDPOINT = "http://54.169.182.6:3000/conversation";
        private Gson gson;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PromptActivity.this,
                    "Ask ChatGPT",
                    "Waiting for response...");
        }
        @Override
        protected ChatGptResponse doInBackground(ChatGptRequest... chatGptRequests) {
            gson = new Gson();
            String jsonRequest = gson.toJson(chatGptRequests[0]);
            try {
                URL url = new URL(API_ENDPOINT);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonRequest.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response data
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    inputStream.close();

                    // Convert the response data from JSON to a Java object
                    ChatGptResponse responseData = gson.fromJson(response.toString(), ChatGptResponse.class);

                    // Do something with the response data
                    return responseData;
                } else {
                    // Handle error
                }

                connection.disconnect();

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ChatGptResponse chatGptResponse) {
            progressDialog.dismiss();
            if (chatGptResponse.getResponse() == null) {
                Toast.makeText(PromptActivity.this, "Something went wrong, couldn't receive any response from ChatGPT!", Toast.LENGTH_SHORT).show();
                return;
            }
//            Toast.makeText(PromptActivity.this, chatGptResponse.getResponse(), Toast.LENGTH_SHORT).show();
            super.onPostExecute(chatGptResponse);
            startResultActivity(chatGptResponse.getResponse());
        }
    }
    private void startResultActivity(String response) {
        Intent intent = new Intent(PromptActivity.this, ResultActivity.class);
        intent.putExtra("imageUri", currentImageUri);
        intent.putExtra("detectedText", detectedTextEditText.getText().toString());
        intent.putExtra("chatgptResponse", response);
        intent.putExtra("promptUsed", autoCompleteTxt.getText().toString().equalsIgnoreCase("Custom") ?  customPromptEditText.getText().toString() : autoCompleteTxt.getText().toString());
        startActivity(intent);
    }

    private void receivingIntent() {
        Intent i = getIntent();
        if (i != null) {
            if (i.getStringExtra("detectedText") != null) {
                String data = i.getStringExtra("detectedText");
                detectedTextEditText.setText(data);
            }
            if (i.getStringExtra("imageUri") != null) {
                currentImageUri = i.getStringExtra("imageUri");
                imageView.setImageURI(Uri.parse(i.getStringExtra("imageUri")));
            }
        }

    }


}