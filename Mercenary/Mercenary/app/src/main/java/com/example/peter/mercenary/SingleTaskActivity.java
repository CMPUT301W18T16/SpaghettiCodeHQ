package com.example.peter.mercenary;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.graphics.BitmapCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import android.widget.ImageView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.base.Objects;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.searchbox.core.DocumentResult;

import static com.google.common.base.Objects.equal;

import java.util.concurrent.ExecutionException;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity  {
    private String encoded;  //  bitmap encoded to string for simple storage
    private String taskDescriptionString;
    private String taskStatusString;
    private String taskTitleString;
    private String taskIDString;
  
    private ArrayList<String> taskImgStringList;
    private ArrayList<Bitmap> ImgBitmapArray = new ArrayList<Bitmap>();
    private ArrayAdapter<String> imageArrayAdapter;
  
    private User user; //currently logged in user
    private Task task;
    private User clickedUser; //target user
    private static final String TASKFILE = "taskfile.sav";
    private static final String ELASTICFILE = "elasticfile.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDesc = findViewById(R.id.task_desc);
        TextView userText = findViewById(R.id.usernameText);
        Button editTask = findViewById(R.id.edit_task);
        Button completed = findViewById(R.id.completedBtn);

        ImageButton map = findViewById(R.id.mapBtn);
        task = getIntent().getParcelableExtra("task");
        user = getIntent().getParcelableExtra("user");

        taskTitle.setText(task.getTitle());
        taskDesc.setText(task.getDescription());

        String query = "{\n" + " \"query\": { \"match\": {\"_id\":\"" + task.getUserId() + "\"} }\n" + "}";

        if (NetworkStatus.connectionStatus(this)) {
            try {
                ElasticFactory.GetUser getUser = new ElasticFactory.GetUser();
                clickedUser = getUser.execute(query).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            userText.setText(clickedUser.getUsername());
            userText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                    //when the user clicks on the username go to the userprofile
                    Intent intent = new Intent(SingleTaskActivity.this, UserProfile.class);
                    intent.putExtra("user", user.getUsername());
                    intent.putExtra("clicked_user", clickedUser.getUsername());
                    startActivity(intent);
                }
            });

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("goal", "single");
                    intent.putExtra("lat", task.getGeoLoc().latitude);
                    intent.putExtra("long", task.getGeoLoc().longitude);
                    startActivity(intent);
                }
            });

            editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SingleTaskActivity.this, EditTaskActivity.class);
                    intent.putExtra("task", task);
                    startActivity(intent);
                }
            });

            if (task.getStatus().equals("accepted")
                    && task.getUserName().equals(user.getUsername())) {
                completed.setVisibility(View.VISIBLE);
                completed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task.setStatus("completed");
                        Intent intent = new Intent(SingleTaskActivity.this, RateReviewActivity.class);
                        intent.putExtra("username", task.getAcceptedUser());
                        startActivity(intent);
                    }
                });
            } else {
                completed.setVisibility(View.INVISIBLE);
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Currently offline, functionalities may not be available.", Toast.LENGTH_LONG);
        }
    }


    // picking img
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
                //TODO: check if the img returned is a dup in our database; if dup then dont upload
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                // make temp storage for our image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap compressedImage = selectedImage.copy(selectedImage.getConfig(), true);
                FileOutputStream out;
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
                if (compressedImageSize > 65536*0.7){
                    int compressedImgWidth = compressedImage.getWidth();
                    int compressedImgHeight = compressedImage.getHeight();

                    while (compressedImageSize > 65536*0.7){
                        // toast for long time image processing
                        Toast toast = Toast.makeText(getApplicationContext(),
                                               "Your image is processed",
                                                     Toast.LENGTH_SHORT);
                        toast.show();

                        // iteratively reduces image size
                        compressedImgWidth = (int)(compressedImgWidth*0.9);
                        compressedImgHeight = (int)(compressedImgHeight*0.9);
                        compressedImage = Bitmap.createScaledBitmap(compressedImage,
                                                                    compressedImgWidth,
                                                                    compressedImgHeight,
                                                             true);

                        // save the reduced file to mobile cache storage
                        try {
                            out = new FileOutputStream(file,false);
                            compressedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
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
                        // Log.i("compressing", "size: " + Integer.toString(compressedImageSize));
                    }
                }
                else{
                    // don't compress
                }

                final ImageView bitmapView = findViewById(R.id.byte_img);
                bitmapView.setImageBitmap(compressedImage);
                encoded = getStringFromBitmap(compressedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (resultCode == Activity.RESULT_CANCELED){
            //Log.i("NO", "no image selected!!");
            Toast toast = Toast.makeText(getApplicationContext(), "No image selected",
                    Toast.LENGTH_SHORT);
            toast.show();

        }
        else{

        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
        finish();

    }

    // image adapter for our lists of images
    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Bitmap> ImgBitmapArray;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public void setImgBitmapArray(ArrayList<Bitmap> myImgBitmapArray){
            this.ImgBitmapArray = myImgBitmapArray;
        }
        public int getCount() {
            return ImgBitmapArray.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(240,240));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(3, 3, 3, 3);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(ImgBitmapArray.get(position));
            return imageView;
        }
    }


    // TODO:reference
    private String getStringFromBitmap(Bitmap bitmapPicture) {
 /*
 * This functions converts Bitmap picture to a string which can be
 * JSONified.
 * */
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}


