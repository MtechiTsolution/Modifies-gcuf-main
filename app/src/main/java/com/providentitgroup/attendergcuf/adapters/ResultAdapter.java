package com.providentitgroup.attendergcuf.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.ResultActivity;
import com.providentitgroup.attendergcuf.models.ResultItem;

import java.util.ArrayList;

import me.shaohui.bottomdialog.BottomDialog;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    ArrayList<ResultItem> resultItems;
    Context context;

    public ResultAdapter(ArrayList<ResultItem> resultItems, Context context) {
        this.resultItems = resultItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_layout, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, final int position) {
        holder.courseId.setText(resultItems.get(position).getCourseId());
        holder.creditHours.setText(resultItems.get(position).getCourseCreditHours());
        holder.courseName.setText(resultItems.get(position).getCourseName());
        holder.qualityPoints.setText(String.format("Quality Points : %s",resultItems.get(position).getQualityPoints()));
        holder.coursePercentage.setText(String.format("Percent : %s",resultItems.get(position).getPercentage()));
        holder.resultMessage.setText(
                String.format("Obtained %s out of %s marks",
                        resultItems.get(position).getMarksTotalAchieved(),
                        resultItems.get(position).getMarksTotalSubject()));

        TextDrawable.IBuilder builder = TextDrawable.builder().roundRect(5);
        TextDrawable drawable = null;
        String grade = resultItems.get(position).getGrade();
        if(grade.matches("A")){
            drawable = builder.build(grade, Color.parseColor("#1abc9c"));
        }else if(grade.matches("B")){
            drawable = builder.build(grade, Color.parseColor("#fa983a"));
        }else if(grade.matches("C")){
            drawable = builder.build(grade, Color.parseColor("#a4b0be"));
        }else if(grade.matches("D")){
            drawable = builder.build(grade, Color.parseColor("#95afc0"));
        }else if(grade.matches("F")){
            drawable = builder.build(grade, Color.parseColor("#e74c3c"));
        }else {
            drawable = builder.build(grade, Color.parseColor("#282828"));
        }
        holder.courseGrade.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomDialog().setLayoutRes(R.layout.dialog_bottom_result_details_layout)
                        .setTag("result_dialog")
                        .setDimAmount(0.5f)
                        .setCancelOutside(true)
                        .setFragmentManager(((ResultActivity)context).getSupportFragmentManager())
                        .setViewListener(new BottomDialog.ViewListener() {
                            @Override
                            public void bindView(View v) {
                                ((TextView)v.findViewById(R.id.subject_name_details)).setText(resultItems.get(position).getCourseName());
                                ((TextView)v.findViewById(R.id.sessional_marks_details)).setText(resultItems.get(position).getSessionalMarks());
                                ((TextView)v.findViewById(R.id.mid_term_marks_details)).setText(resultItems.get(position).getMidExamMarks());
                                ((TextView)v.findViewById(R.id.final_theory_marks_details)).setText(resultItems.get(position).getFinalTheoryMarks());
                                ((TextView)v.findViewById(R.id.final_practical_marks_details)).setText(resultItems.get(position).getFinalPracticalMarks());
                                ((TextView)v.findViewById(R.id.obtained_marks_details)).setText(
                                        String.format("%s / %s",
                                                resultItems.get(position).getMarksTotalAchieved(),
                                                resultItems.get(position).getMarksTotalSubject()));

                                ((TextView)v.findViewById(R.id.quality_points_details)).setText(resultItems.get(position).getQualityPoints());
                                ((TextView)v.findViewById(R.id.grade_details)).setText(resultItems.get(position).getGrade());
                                ((TextView)v.findViewById(R.id.percentage_details)).setText(resultItems.get(position).getPercentage());
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return resultItems.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView courseId;
        TextView creditHours;
        TextView courseName;
        TextView qualityPoints;
        TextView coursePercentage;
        ImageView courseGrade;
        TextView resultMessage;


        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            courseId = itemView.findViewById(R.id.course_id);
            qualityPoints = itemView.findViewById(R.id.quality_points);
            courseName = itemView.findViewById(R.id.course_name);
            coursePercentage = itemView.findViewById(R.id.percentage);
            courseGrade = itemView.findViewById(R.id.grade);
            creditHours = itemView.findViewById(R.id.credit_hours);
            resultMessage = itemView.findViewById(R.id.result_message);
        }
    }

}
