package com.providentitgroup.attendergcuf.adapters;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.providentitgroup.attendergcuf.ChatsAcitivity;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.models.AttendanceItem;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private ArrayList<AttendanceItem> attendanceItems;
    private Context context;

    public AttendanceAdapter(ArrayList<AttendanceItem> attendanceItems, Context context) {
        this.attendanceItems = attendanceItems;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_layout, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String attendancePercent = getStringWithoutLastCharacters(attendanceItems.get(position).getAttendancePercentage(),1).trim();
        holder.courseName.setText(getStringWithoutLastCharacters(attendanceItems.get(position).getCourseName(),6));
        holder.courseId.setText(attendanceItems.get(position).getCourseId());
        holder.courseInstructor.setText(attendanceItems.get(position).getCourseInstructor());
        holder.attendanceMessage.setText(attendanceItems.get(position).getNumOfPresents() +" presents out of "+attendanceItems.get(position).getNumOfLectures());
        TextDrawable drawable;
        if(Integer.valueOf(attendancePercent)>=90){
            drawable = TextDrawable.builder()
                    .buildRect(
                            attendancePercent
                            , Color.parseColor("#DAE5D5"));
        }else if(Integer.valueOf(attendancePercent)>=75){
            drawable = TextDrawable.builder()
                    .buildRect(
                            attendancePercent
                            , Color.parseColor("#DAE5D5"));
        }else{
            drawable = TextDrawable.builder()
                    .buildRect(
                            attendancePercent
                            , Color.parseColor("#DAE5D5"));
        }

        holder.attendancePercentage.setImageDrawable(drawable);
        holder.creditHours.setText(getLast6Characters(attendanceItems.get(position).getCourseName()));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
////            public void onClick(View v) {
////                if(DataLocal.isExists(context,CNIC)){
////                    Intent intent=new Intent(context, ChatsAcitivity.class);
////                    intent.putExtra("userid",DataLocal.getString(context,CNIC));
////                    intent.putExtra("groupid",getStringWithoutLastCharacters(attendanceItems.get(position).getCourseName(),6)+"@"+attendanceItems.get(position).getCourseId());
////                    intent.putExtra("groupname",getStringWithoutLastCharacters(attendanceItems.get(position).getCourseName(),6));
////                    context.startActivity(intent);
////                }
////                else{
////                    Toast.makeText(context,"you are not properly login...",Toast.LENGTH_SHORT).show();
////                }
////
////            }
//        });
    }

    @Override
    public int getItemCount() {
        return attendanceItems.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView courseId;
        TextView courseName;
        TextView courseInstructor;
        TextView attendanceMessage;
        ImageView attendancePercentage;
        TextView creditHours;


        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            courseId = itemView.findViewById(R.id.course_id);
            courseInstructor = itemView.findViewById(R.id.quality_points);
            courseName = itemView.findViewById(R.id.course_name);
            attendanceMessage = itemView.findViewById(R.id.result_message);
            attendancePercentage = itemView.findViewById(R.id.grade);
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
