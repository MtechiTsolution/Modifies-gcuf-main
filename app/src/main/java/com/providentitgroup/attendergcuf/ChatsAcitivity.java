package com.providentitgroup.attendergcuf;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.adapters.AssignedCourseAdapter;
import com.providentitgroup.attendergcuf.adapters.AttendanceAdapter;
import com.providentitgroup.attendergcuf.adapters.MessageAdopter;
import com.providentitgroup.attendergcuf.models.AttendanceItem;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatsAcitivity extends AppCompatActivity {

    Context context;
    String userid,groupid;
    ImageButton sendImage;
    TextView groupname;
    EditText messageedit;
    MediaPlayer mp;
    ImageView send;
    RecyclerView messagelist;
    List<String> messagelistitem;
    MessageAdopter messageAdopter;
    ProgressBar progressBar;
    String name="",rollnumber="",grpname;
    Uri uri;
    private  static final int PICK_IMAGE=1;
    private boolean receiever_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_acitivity);
        messageedit=findViewById(R.id.msgedittext);
        groupname=findViewById(R.id.groupname);
        progressBar=findViewById(R.id.progress_bar);
        messagelist=findViewById(R.id.chatlist);
        sendImage=findViewById(R.id.uploadimagebtn);

        mp = MediaPlayer.create(this, R.raw.send);

        send=findViewById(R.id.sendbtn);
        context =ChatsAcitivity.this;
        Intent intent=getIntent();
        if(intent!=null){
            userid=intent.getStringExtra("userid");
            groupid=intent.getStringExtra("groupid");
             grpname=intent.getStringExtra("groupname");
            groupname.setText(grpname);
            groupid= groupid.replace(" ","");
            //Toast.makeText(ChatsAcitivity.this, ""+groupid.replace(" ",""), Toast.LENGTH_SHORT).show();

        }        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chats");
        DatabaseReference myRef2 = database.getReference("Users");
        messagelistitem=new ArrayList<>();
        Collections.reverse(messagelistitem);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        messagelist.setLayoutManager(linearLayoutManager);
        messageAdopter = new MessageAdopter(messagelistitem,context,groupid);
        messagelist.setAdapter(messageAdopter);
        messagelist.smoothScrollToPosition(messageAdopter.getItemCount());

        myRef2.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("name")){
                    name=snapshot.child("name").getValue().toString();
                }
                if(snapshot.hasChild("rollnumber")){
                    rollnumber=snapshot.child("rollnumber").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child(groupid).orderByChild("time").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                messagelistitem.add(snapshot.getKey());
                messagelist.smoothScrollToPosition(messagelistitem.size()-1);
                messageAdopter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setType("image/*");
                intent1.setAction(intent1.ACTION_GET_CONTENT);
                startActivityForResult(intent1,PICK_IMAGE);

                mp.start();

            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!messageedit.getText().toString().isEmpty()){
                  String messageid=  myRef.child(groupid).push().getKey();
                  myRef.child(groupid).child(messageid).child("message").setValue(messageedit.getText().toString());
                  myRef.child(groupid).child(messageid).child("userid").setValue(userid);
                  myRef.child(groupid).child(messageid).child("name").setValue(name);
                  myRef.child(groupid).child(messageid).child("rollnumber").setValue(rollnumber);
                  myRef.child(groupid).child(messageid).child("type").setValue("text");
                  myRef.child(groupid).child(messageid).child("time").setValue(ServerValue.TIMESTAMP);
                    mp.start();
                  messageedit.setText("");

                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==1&&resultCode==RESULT_OK
                &&data!=null&&data.getData()!=null) {

            uri = data.getData();
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(this, uploadimage.class);
                intent.putExtra("picture", byteArray);
                intent.putExtra("userid",DataLocal.getString(context,CNIC));
                intent.putExtra("groupid",groupid);
                intent.putExtra("groupname",grpname);
                intent.putExtra("name",name);
                intent.putExtra("rollno",rollnumber);

                startActivity(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ChatsAcitivity.this, "Something went wrong\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            }



        }else {
            Toast.makeText(ChatsAcitivity.this, "No File selected", Toast.LENGTH_SHORT).show();
        }

    }
}