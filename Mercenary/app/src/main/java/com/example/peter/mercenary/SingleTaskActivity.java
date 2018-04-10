package com.example.peter.mercenary;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {
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

        GridView imgGrid = (GridView) findViewById(R.id.gridView);



        ImageButton map = findViewById(R.id.mapBtn);
        task = getIntent().getParcelableExtra("task");
        user = getIntent().getParcelableExtra("user");




        ArrayList<String> taskImgStringList = task.getPhoto();
        final ArrayList<Bitmap> ImgBitmapArray = new ArrayList<Bitmap>();


        /*if (bundle != null) {
            userText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                    //when the user clicks on the username go to the userprofile
                }
            });
            editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentEdit = new Intent(getApplicationContext(), EditTaskActivity.class);
                    startActivity(intentEdit);
                    intentEdit.putExtra("task", task);
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
        }*/
        if (taskImgStringList.isEmpty())
        {
            //Log.i("!taskimg", "is empty" );

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
                    //Log.i("taskid in single",task.getId());
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

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