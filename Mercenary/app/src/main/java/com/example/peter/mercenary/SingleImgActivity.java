package com.example.peter.mercenary;

import android.graphics.Bitmap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SingleImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_img);
        Bitmap BitmapImg = (Bitmap) getIntent().getExtras().get("Bitmap");
        ImageView img = findViewById(R.id.single_img_view);
        img.setImageBitmap(BitmapImg);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
