package com.providentitgroup.attendergcuf.adapters;

import static android.content.ContentValues.TAG;
import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.Showimage;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class MessageAdopter extends RecyclerView.Adapter<MessageAdopter.MessageViewHolder> {

    List<String> messagelist;
    String groupid;
    Context context;
    DatabaseReference myRef;
   public MessageAdopter(List<String> messagelist,Context context,String groupid){
        this.messagelist=messagelist;
        this.context=context;
        this.groupid=groupid;


    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.third_user, parent, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Chats");
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, @SuppressLint("RecyclerView") int position) {


        myRef.child(groupid).child(messagelist.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("type").exists()&&snapshot.child("message").exists()&&snapshot.child("name").exists()
                        &&snapshot.child("rollnumber").exists()&&snapshot.child("userid").exists()){

                    String userid=snapshot.child("userid").getValue().toString();
                    if(DataLocal.isExists(context,CNIC)){
                    if(userid.equals(DataLocal.getString(context,CNIC))){
                        holder.msgcard.setGravity(Gravity.RIGHT);
                        holder.c1.setBackgroundColor(Color.DKGRAY);
                        holder.name.setTextColor(Color.WHITE);
                        holder.rollnumber.setTextColor(Color.WHITE);
                        holder.messgae.setTextColor(Color.WHITE);


//

                    }}

                    if(snapshot.child("type").getValue().toString().equals("text")){
                        holder.imageView.setVisibility(View.GONE);
                        holder.messgae.setVisibility(View.VISIBLE);
                        holder.messgae.setText(snapshot.child("message").getValue().toString());

                        holder.name.setText(snapshot.child("name").getValue().toString());
                        holder.rollnumber.setText(snapshot.child("rollnumber").getValue().toString());


                    }else{
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.messgae.setVisibility(View.GONE);
                        Picasso.get().load(snapshot.child("message").getValue().toString())
                                .into(holder.imageView);
                        holder.name.setText(snapshot.child("name").getValue().toString());
                        holder.rollnumber.setText(snapshot.child("rollnumber").getValue().toString());
                        holder.imageView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                try{
                                    Intent intent = new Intent(context, Showimage.class);
                                     intent.putExtra("Imageshow",snapshot.child("message").getValue().toString());
                                    ((Activity)context).startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }



                            }
                        });

                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    class  MessageViewHolder extends RecyclerView.ViewHolder{

        private Animator currentAnimator;
        private int shortAnimationDuration;
       TextView name,rollnumber,messgae;
       ImageView imageView;
       LinearLayout msgcard,c1;
       String imageurl;
        boolean isImageFitToScreen;
//       CardView c1;
        MediaPlayer mp;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nmus);
            rollnumber=itemView.findViewById(R.id.number);
            messgae=itemView.findViewById(R.id.message);
            imageView=itemView.findViewById(R.id.msgimg);
            msgcard=itemView.findViewById(R.id.msgcrd);
            c1=itemView.findViewById(R.id.card1);

        }
    }
}
