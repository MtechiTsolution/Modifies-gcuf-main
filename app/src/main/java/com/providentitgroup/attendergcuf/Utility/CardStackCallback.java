package com.providentitgroup.attendergcuf.Utility;

import androidx.recyclerview.widget.DiffUtil;

import com.providentitgroup.attendergcuf.models.Student;

import java.util.ArrayList;

public class CardStackCallback extends DiffUtil.Callback {

    private ArrayList<Student> old, baru;

    public CardStackCallback(ArrayList<Student> old, ArrayList<Student> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getRollNumber() == baru.get(newItemPosition).getRollNumber();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}