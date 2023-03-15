package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button askBtn;
    private EditText promptEditText;

    private void bindingView() {
        askBtn = (Button) findViewById(R.id.askBtn);
        promptEditText = (EditText) findViewById(R.id.promptEditText);
    }
    
    private void bindingAction() {
        askBtn.setOnClickListener(this::askBtnOnClick);
    }

    private void askBtnOnClick(View view) {
        ChatGptRequest chatGptRequest = new ChatGptRequest(promptEditText.getText().toString(), "thaiduongdeptrai");
        ChatGptTask chatGptTask = new ChatGptTask(MainActivity.this);
        chatGptTask.execute(chatGptRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        bindingAction();
    }
}

class ChatGptTask extends AsyncTask<ChatGptRequest, Void, ChatGptResponse> {
    private static final String API_ENDPOINT = "http://54.169.182.6:3000/conversation";
    private Gson gson;
    private String secretKey;
    private Context parentContext;
    public ChatGptTask(Context context) {
        parentContext = context;
        gson = new Gson();
    }

    @Override
    protected ChatGptResponse doInBackground(ChatGptRequest... chatGptRequests) {
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
        Toast.makeText(parentContext, chatGptResponse.getResponse(), Toast.LENGTH_SHORT).show();
        super.onPostExecute(chatGptResponse);

    }
}