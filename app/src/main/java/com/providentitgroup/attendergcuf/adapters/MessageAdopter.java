package com.providentitgroup.attendergcuf.adapters;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.squareup.picasso.Picasso;

import java.util.Collections;
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
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {



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
                        Picasso.get().load(snapshot.child("message").getValue().toString()).into(holder.imageView);


                        holder.name.setText(snapshot.child("name").getValue().toString());
                        holder.rollnumber.setText(snapshot.child("rollnumber").getValue().toString());
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


       TextView name,rollnumber,messgae;
       ImageView imageView;
       LinearLayout msgcard,c1;
//       CardView c1;

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
