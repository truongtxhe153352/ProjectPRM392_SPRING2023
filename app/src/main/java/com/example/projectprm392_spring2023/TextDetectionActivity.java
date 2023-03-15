package com.example.projectprm392_spring2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.Manifest;

public class TextDetectionActivity extends AppCompatActivity {
    Button btnCapture;
    Button btnGallery;
    TextView txtData;
    ImageView imgView;
    Activity activity;
    Uri imageUri;

    private static final int PERMISSION_CODE = 100;
    private static final int SELECT_IMAGE_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2001;



    private void bindingView() {
        btnCapture = findViewById(R.id.btnCapture);
        btnGallery = findViewById(R.id.btnGallery);
        txtData = findViewById(R.id.txtData);
        imgView = findViewById(R.id.imageView);
        activity = TextDetectionActivity.this;
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_images);
        bindingView();
        bindingAction();
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

                // Start text detection
                TextDetectionTask textDetectionTask = new TextDetectionTask();
                textDetectionTask.execute();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    class TextDetectionTask extends AsyncTask<Void, Void, Void> {
        String detectedText;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TextDetectionActivity.this,
                    "Text Recognition",
                    "Detecting...");
        }
        @Override
        protected Void doInBackground(Void... voids) {
            detectedText = "Here's some detected text";
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            startPromptActivity(detectedText);
        }
    }



    private void startPromptActivity(String detectedText) {
        Intent intent = new Intent(TextDetectionActivity.this, PromptActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        intent.putExtra("detectedText", detectedText);
        startActivity(intent);
    }

    private String detectText(Bitmap bitmapImage) {
        return "Here's some detected text";
    }

    public void textFind() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(activity).build();
        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < sparseArray.size(); i++) {
            TextBlock textBlock = sparseArray.get(i);
            String str = textBlock.getValue();
            stringBuilder.append(str);
        }
        txtData.setText(stringBuilder);
    }


    private void findTextByBitmap(Bitmap bitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(TextDetectionActivity.this, "Error Occur!", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < sparseArray.size(); i++) {
                TextBlock textBlock = sparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            txtData.setText(stringBuilder.toString());
        }
    }

}

