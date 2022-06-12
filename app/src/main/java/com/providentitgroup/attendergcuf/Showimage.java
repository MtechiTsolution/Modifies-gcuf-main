package com.providentitgroup.attendergcuf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Showimage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);

        Intent intent= getIntent();
        ImageView imageView = (ImageView) findViewById(R.id.Image_show);
        Toast.makeText(this, "Ma a gia ", Toast.LENGTH_SHORT).show();
       String strImage= String.valueOf(intent.getStringExtra("Imageshow"));

        Log.d("LOG_TAG" ,strImage);

        Picasso.get().load(strImage)
                .into(imageView);

    }
}