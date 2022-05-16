package com.providentitgroup.attendergcuf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.SelectMarkAttendanceTypeActivity;
import com.providentitgroup.attendergcuf.TVADetailsActivity;
import com.providentitgroup.attendergcuf.models.MarkAttendanceItem;

import java.util.ArrayList;

public class MarkAttendanceAdapter extends RecyclerView.Adapter<MarkAttendanceAdapter.MarkAttendanceItemViewHolder> {

    private ArrayList<MarkAttendanceItem> MarkAttendanceItems;
    private Context context;

    public MarkAttendanceAdapter(ArrayList<MarkAttendanceItem> MarkAttendanceItems, Context context) {
        this.MarkAttendanceItems = MarkAttendanceItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MarkAttendanceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_mark_attendance, parent, false);
        return new MarkAttendanceItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MarkAttendanceItemViewHolder holder, final int position) {

        holder.classSection.setText(MarkAttendanceItems.get(position).getCourseSection());
        holder.courseTiming.setText(MarkAttendanceItems.get(position).getCourseTiming());
        holder.courseName.setText(MarkAttendanceItems.get(position).getCourseTitle());
        TextDrawable drawable;
        if(MarkAttendanceItems.get(position).getCourseLink().equals("#") || MarkAttendanceItems.get(position).getCourseLink().equals("")){
            drawable= TextDrawable.builder()
                    .beginConfig()
                    .fontSize(32)
                    .bold()
                    .endConfig()
                    .buildRect(
                            MarkAttendanceItems.get(position).getCourseDay().substring(0,3).toUpperCase()
                            , Color.parseColor("#aaa"));
            holder.courseDay.setImageDrawable(drawable);


        }else{
            drawable= TextDrawable.builder()
                    .beginConfig()
                    .fontSize(32)
                    .bold()
                    .endConfig()
                    .buildRect(
                            MarkAttendanceItems.get(position).getCourseDay().substring(0,3).toUpperCase()
                            , ColorGenerator.MATERIAL.getRandomColor());
            holder.courseDay.setImageDrawable(drawable);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG",MarkAttendanceItems.get(position).getCourseLink());
                    Intent intent = new Intent(context, SelectMarkAttendanceTypeActivity.class);
                    intent.putExtra("link",MarkAttendanceItems.get(position).getCourseLink());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return MarkAttendanceItems.size();
    }

    public class MarkAttendanceItemViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView classSection;
        TextView courseTiming;
        ImageView courseDay;


        public MarkAttendanceItemViewHolder(@NonNull View itemView) {
            super(itemView);

            classSection = itemView.findViewById(R.id.course_section);
            courseName = itemView.findViewById(R.id.course_name);
            courseTiming = itemView.findViewById(R.id.time_duration);
            courseDay = itemView.findViewById(R.id.time_table_img);
        }
    }
}

