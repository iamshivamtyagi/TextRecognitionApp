package com.example.textrecognitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
        cameraPermission = new String{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //storage permission
        storagePermission = new String{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        } ;

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
                //intent to pick image from gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                //set intent type to image
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
            }

            private void requestStoragePermission() {
                ActivityCompat.requestPermissions(MainActivity.this,
                        storagePermission, STORAGE_REQUEST_CODE);
            }

            private void pickCamera() {
                //intent to take image from camera, it will also be save to storage to get high quality image
                ContentValues values = new ContentValues();
                //title of image
                values.put(MediaStore.Images.Media.TITLE,"NewPic");
                //description
                values.put(MediaStore.Images.Media.DESCRIPTION,"Image to text");
                image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
                startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

            }

            private boolean checkStoragePermission() {

                boolean resultStorage = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

                return resultStorage;
            }

            private void requestCameraPermission() {
                ActivityCompat.requestPermissions(MainActivity.this,
                        cameraPermission, CAMERA_REQUEST_CODE);

            }

            private boolean checkCameraPermission() {
               /*Check camera permission and return result
               In order to capture high quality image we have to save image in external memory first
               before inserting to image view that's why storage permission also be required
                */

                boolean resultCam = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

                boolean resultStorage = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

                return resultCam && resultStorage;
            }
        });
    }



}
