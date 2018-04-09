package com.example.peter.mercenary;


import android.app.Activity;
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


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity  {
    User currentUser; //currently logged in user
    String encoded;  //  bitmap encoded to string for simple storage
    User taskRequester;
    String taskDescriptionString;
    String taskStatusString;
    String taskTitleString;
    ArrayList<String> taskImgStringList;
    String taskIDString;
    Task currentTask;
    ArrayList<Bitmap> ImgBitmapArray = new ArrayList<Bitmap>();
    private ArrayAdapter<String> imageArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText taskTitle = findViewById(R.id.task_title);
        EditText taskDesc = findViewById(R.id.task_desc);
        TextView taskStatus = findViewById(R.id.task_status);
        TextView userText = findViewById(R.id.usernameText);
        Button addImgButton = findViewById(R.id.add_img_button);
        ImageButton map = findViewById(R.id.mapBtn);
        Button saveTaskButton = findViewById(R.id.task_save_button);
        ImageView imgByte = findViewById(R.id.byte_img);
        GridView imgGrid = (GridView) findViewById(R.id.gridView);

         Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            taskRequester = bundle.getParcelable("user");
            taskTitleString = bundle.getString("task_title");
            taskDescriptionString = bundle.getString("task_desc");
            taskStatusString = bundle.getString("task_status");
            taskImgStringList = bundle.getStringArrayList("task_img");
            currentTask = bundle.getParcelable("task");
            taskIDString = bundle.getString("task_id");
            taskTitle.setText(taskTitleString);
            taskDesc.setText(taskDescriptionString);
            taskStatus.setText(taskStatusString);
            //Log.i("singletTaskID", taskIDString);

            if (taskImgStringList.isEmpty())
            {
                //Log.i("!taskimg", "is empty" );

            }


            else{
                //TODO: photo grid adapter goes here
                //Log.i("check","there are some img(s)");

                // StringEscapeUtils is extremely slow
                //String unescapedImg = StringEscapeUtils.unescapeJson(imgFromServer);

                //Log.i("checking","img from server has "+taskImgStringList.size());

                // let's just test first img from the img list
                assert(taskImgStringList != null);
                for (String S : taskImgStringList){
                    Log.i("HUGE!!!",Integer.toString(S.length()));
                    ImgBitmapArray.add(getBitmapFromString(S));
                }

                ImageAdapter myImageAdapter = new ImageAdapter(this);
                myImageAdapter.setImgBitmapArray(ImgBitmapArray);
                imgGrid.setAdapter(myImageAdapter);


                imgGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Toast.makeText(SingleTaskActivity.this, "" + position,
                                Toast.LENGTH_SHORT).show();

                        final Intent intent = new Intent(SingleTaskActivity.this, SingleImgActivity.class);
                        intent.putExtra("Bitmap",ImgBitmapArray.get(position));
                        startActivity(intent);
                    }
                });
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

        userText.setOnClickListener(v -> {
            //todo
            //when the user clicks on the username go to the userprofile
        });

        map.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("Lat", 53.526f); //needs to pass task location
            intent.putExtra("Long", -113.525f);
            startActivity(intent);
        });

        saveTaskButton.setOnClickListener(v -> {
            //create query
            //and upload
            //TODO: detect if photo list if empty; if empty then initialize it; if not then add photo to it
            if (encoded != null ) {
                String escapedImg = org.apache.lucene.queryparser.classic.QueryParser.escape(encoded);
                taskImgStringList.add(escapedImg);
            }

            //10 is the min limit
            if (taskImgStringList.size() > 11){
                Collections.rotate(taskImgStringList, taskImgStringList.size()-11);
                taskImgStringList = new ArrayList<>(taskImgStringList.subList(0, 11));

            }




            currentTask.setPhoto(taskImgStringList);
            currentTask.setStatus(taskStatusString);
            currentTask.setRequester(taskRequester.getUsername());
            currentTask.setId(taskIDString);

            try {
                currentTask.setDescription(taskDesc.getText().toString());
            } catch (DescTooLongException e) {
                e.printStackTrace();
            }
            try {
                currentTask.setTitle(taskTitle.getText().toString());
            } catch (TitleTooLongException e) {
                e.printStackTrace();
            }
            //Log.i("after", currentTask.getId());

            ElasticFactory.UpdateTask addTask = new ElasticFactory.UpdateTask();
            addTask.execute(currentTask);

            // if task updated then toast success
            // if no internet toast a fail message
            // else toast a fail message
            // TODO: we need to get result from the server and if internet is connected
            Toast toast = Toast.makeText(getApplicationContext(), "Task updated",
                    Toast.LENGTH_SHORT);
            toast.show();
        });
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



