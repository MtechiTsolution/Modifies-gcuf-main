package com.providentitgroup.attendergcuf.adapters;

import android.content.Context;
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
import com.providentitgroup.attendergcuf.models.TVADetailsItem;
import com.providentitgroup.attendergcuf.models.TVADetailsItem;

import java.util.ArrayList;

public class TVADetailsAdapter extends RecyclerView.Adapter<TVADetailsAdapter.TVADetailsItemViewHolder> {

    private ArrayList<TVADetailsItem> TVADetailsItems;
    private Context context;

    public TVADetailsAdapter(ArrayList<TVADetailsItem> TVADetailsItems, Context context) {
        this.TVADetailsItems = TVADetailsItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TVADetailsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_tva_item, parent, false);
        return new TVADetailsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVADetailsItemViewHolder holder, final int position) {

       holder.studentName.setText(TVADetailsItems.get(position).getStudentName());
       holder.studentRollNumber.setText(TVADetailsItems.get(position).getStudentRollNumber());
       holder.studentGender.setText(TVADetailsItems.get(position).getStudentsGender());
        TextDrawable drawable;
        String percent =TVADetailsItems.get(position).getStudentsPercentage();
        int percentInt = Integer.parseInt(percent.substring(0,percent.length()-1).trim());
        if(percentInt>=90){
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(32) /* size in px */
                    .bold()
                    .endConfig()
                    .buildRoundRect(
                            percent
                            , Color.parseColor("#2ecc71"),10);
        }else if(percentInt>=75){
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(32) /* size in px */
                    .bold()
                    .endConfig()
                    .buildRoundRect(
                            percent
                            , Color.parseColor("#fa983a"),10);
        }else{
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(32) /* size in px */
                    .bold()
                    .endConfig()
                    .buildRoundRect(
                            percent
                            , Color.parseColor("#e74c3c"),10);
        }

        holder.studentPercentage.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return TVADetailsItems.size();
    }

    public class TVADetailsItemViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView studentRollNumber;
        ImageView studentPercentage;
        TextView studentGender;


        public TVADetailsItemViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            studentPercentage = itemView.findViewById(R.id.attendance_percentage);
            studentRollNumber = itemView.findViewById(R.id.student_roll_number);
            studentGender = itemView.findViewById(R.id.student_gender);

        }
    }
}
