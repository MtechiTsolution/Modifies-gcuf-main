package com.providentitgroup.attendergcuf.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.providentitgroup.attendergcuf.FeeDetailsActivity;
import com.providentitgroup.attendergcuf.R;
import com.providentitgroup.attendergcuf.models.FeeItem;

import java.util.ArrayList;

import me.shaohui.bottomdialog.BottomDialog;

public class FeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList <Object> feeItems;
    private Context context;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    public FeeAdapter(ArrayList<Object> feeItems, Context context) {
        this.feeItems = feeItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case R.layout.item_fee_layout:
                return new FeeItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fee_layout,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FeeItemViewHolder){
            final FeeItem feeItem = (FeeItem) feeItems.get(position);
            ((FeeItemViewHolder) holder).indexNumber.setText(String.format("#%s",feeItem.getId()));
            ((FeeItemViewHolder) holder).voucherNumber.setText(String.format("Voucher No. %s",feeItem.getVoucherNum()));
            if(feeItem.getPaidAmount()==null || feeItem.getPaidAmount().isEmpty()){
                ((FeeItemViewHolder) holder).backgroundContainer.setBackgroundResource(R.color.colorRed);

            }
            ((FeeItemViewHolder) holder).semester.setText(String.format("%s",feeItem.getSemester()));
            ((FeeItemViewHolder) holder).feeAmount.setText(String.format("Fee Amount : RS : %s",feeItem.getFeeAmount()));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new BottomDialog().setLayoutRes(R.layout.dialog_bottom_fee_details_layout)
                            .setTag("time_table_dialog")
                            .setDimAmount(0.5f)
                            .setCancelOutside(true)
                            .setFragmentManager(((FeeDetailsActivity)context).getSupportFragmentManager())
                            .setViewListener(new BottomDialog.ViewListener() {
                                @Override
                                public void bindView(View v) {
                                    ((TextView)v.findViewById(R.id.voucher_num)).setText(String.format("Voucher No. %s",feeItem.getVoucherNum()));
                                    if(feeItem.getPaidAmount()==null || feeItem.getPaidAmount().isEmpty()){
                                        ((TextView)v.findViewById(R.id.paid_amount)).setText(String.format("RS 0.00"));
                                    }else{
                                        ((TextView)v.findViewById(R.id.paid_amount)).setText(String.format("RS %s",feeItem.getPaidAmount()));
                                    }
                                    ((TextView)v.findViewById(R.id.fee_amount)).setText(String.format("RS %s",feeItem.getFeeAmount()));
                                    ((TextView)v.findViewById(R.id.semester)).setText(feeItem.getSemester());
                                    ((TextView)v.findViewById(R.id.entry_date)).setText(feeItem.getEntryDate());
                                    if(feeItem.getBankName()==null || feeItem.getBankName().isEmpty()){
                                        ((TextView)v.findViewById(R.id.bank_name)).setText(String.format("N / A"));
                                    }
                                    else{
                                        ((TextView)v.findViewById(R.id.bank_name)).setText(feeItem.getBankName());
                                    }
                                    ((TextView)v.findViewById(R.id.program_title)).setText(feeItem.getProgramTitle());
                                    ((TextView)v.findViewById(R.id.student_name)).setText(feeItem.getStudentName());
                                    ((TextView)v.findViewById(R.id.father_name)).setText(feeItem.getFatherName());
                                }
                            })
                            .show();

                }

                private LayoutInflater getLayoutInflater() {
                    return null;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(feeItems.get(position) instanceof FeeItem) return R.layout.item_fee_layout;
        return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return feeItems.size();
    }


    public class FeeItemViewHolder extends RecyclerView.ViewHolder{
        TextView indexNumber;
        TextView voucherNumber;
        TextView semester;
        TextView feeAmount;
        RelativeLayout backgroundContainer;

        public FeeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            indexNumber = itemView.findViewById(R.id.index_number);
            voucherNumber = itemView.findViewById(R.id.voucer_number);
            semester  = itemView.findViewById(R.id.semester);
            feeAmount = itemView.findViewById(R.id.fee_amount);
            backgroundContainer = itemView.findViewById(R.id.background_container);
        }
    }
}
