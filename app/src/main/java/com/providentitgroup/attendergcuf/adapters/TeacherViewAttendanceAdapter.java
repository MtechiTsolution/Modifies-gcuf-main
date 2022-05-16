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
import com.providentitgroup.attendergcuf.TVADetailsActivity;
import com.providentitgroup.attendergcuf.models.AttendanceItem;
import com.providentitgroup.attendergcuf.models.TeacherViewAttendanceItem;

import java.util.ArrayList;

public class TeacherViewAttendanceAdapter extends RecyclerView.Adapter<TeacherViewAttendanceAdapter.TeacherViewAttendanceItemViewHolder> {

    private ArrayList<TeacherViewAttendanceItem> teacherViewAttendanceItems;
    private Context context;

    public TeacherViewAttendanceAdapter(ArrayList<TeacherViewAttendanceItem> teacherViewAttendanceItems, Context context) {
        this.teacherViewAttendanceItems = teacherViewAttendanceItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherViewAttendanceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_teacher_view_layout, parent, false);
        return new TeacherViewAttendanceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewAttendanceItemViewHolder holder, final int position) {

        holder.courseId.setText(teacherViewAttendanceItems.get(position).getCourseId());
        holder.courseName.setText(teacherViewAttendanceItems.get(position).getCourseTitle());
        holder.classSection.setText(teacherViewAttendanceItems.get(position).getCourseSection());
        holder.creditHours.setText(teacherViewAttendanceItems.get(position).getCourseCreditHours());
        holder.enrolledStudents.setText(String.format("%s Students Enrolled", teacherViewAttendanceItems.get(position).getNumberOfStudents()));
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(
                        teacherViewAttendanceItems.get(position).getNumberOfLectures()
                        , ColorGenerator.MATERIAL.getRandomColor());

        holder.numOfLectures.setImageDrawable(drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG",teacherViewAttendanceItems.get(position).getDetailsLink());
                Intent intent = new Intent(context, TVADetailsActivity.class);
                intent.putExtra("link",teacherViewAttendanceItems.get(position).getDetailsLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherViewAttendanceItems.size();
    }

    public class TeacherViewAttendanceItemViewHolder extends RecyclerView.ViewHolder {
        TextView courseId;
        TextView courseName;
        TextView classSection;
        TextView enrolledStudents;
        ImageView numOfLectures;
        TextView creditHours;


        public TeacherViewAttendanceItemViewHolder(@NonNull View itemView) {
            super(itemView);

            courseId = itemView.findViewById(R.id.course_id);
            classSection = itemView.findViewById(R.id.class_name);
            courseName = itemView.findViewById(R.id.course_name);
            enrolledStudents = itemView.findViewById(R.id.students_enrolled);
            numOfLectures = itemView.findViewById(R.id.num_lectures);
            creditHours = itemView.findViewById(R.id.credit_hours);
        }
    }
}
