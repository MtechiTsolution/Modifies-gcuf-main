package com.providentitgroup.attendergcuf.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.models.Student;

import java.util.ArrayList;

public class StudentCardAdapter extends RecyclerView.Adapter<StudentCardAdapter.StudentViewHolder> {

    private ArrayList<Student> Students;
    private Context context;

    public StudentCardAdapter(ArrayList<Student> Students, Context context) {
        this.Students = Students;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_card_attendance, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.studentName.setText(Students.get(position).getName());
        holder.studentRollNumber.setText(Students.get(position).getRollNumber());
    }
    public ArrayList<Student> getItems() {
        return Students;
    }
    public void setItems(ArrayList<Student> items) {
        this.Students = items;
    }
    @Override
    public int getItemCount() {
        return Students.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView studentRollNumber;


        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            studentRollNumber = itemView.findViewById(R.id.roll_number);

        }
    }

}
