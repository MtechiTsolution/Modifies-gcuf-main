package com.providentitgroup.attendergcuf.adapters;

import android.content.Context;
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
import com.providentitgroup.attendergcuf.ResultActivity;
import com.providentitgroup.attendergcuf.TimeTableActivity;
import com.providentitgroup.attendergcuf.models.TimeTableItem;

import java.util.ArrayList;

import me.shaohui.bottomdialog.BottomDialog;

public class TimeTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList <Object> timeTableItems;
    private Context context;
    private ColorGenerator colorGenerator;

    public TimeTableAdapter(ArrayList<Object> timeTableItems, Context context) {
        this.timeTableItems = timeTableItems;
        this.context = context;
        colorGenerator = ColorGenerator.MATERIAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case R.layout.item_layout_timetable:
                return new TimeTableItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_timetable,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TimeTableItemViewHolder){
            final TimeTableItem timeTableItem = (TimeTableItem) timeTableItems.get(position);
            ((TimeTableItemViewHolder) holder).timeDuration.setText(timeTableItem.getTime());
            ((TimeTableItemViewHolder) holder).courseName.setText(timeTableItem.getCourseTitle());
            ((TimeTableItemViewHolder) holder).courseInstructor.setText(timeTableItem.getInstructorName());
            String [] arr = timeTableItem.getCourseTitle().split(" ");
            StringBuilder shortCutName= new StringBuilder();
            for(String str:arr){
                if(str.trim().length()>0){
                    shortCutName.append(str.charAt(0));
                }
            }
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .fontSize(56)
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(shortCutName.toString(),colorGenerator.getColor(timeTableItem.getCourseTitle()),10);
            ((TimeTableItemViewHolder) holder).timeTableImage.setImageDrawable(drawable);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new BottomDialog().setLayoutRes(R.layout.dialog_bottom_time_table_details_layout)
                            .setTag("time_table_dialog")
                            .setDimAmount(0.5f)
                            .setCancelOutside(true)
                            .setFragmentManager(((TimeTableActivity)context).getSupportFragmentManager())
                            .setViewListener(new BottomDialog.ViewListener() {
                                @Override
                                public void bindView(View v) {
                                    ((TextView)v.findViewById(R.id.time_table_room_no)).setText(timeTableItem.getRoomNumber());
                                    ((TextView)v.findViewById(R.id.time_table_building)).setText(timeTableItem.getBuilding());
                                    ((TextView)v.findViewById(R.id.time_table_subject_name)).setText(timeTableItem.getCourseTitle());
                                    ((TextView)v.findViewById(R.id.timing_duration_subject)).setText(timeTableItem.getTime());
                                }
                            })
                            .show();

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(timeTableItems.get(position) instanceof TimeTableItem) return R.layout.item_layout_timetable;
        return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return timeTableItems.size();
    }


    public class TimeTableItemViewHolder extends RecyclerView.ViewHolder{
        ImageView timeTableImage;
        TextView timeDuration;
        TextView courseName;
        TextView courseInstructor;

        public TimeTableItemViewHolder(@NonNull View itemView) {
            super(itemView);
            timeDuration = itemView.findViewById(R.id.time_duration);
            timeTableImage = itemView.findViewById(R.id.time_table_img);
            courseName  = itemView.findViewById(R.id.course_name);
            courseInstructor = itemView.findViewById(R.id.course_instructor);
        }
    }
}
