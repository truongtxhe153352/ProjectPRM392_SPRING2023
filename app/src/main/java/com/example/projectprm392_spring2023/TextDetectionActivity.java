package com.example.projectprm392_spring2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.Manifest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextDetectionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btnCapture;
    Button btnGallery;
    ImageView imgView;
    Activity activity;
    Uri imageUri;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private static final int PERMISSION_CODE = 100;
    private static final int SELECT_IMAGE_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2001;

    private final String credentialString = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"infra-edge-377903\",\n" +
            "  \"private_key_id\": \"1618ba12daf616fd5c07b4e0a80dc96d7d745818\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDfCVKfWnYQl9ei\\nywGczzlNd1pGq5+NszQxPRkNOaTpIdPDot0uWJTHendX9jl/sSwFE3poITbyqUVt\\nFpcV9OhD6WSnkIrPkd1ItFYxwf16i5DBkipnPcl4WNiqkzx0SReocJ0xkPRjaU0b\\n4n/Taz5h1kvag+T6j8IZmZf/azKn70baYj6oic/ytrr1qcAoGmTC8rSqQZmfz+Yu\\ndShcVfDeCLFQjFA7aE3mBWVdKTlSKKGUEO+IMymK5BHNelUp+v4bClgHHPKROn5t\\nHnsQ06ZK5tmPhv/bQ7dURCkM0zjmbN24qnBhI8CBprjp4bpwNXyTTt4x6ulp0e93\\nFMjIoaLXAgMBAAECggEAF4qJs1LTrgyuMKXyFaBfEVN3roiO3rX7hvbxSKQSSUkj\\na5LVJanT36TdfpCj9lu3pGbZ6gPCI6VviS7JOQK7QUUb3/6G/ZcNHuS/SaE1eqnL\\nLaNFNoB0XhotRFPQyGAD+ei+WWz4s/AkDhK/deFBRF0KGeux+isSTFbEjwERkl1M\\niKHBiPlfVfb7spITpK6EjP0jLS3Vdmy80WKWUOfPrcf9FSxtv8qnsw+kVcM4xu57\\n4Da5wmoT6MshdPCR9jDWtB7vPXQ4SxB+kj1hPU+X/f0HkCkS4hX0OlTlFOR5vdR6\\nd10iYFsCHv8r9Wd/ZasMP805Lv9NuyBDRiufB28cgQKBgQD/ytZdASoZZfS1ghNB\\nkswJsrySYx77sQX0p4hlg5Qfm0NjTnX/SgerCZH1AHMOdBFPKsOMn1kOkW+RFeXb\\nemC/IYuYZ5F3AlMwvonmcCE7f3LPEkPSqLKc3hTZz76TIYj4J89jypuiGUpJ6ao2\\n7ijwx1XBdNfuWOhn8wR0V7S4zQKBgQDfN610TcFOKT4cY3w15SoXVNYzqP9iwkwX\\n41HtvvhFtF/YRzZhdxYlBKRP/UujFw/qPl/3MoeQgXwEwsFra/7TnU7byN3GMZzu\\nDK60ApDdKpnt+TFMwoYDjh/viuONAu7XHRugUfDVy/1JVF6dRajMh7lr1HIKeGmv\\n8BIceEwaMwKBgQDcg069qu1SK/UELPjQxO6lRbMPN+hT7s5FldAeQ4qEnONBTzim\\nNnnZ10w+vH5z7VemuiUOq6ioyHQ3zXr09NFGtHKVlmNvB1AUa46SXAQqOLsPDJ3v\\nk4M3fLTY+SE/0d80n41IaDg3TDog8hHNfQ9KU234cvRcA8WHJ5elfJ7N2QKBgQDI\\niJ+m1dftHTY0kNfdH2UCEvBg9tmA4uczqsBG44LuNE7K4ackRrU5SoNHbVqPST++\\nBI14agfKNdmx+TmFBT+o4zEeWMWMsbO3VON6yrdUSlqBpZa5zCRTe1KLhG5Ll6/0\\nx6yzCgpgOGPzhf1+Mz+jh+d3zSMq7uSb8ASFWTmdawKBgHUOopqmwNY14HMkPI2f\\nd5vkPmG6zVHNsqghVia4pIzBt4xrNtBA2PWW70OAoIqBzCZXn1W6AuzOY6hlrOA1\\new47FqV0AiLjmvzQ1lB5k5bm/rH/brgq7ZS6Uugqfr869c89yEAoWTIeuupieN9j\\nnT629z2Naah5XEh05oA2V5MZ\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"camgpt@infra-edge-377903.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"113930226339496075868\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/camgpt%40infra-edge-377903.iam.gserviceaccount.com\"\n" +
            "}\n";

    private ImageAnnotatorSettings imageAnnotatorSettings;
    private ImageAnnotatorClient vision;


    private void bindingView() {
        btnCapture = findViewById(R.id.btnCapture);
        btnGallery = findViewById(R.id.btnGallery);
        imgView = findViewById(R.id.imageView);
        activity = TextDetectionActivity.this;
        navigationView =  findViewById(R.id.nav_view);
    }

    private void bindingAction() {
        btnCapture.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
            } else {
                //intent to take image from camera, it will also be save to storage to get high quality image
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "NewPick"); //title of the picture
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text"); //title of the picture
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detection);
        bindingView();
        bindingAction();

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
                Log.d("thaiduongme", "New photo chose");
                break;
            }
            case R.id.nav_history: {
                Intent i = new Intent(TextDetectionActivity.this, HistoryList.class);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK) {
            //got image from camera now crop it
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guid lines
                    .start(this);
        }


        if(requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(TextDetectionActivity.this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            // Cropped image successfully
            if (resultCode == RESULT_OK) {
                // Update imageView
                imageUri = result.getUri();
                imgView.setImageURI(imageUri);

                // Convert imgView to bitmap
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Start text detection
                TextDetectionTask textDetectionTask = new TextDetectionTask();
                textDetectionTask.execute(bitmapImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void prepareTextDetection() throws Exception {
        // Initialize vision
        if (imageAnnotatorSettings == null) {
            imageAnnotatorSettings = ImageAnnotatorSettings.newHttpJsonBuilder().setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new ByteArrayInputStream(credentialString.getBytes())))).build();

        }
        if (vision == null) {
            vision = ImageAnnotatorClient.create(imageAnnotatorSettings);
        }
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


    class TextDetectionTask extends AsyncTask<Bitmap, Void, Void> {
        String detectedText;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TextDetectionActivity.this,
                    "Text Recognition",
                    "Detecting...");
        }
        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            try {
                prepareTextDetection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            detectedText = detectText(bitmaps[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (detectedText == "") {
                Toast.makeText(TextDetectionActivity.this, "No text found, try again!", Toast.LENGTH_LONG).show();
            } else {
                startPromptActivity(detectedText);
            }
        }
    }



    private void startPromptActivity(String detectedText) {
        Intent intent = new Intent(TextDetectionActivity.this, PromptActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        intent.putExtra("detectedText", detectedText);
        startActivity(intent);
    }


//    public void textFind() {
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(activity).build();
//        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
//
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (int i = 0; i < sparseArray.size(); i++) {
//            TextBlock textBlock = sparseArray.get(i);
//            String str = textBlock.getValue();
//            stringBuilder.append(str);
//        }
//        txtData.setText(stringBuilder);
//    }


//    private void findTextByBitmap(Bitmap bitmap) {
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
//        if (!textRecognizer.isOperational()) {
//            Toast.makeText(TextDetectionActivity.this, "Error Occur!", Toast.LENGTH_SHORT).show();
//        } else {
//            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//            SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);
//            StringBuilder stringBuilder = new StringBuilder();
//
//            for (int i = 0; i < sparseArray.size(); i++) {
//                TextBlock textBlock = sparseArray.valueAt(i);
//                stringBuilder.append(textBlock.getValue());
//                stringBuilder.append("\n");
//            }
//            txtData.setText(stringBuilder.toString());
//        }
//    }

}

