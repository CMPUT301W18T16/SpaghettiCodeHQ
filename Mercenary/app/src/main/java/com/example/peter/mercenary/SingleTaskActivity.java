package com.example.peter.mercenary;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.graphics.BitmapCompat;
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

import com.google.common.base.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Objects.equal;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {
    User currentUser; //currently logged in user
    byte[] byteArray;  // base64 byte array of the compressed img
    String encoded;  //  base64 byte array encoded to string for simple storage
    User taskRequester;
    String taskDescriptionString;
    String taskStatusString;
    String taskTitleString;
    String taskImgString;
    String taskIDString;
    Task currentTask;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView taskTitle = findViewById(R.id.task_title);
        final TextView taskDesc = findViewById(R.id.task_desc);
        final TextView taskStatus = findViewById(R.id.task_status);
        TextView userText = findViewById(R.id.usernameText);
        Button addImgButton = findViewById(R.id.add_img_button);
        ImageButton map = findViewById(R.id.mapBtn);
        Button saveTaskButton = findViewById(R.id.task_save_button);
        ImageView imgByte = findViewById(R.id.byte_img);

         Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            taskRequester = bundle.getParcelable("user");
            taskTitleString = bundle.getString("task_title");
            taskDescriptionString = bundle.getString("task_desc");
            taskStatusString = bundle.getString("task_status");
            taskImgString = bundle.getString("task_img");
            currentTask = bundle.getParcelable("task");
            taskIDString = bundle.getString("task_id");
            taskTitle.setText(taskTitleString);
            taskDesc.setText(taskDescriptionString);
            taskStatus.setText(taskStatusString);
            Log.i("singletTaskID", taskIDString);



            // deal with single image first

            if (StringUtils.isEmpty(taskImgString))
            {
                Log.i("!taskimg", "is empty" );
            }
            else{
                Log.i("check","img equal didnt pass");
                Log.i("currentimg",taskImgString);
                String imgFromServer = bundle.getString("task_img");
                Log.i("checking","img from server has "+imgFromServer.length());
                byte[] decodedBase64 = Base64.decode(imgFromServer, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBase64, 0, decodedBase64.length);
                imgByte.setImageBitmap(decodedByte);
                encoded = taskImgString;
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

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create query
                //and upload
                currentTask.setPhoto(encoded);
                currentTask.setId(taskIDString);
                currentTask.setStatus(taskStatusString);
                currentTask.setRequester(taskRequester.getUsername());
                try {
                    currentTask.setDescription(taskDescriptionString);
                } catch (DescTooLongException e) {
                    e.printStackTrace();
                }
                try {
                    currentTask.setTitle(taskTitleString);
                } catch (TitleTooLongException e) {
                    e.printStackTrace();
                }
                Log.i("after", currentTask.getId());

                ElasticFactory.UpdateTask addTask = new ElasticFactory.UpdateTask();
                addTask.execute(currentTask);


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




                // make temp storage for our image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap compressedImage = selectedImage.copy(selectedImage.getConfig(), true);
                FileOutputStream out = null;
                String cacheDir = getCacheDir().toString();
                File dir = new File(cacheDir);
                if(!dir.exists())
                    dir.mkdirs();
                // store img to cache file
                File file = new File(dir, "cached"  + ".png");
                out = new FileOutputStream(file,false);
                compressedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();


                Log.i("I have an img!", "size: " + Long.toString(file.length()));
                int selectedImageSize = Math.toIntExact(file.length());
                int compressedImageSize = selectedImageSize;
                if (compressedImageSize > 65536){
                    int compressedImgWidth = compressedImage.getWidth();
                    int compressedImgHeight = compressedImage.getHeight();

                    while (compressedImageSize > 65536){
                        // toast for long time image processing
                        Toast toast = Toast.makeText(getApplicationContext(), "Your image is being processed",
                                Toast.LENGTH_SHORT);
                        toast.show();

                        // iteratively reduces image size
                        compressedImgWidth = (int)(compressedImgWidth*0.9);
                        compressedImgHeight = (int)(compressedImgHeight*0.9);
                        compressedImage = Bitmap.createScaledBitmap(compressedImage,compressedImgWidth, compressedImgHeight, true);

                        // save the reduced file to mobile cache storage
                        try {
                            out = new FileOutputStream(file,false);
                            compressedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.flush();
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                         compressedImageSize = Math.toIntExact(file.length());
                         Log.i("compressing", "size: " + Integer.toString(compressedImageSize));
                    }
                }
                else{
                    // don't compress
                }

                final ImageView bitmapView = findViewById(R.id.byte_img);
                bitmapView.setImageBitmap(compressedImage);
                compressedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.i("!!HUGE!!", byteArray.toString());
                Log.i("!!SIZE!!",Integer.toString(selectedImageSize) );
                Log.i("!!BYTE_SIZE!!", Integer.toString(byteArray.length));
                Log.i("STRING_LEN!", String.valueOf(encoded.length()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i("NO", "no image selected!!");

        }

        // time to save your stuffs

    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}



