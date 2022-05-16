package com.providentitgroup.attendergcuf;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.providentitgroup.attendergcuf.adapters.TimeTableAdapter;

import java.util.ArrayList;

public class TimeTableFragment extends Fragment {
    private ArrayList <Object> arrayList;

    public TimeTableFragment() {
    }
    public TimeTableFragment(ArrayList<Object> arrayList) {
        this.arrayList =arrayList;
        Log.d("TAGq",arrayList.size()+"");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table_layout,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.time_table_recycler_view);
        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(arrayList, view.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setAdapter(timeTableAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        timeTableAdapter.notifyDataSetChanged();

        return view;

    }
}
