package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private Button detectBtn;
    private ImageView sourceImage;
    private TextView resultText;
    private Credentials credentials;
    private ImageAnnotatorSettings imageAnnotatorSettings;
    private ImageAnnotatorClient vision;

    private void bindingView() {
        detectBtn = (Button) findViewById(R.id.detectBtn);
        sourceImage = (ImageView) findViewById(R.id.sourceImage);
        resultText = (TextView) findViewById(R.id.resultText);
    }

    private void bindingAction() {
        detectBtn.setOnClickListener(this::detectBtnOnClick);
    }

    private void detectBtnOnClick(View view) {
        TextDetectionTask textDetectionTask = new TextDetectionTask();
        textDetectionTask.execute(sourceImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        bindingAction();
        try {
            prepareTextDetection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareTextDetection() throws Exception {
        // Load Google Cloud credential
        AssetManager assetManager = getAssets();
        credentials = ServiceAccountCredentials.fromStream(assetManager.open("credentials/gcloud.json"));

        // Initialize vision
        imageAnnotatorSettings = ImageAnnotatorSettings.newHttpJsonBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        vision = ImageAnnotatorClient.create(imageAnnotatorSettings);
    }

    private String detectText(Bitmap bitmapImage) {
        // Convert Bitmap image to ByteString
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bitMapData = stream.toByteArray();
        ByteString imgBytes = ByteString.copyFrom(bitMapData);

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Performs label detection on the image file
        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        // Return full text
        return responses.get(0).getFullTextAnnotation().getText();

    }

    class TextDetectionTask extends AsyncTask<ImageView, Void, Void> {
        String currentText;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Text Detection",
                    "Detecting...");
        }
        @Override
        protected Void doInBackground(ImageView... imageViews) {
            // Convert ImageView to Bitmap
            imageViews[0].setDrawingCacheEnabled(true);
            Bitmap bitmapImage = imageViews[0].getDrawingCache();

            currentText = detectText(bitmapImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            resultText.setText(currentText);
        }
    }
}