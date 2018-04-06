package com.example.peter.mercenary;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {
    User currentUser; //currently logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDesc = findViewById(R.id.task_desc);
        TextView taskStatus = findViewById(R.id.task_status);
        TextView userText = findViewById(R.id.usernameText);
        Button addImgButton = findViewById(R.id.add_img_button);
        ImageButton map = findViewById(R.id.mapBtn);

        ImageView imgByte = findViewById(R.id.byte_img);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null){


            User taskRequester = bundle.getParcelable("user");
            taskTitle.setText(bundle.getString("task_title"));
            taskDesc.setText(bundle.getString("task_desc:")+taskRequester.getUsername());
            taskStatus.setText(bundle.getString("task_status"));


            // deal with single image first
            Log.i("!taskimg", "is " + bundle.get("task_img").getClass().getName());
            if (bundle.getByteArray("task_img") == null){
                Log.i("!taskimg", "is empty" );

            }
            else{
                byte[] decodedString = Base64.decode(bundle.getString("task_img"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgByte.setImageBitmap(decodedByte);

            }

        }
        addImgButton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE);

            }
        });

        userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                //when the user clicks on the username go to the userprofile
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("Lat", 53.526f); //needs to pass task location
                intent.putExtra("Long", -113.525f);
                startActivity(intent);
            }
        });
    }


    // picking img (one)
    // reference: https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    //
    public static final int PICK_IMAGE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                // TODO:
                // {
                // minci: check img size. if img size is less than 64kb then ignore compression
                // go straightly to bytes converting
                // }
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                // check image size
                int selectedImageSize = selectedImage.getByteCount();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap compressedImage = selectedImage.copy(selectedImage.getConfig(), true);
                int compressedImageSize = selectedImageSize;

                if (compressedImageSize > 65536){
                    int compressedImgWidth = compressedImage.getWidth();
                    int compressedImgHeight = compressedImage.getHeight();

                    while (compressedImageSize > 65536){
                         compressedImgWidth = (int)(compressedImgWidth*0.9);
                         compressedImgHeight = (int)(compressedImgHeight*0.9);
                         compressedImage = Bitmap.createScaledBitmap(compressedImage,compressedImgWidth, compressedImgHeight, true);

                         compressedImageSize = compressedImage.getByteCount();
                         Log.i("compressing", "size: " + Integer.toString(compressedImageSize));
                    }
                }
                else{
                    // don't compress
                }

                final ImageView bitmapView = findViewById(R.id.byte_img);
                bitmapView.setImageBitmap(compressedImage);
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.i("!!HUGE!!", byteArray.toString());
                Log.i("!!SIZE!!",Integer.toString(selectedImageSize) );
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }
        else{
            Log.i("NO", "no image selected!!");

        }

    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}



