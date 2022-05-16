package com.providentitgroup.attendergcuf.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.providentitgroup.attendergcuf.QecRankingActivity;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.RatingActivity;
import com.providentitgroup.attendergcuf.models.AssignedCourseItem;
import com.providentitgroup.attendergcuf.models.EnrolledCourseItem;
import com.providentitgroup.attendergcuf.models.QecCourseItem;

import java.util.ArrayList;

public class AssignedCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<AssignedCourseItem> arrayList;
    private Context context;
    private ColorGenerator colorGenerator;

    public AssignedCourseAdapter(ArrayList<AssignedCourseItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.colorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public int getItemViewType(int position) {
         return R.layout.item_layout_enrolled_courses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignedCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_enrolled_courses,parent,false));
      
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AssignedCourseAdapter.AssignedCourseViewHolder){
            AssignedCourseItem assignCourse  = (AssignedCourseItem) arrayList.get(position);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .fontSize(35)
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(assignCourse.getCourseId(),colorGenerator.getColor(assignCourse.getCourseTitle()),10);
            ((AssignedCourseAdapter.AssignedCourseViewHolder) holder).courseCode.setImageDrawable(drawable);
            ((AssignedCourseAdapter.AssignedCourseViewHolder) holder).courseName.setText(assignCourse.getCourseTitle());
            ((AssignedCourseAdapter.AssignedCourseViewHolder) holder).courseClass.setText(assignCourse.getCourseClass());
            ((AssignedCourseAdapter.AssignedCourseViewHolder) holder).courseCreditHours.setText(assignCourse.getCourseCreditHours());

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class AssignedCourseViewHolder extends RecyclerView.ViewHolder{
        TextView courseName;
        ImageView courseCode;
        TextView courseCreditHours;
        TextView courseClass;

        public AssignedCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_title);
            courseCode = itemView.findViewById(R.id.course_code);
            courseCreditHours = itemView.findViewById(R.id.course_credit_hours);
            courseClass = itemView.findViewById(R.id.course_instructor);
        }
    }
}

