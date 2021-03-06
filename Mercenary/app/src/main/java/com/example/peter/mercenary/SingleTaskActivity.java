package com.example.peter.mercenary;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
    private Button delTask;
    private Button completed;
    private Button editTask;
    private EditText makeBid;
    private TextView seeBid;
    private boolean myTask=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        task = getIntent().getParcelableExtra("task");
        user = getIntent().getParcelableExtra("user");


        if (task.getUserId().equals(user.getId())) {

            setContentView(R.layout.single_task_activity);
             editTask = findViewById(R.id.edit_task);
             delTask = findViewById(R.id.task_del);
             seeBid = findViewById(R.id.see_bid);
        }
        else{
            myTask=false;
            setContentView(R.layout.other_single_task_activity);
            makeBid = findViewById(R.id.make_bid);

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        completed = findViewById(R.id.completedBtn);

        TextView taskTitle = findViewById(R.id.task_title_edit);
        TextView taskDesc = findViewById(R.id.task_desc_edit);
        TextView userText = findViewById(R.id.usernameText);


        GridView imgGrid = findViewById(R.id.img_grid_view_task);
        ImageButton map = findViewById(R.id.mapBtn);

        String taskId = getIntent().getStringExtra("taskid");
        TextView showTaskStatus = findViewById(R.id.task_act_status);
        showTaskStatus.setText(task.getStatus());
        taskImgStringList = task.getPhoto();
        if (taskImgStringList.isEmpty()){

        }
        else{
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
            if(myTask) {

                delTask.setOnClickListener((View v) -> {
                    //TODO: delete button   -DONE
                    //TODO: popup to confirm deleting


                    // confirmation dialog
                    //ConfirmDeletingDialog deletingTaskDialog =  new ConfirmDeletingDialog();
                    //deletingTaskDialog.setTargetFragment(deletingTaskDialog, 0);
                    //deletingTaskDialog.show(getFragmentManager(), "tag");

                    ElasticFactory.DeletingTask deletingTask = new ElasticFactory.DeletingTask();
                    deletingTask.execute(taskId);

                    // toast to notify user a task is deleted
                    Toast toast = Toast.makeText(getApplicationContext(), "Task deleted",
                            Toast.LENGTH_SHORT);
                    toast.show();

                    // grey out delete and save buttons to prevent user to deleting/saving non-existent task
                    delTask.setAlpha(.5f);
                    delTask.setClickable(false);

                });

                editTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SingleTaskActivity.this, EditTaskActivity.class);
                        intent.putExtra("task", task);
                        intent.putExtra("user", user);
                        // I must get taskId here, otherwise there's no way a task can update
                        // unless you want to pass a whole tasklist to every activity uses taskid ^^
                        intent.putExtra("taskid", taskId);
                        startActivity(intent);
                    }
                });


            }
            else{
                makeBid.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SingleTaskActivity.this, MakeBid.class);
                        intent.putExtra("task", task);
                        intent.putExtra("user", user);
                        // I must get taskId here, otherwise there's no way a task can update
                        // unless you want to pass a whole tasklist to every activity uses taskid ^^
                        intent.putExtra("taskid", taskId);
                        startActivity(intent);
                    }
                });
            }

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


    // reference: http://mobile.cs.fsu.edu/converting-images-to-json-objects/
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


