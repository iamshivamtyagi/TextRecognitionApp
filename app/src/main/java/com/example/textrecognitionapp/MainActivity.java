package com.example.textrecognitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    EditText mResultEt;
    ImageView mPreviewIv;

    public static final int CAMERA_REQUEST_CODE = 200;
    public static final int STORAGE_REQUEST_CODE = 400;
    public static final int IMAGE_PICK_GALLERY_CODE = 1000;
    public static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultEt = findViewById(R.id.et_result);
        mPreviewIv = findViewById(R.id.imageIv);

        //camera permission


    }

    //action menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    //handle actionbar clicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.addImage) {
            showImageImportDialog();
        }
        if (id == R.id.settings) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        //items to display in dialog
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //set title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //camera option clicked
                if (!checkCameraPermission()) {
                    //camera permission not allowed , request it
                    requestCameraPermission();
                } else {
                    //permission allowed , take pic
                    pickCamera();
                }
                if (which == 1) {
                    //gallery option clicked
                    if (!checkStoragePermission()) {
                        //storage permission not allowed , request it
                        requestStoragePermission();
                    } else {
                        //permission allowed , select pic
                        pickGallery();
                    }
                }
            }

            private void pickGallery() {
            }

            private void requestStoragePermission() {
            }

            private void pickCamera() {
            }

            private boolean checkStoragePermission() {
            }

            private void requestCameraPermission() {
            }

            private boolean checkCameraPermission() {
               /*Check camera permission and return result
               In order to capture high quality image we have to save image in external memory first
               before inserting to image view that's why storage permission also be required
                */

                boolean resultCam = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

                boolean resultStorage = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

                return resultCam && resultStorage;
            }
        });
    }


}
