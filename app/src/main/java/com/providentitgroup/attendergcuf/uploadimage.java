package com.providentitgroup.attendergcuf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class uploadimage extends AppCompatActivity {
 String downloodurl;
    Bitmap bmp;
    FirebaseStorage fs;
    StorageReference sr;
    Button send;
    String userid,groupid,grpname,name,rollno,messageid;
    DatabaseReference myRef;
    Uri filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);

        send=findViewById(R.id.send_image);

        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        groupid=intent.getStringExtra("groupid");
        grpname=intent.getStringExtra("groupname");
        name=intent.getStringExtra("name");
        rollno=intent.getStringExtra("rollno");

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");
         bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.iv_sendImage);
        image.setImageBitmap(bmp);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Chats");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 messageid=  myRef.child(groupid).push().getKey();
                filepath=bitmapToUriConverter(bmp);
                if(filepath==null)
                {
                    Toast.makeText(uploadimage.this, "", Toast.LENGTH_SHORT).show();
                }
                else{uploadimage();}
            }
        });


    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
            Toast.makeText(uploadimage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public void uploadimage(){
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();
        final StorageReference stdf = sr.child("images/" +messageid );
        UploadTask uploadTask = stdf.putFile(filepath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return stdf.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    downloodurl = downloadUri.toString();
                    if (task.isSuccessful()) {


                        myRef.child(groupid).child(messageid).child("message").setValue(downloodurl);
                        myRef.child(groupid).child(messageid).child("userid").setValue(userid);
                        myRef.child(groupid).child(messageid).child("name").setValue(name);
                        myRef.child(groupid).child(messageid).child("rollnumber").setValue(rollno);
                        myRef.child(groupid).child(messageid).child("type").setValue("image");
                        myRef.child(groupid).child(messageid).child("time").setValue(ServerValue.TIMESTAMP);

                        onBackPressed();


                        Toast.makeText(uploadimage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(uploadimage.this, "error uploading image. " + task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });
    }
}