package com.providentitgroup.attendergcuf.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.providentitgroup.attendergcuf.QecRankingActivity;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.RatingActivity;
import com.providentitgroup.attendergcuf.models.EnrolledCourseItem;
import com.providentitgroup.attendergcuf.models.QecCourseItem;

import java.util.ArrayList;

public class EnrolledCoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> arrayList;
    private Context context;
    private ColorGenerator colorGenerator;

    public EnrolledCoursesAdapter(ArrayList<Object> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.colorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public int getItemViewType(int position) {
        if(arrayList.get(position) instanceof EnrolledCourseItem){
            return R.layout.item_layout_enrolled_courses;
        }else if(arrayList.get(position) instanceof QecCourseItem){
            return R.layout.item_layout_qec_courses;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case R.layout.item_layout_enrolled_courses:
                return new EnrolledCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_enrolled_courses,parent,false));
            case R.layout.item_layout_qec_courses:
                return new QecCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_qec_courses,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EnrolledCourseViewHolder){
            EnrolledCourseItem enrolledCourse  = (EnrolledCourseItem) arrayList.get(position);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .fontSize(30)
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(enrolledCourse.getCourseId(),colorGenerator.getColor(enrolledCourse.getCourseTitle()),10);
            ((EnrolledCourseViewHolder) holder).courseCode.setImageDrawable(drawable);
            ((EnrolledCourseViewHolder) holder).courseName.setText(enrolledCourse.getCourseTitle());
            ((EnrolledCourseViewHolder) holder).courseInstructor.setText(enrolledCourse.getCourseInstructor());
            ((EnrolledCourseViewHolder) holder).courseCreditHours.setText(enrolledCourse.getCourseCreditHours());

        }else if (holder instanceof QecCourseViewHolder){
            final QecCourseItem qecCourseItem  = (QecCourseItem) arrayList.get(position);


            if(qecCourseItem.getRatingLink()!=null){
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .fontSize(32)
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(qecCourseItem.getCourseId(),((QecRankingActivity)context).getResources().getColor(R.color.colorGreen),10);
                ((QecCourseViewHolder) holder).courseCode.setImageDrawable(drawable);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, RatingActivity.class);
                        intent.putExtra("qecLink",qecCourseItem.getRatingLink());
                        intent.putExtra("teacherName", qecCourseItem.getCourseInstructor());
                        intent.putExtra("courseName",qecCourseItem.getCourseTitle());
                        context.startActivity(intent);
                        ((QecRankingActivity)context).finish();
                    }
                });

            }else{
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .fontSize(32)
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(qecCourseItem.getCourseId(),((QecRankingActivity)context).getResources().getColor(R.color.colorLightBlack),10);
                ((QecCourseViewHolder) holder).courseCode.setImageDrawable(drawable);
                holder.itemView.setClickable(false);
                holder.itemView.setFocusable(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.itemView.setForeground(null);
                }

            }
            ((QecCourseViewHolder) holder).courseName.setText(qecCourseItem.getCourseTitle());
            ((QecCourseViewHolder) holder).courseInstructor.setText(qecCourseItem.getCourseInstructor());
            ((QecCourseViewHolder) holder).courseCreditHours.setText(qecCourseItem.getCourseCreditHours());

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class EnrolledCourseViewHolder extends RecyclerView.ViewHolder{
        TextView courseName;
        ImageView courseCode;
        TextView courseCreditHours;
        TextView courseInstructor;

        public EnrolledCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_title);
            courseCode = itemView.findViewById(R.id.course_code);
            courseCreditHours = itemView.findViewById(R.id.course_credit_hours);
            courseInstructor = itemView.findViewById(R.id.course_instructor);
        }
    }
    public class QecCourseViewHolder extends RecyclerView.ViewHolder{
        TextView courseName;
        ImageView courseCode;
        TextView courseCreditHours;
        TextView courseInstructor;

        public QecCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_title);
            courseCode = itemView.findViewById(R.id.course_code);
            courseCreditHours = itemView.findViewById(R.id.course_credit_hours);
            courseInstructor = itemView.findViewById(R.id.course_instructor);
        }
    }
}
