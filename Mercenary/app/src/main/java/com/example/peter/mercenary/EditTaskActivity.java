package com.example.peter.mercenary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class EditTaskActivity extends AppCompatActivity {

    String encoded;  //  bitmap encoded to string for simple storage
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText taskTitle = findViewById(R.id.task_title);
        EditText taskDesc = findViewById(R.id.task_desc);
        Button saveChange = findViewById(R.id.save_change);
        Button addImgButton = findViewById(R.id.add_img_button);
        ImageView imgByte = findViewById(R.id.byte_img);
        GridView imgGrid = (GridView) findViewById(R.id.gridView);

        ArrayList<Bitmap> ImgBitmapArray = new ArrayList<Bitmap>();
        Task task = getIntent().getExtras().getParcelable("task");
        Log.i("taskID:",task.getId());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if (task.getPhoto().isEmpty())
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
            assert(task.getPhoto() != null);
            for (String S : task.getPhoto()){
                Log.i("HUGE!!!",Integer.toString(S.length()));
                ImgBitmapArray.add(getBitmapFromString(S));
            }

            ImageAdapter myImageAdapter = new ImageAdapter(this);
            myImageAdapter.setImgBitmapArray(ImgBitmapArray);
            imgGrid.setAdapter(myImageAdapter);


            imgGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Toast.makeText(EditTaskActivity.this, "" + position,
                            Toast.LENGTH_SHORT).show();

                    final Intent intent = new Intent(EditTaskActivity.this, SingleImgActivity.class);
                    intent.putExtra("Bitmap",ImgBitmapArray.get(position));
                    startActivity(intent);
                }
            });
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

        saveChange.setOnClickListener(v -> {
            //create query
            //and upload
            //TODO: detect if photo list if empty; if empty then initialize it; if not then add photo to it
            if (encoded != null ) {
                //String escapedImg = org.apache.lucene.queryparser.classic.QueryParser.escape(encoded);


                assert task != null;
                task.getPhoto().add(encoded);
            }

            Log.i("taskID:",task.getId());

            //10 is the min limit
            if (task.getPhoto().size() > 11){
                Collections.rotate(task.getPhoto(), task.getPhoto().size()-11);
            }
            try {
                task.setDescription(taskDesc.getText().toString());
            } catch (DescTooLongException e) {
                e.printStackTrace();
            }
            try {
                task.setTitle(taskTitle.getText().toString());
            } catch (TitleTooLongException e) {
                e.printStackTrace();
            }


            //Log.i("after", currentTask.getId());

            ElasticFactory.UpdateTask addTask = new ElasticFactory.UpdateTask();
            addTask.execute(task);

            // if task updated then toast success
            // if no internet toast a fail message
            // else toast a fail message
            // TODO: we need to get result from the server and if internet is connected
            Toast toast = Toast.makeText(getApplicationContext(), "Task updated",
                    Toast.LENGTH_SHORT);
            toast.show();
        });





    }
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
