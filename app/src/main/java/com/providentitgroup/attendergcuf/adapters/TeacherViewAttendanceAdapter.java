package com.providentitgroup.attendergcuf.adapters;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.providentitgroup.attendergcuf.ChatsAcitivity;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.TVADetailsActivity;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.models.AttendanceItem;
import com.providentitgroup.attendergcuf.models.TeacherViewAttendanceItem;

import java.util.ArrayList;

public class TeacherViewAttendanceAdapter extends RecyclerView.Adapter<TeacherViewAttendanceAdapter.TeacherViewAttendanceItemViewHolder> {

    private ArrayList<TeacherViewAttendanceItem> teacherViewAttendanceItems;
    private Context context;
    int checker;

    public TeacherViewAttendanceAdapter(ArrayList<TeacherViewAttendanceItem> teacherViewAttendanceItems, Context context,int   i) {
        this.teacherViewAttendanceItems = teacherViewAttendanceItems;
        this.context = context;
        this.checker=i;
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
                if(checker==0){
                    Log.d("TAG",teacherViewAttendanceItems.get(position).getDetailsLink());
                    Intent intent = new Intent(context, TVADetailsActivity.class);
                    intent.putExtra("link",teacherViewAttendanceItems.get(position).getDetailsLink());
                    context.startActivity(intent);
                }else{
                    if(DataLocal.isExists(context,CNIC)){
                        Intent intent=new Intent(context, ChatsAcitivity.class);
                        intent.putExtra("userid",DataLocal.getString(context,CNIC));
                        intent.putExtra("groupid",teacherViewAttendanceItems.get(position).getCourseTitle()+"@"+teacherViewAttendanceItems.get(position).getCourseId());
                        intent.putExtra("groupname",teacherViewAttendanceItems.get(position).getCourseTitle());
                        context.startActivity(intent);
                    }
                    else{
                        Toast.makeText(context,"you are not properly login...",Toast.LENGTH_SHORT).show();
                    }
                }

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
    public static  String getLast6Characters(String word){
        if (word.length() == 6) {
            return word;
        } else if (word.length() > 6) {
            return word.substring(word.length() - 6);
        } else {
            // whatever is appropriate in this case
            return "N/A";
        }
    }

    public static String getStringWithoutLastCharacters(String str, int length) {
        if (str != null && str.length() > length) {
            str = str.substring(0, str.length() - length);
        }
        return str;
    }
}
