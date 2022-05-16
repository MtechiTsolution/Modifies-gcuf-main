package com.providentitgroup.attendergcuf;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.MarkAttendanceAdapter;
import com.providentitgroup.attendergcuf.adapters.TeacherViewAttendanceAdapter;
import com.providentitgroup.attendergcuf.models.MarkAttendanceItem;
import com.providentitgroup.attendergcuf.models.MarkAttendanceItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class MarkAttendanceViewActivity extends AppCompatActivity {

    Context context;
    ArrayList<MarkAttendanceItem> attendanceItems;
    RecyclerView attendanceItemsRecycler;
    MarkAttendanceAdapter attendanceAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance_view);
        if (!DataLocal.getBoolean(this, LoginActivity.IS_FACULTY)) {
            startActivity(new Intent(MarkAttendanceViewActivity.this, LoginActivity.class));
            finishAffinity();
        }
        context = this;

        attendanceItems = new ArrayList<>(0);
        attendanceAdapter = new MarkAttendanceAdapter(attendanceItems, context);

        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        attendanceItemsRecycler = findViewById(R.id.recycler_view);


        attendanceItemsRecycler.setAdapter(attendanceAdapter);
        attendanceItemsRecycler.setLayoutManager(new LinearLayoutManager(context));

        setBottomNavigationListener();

        requestAttendanceData(DataRequester.BASE_FACULTY_MODULE_URL + "attendance_viewAll.php");
    }
    private void setBottomNavigationListener() {
        bottomNavigationView.setSelectedItemId(R.id.action_result);
        bottomNavigationView.getMenu().findItem(R.id.action_result).setTitle("Mark Attendance");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_attendance:
                        if(!DataLocal.getBoolean(getApplicationContext(), LoginActivity.IS_FACULTY)) {
                            startActivity(new Intent(context, AttendanceActivity.class));
                            finishAffinity();
                        }else{
                            startActivity(new Intent(context, TeacherViewAttendanceActivity.class));
                            finishAffinity();
                        }
                        break;
                    case R.id.action_result:
                        return true;
                    case R.id.action_profile:
                        startActivity(new Intent(context, SettingsActivity.class));
                        finishAffinity();
                        return false;
                    case R.id.action_home:
                        startActivity(new Intent(context, HomeActivity.class));
                        return false;
                }
                return false;
            }
        });
    }
    public void requestAttendanceData(String url) {

        progressBar.setVisibility(View.VISIBLE);
        attendanceItemsRecycler.setVisibility(View.GONE);
        DataRequester.get(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onStart() {

                progressBar.setVisibility(View.VISIBLE);
                attendanceItemsRecycler.setVisibility(View.GONE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                attendanceItemsRecycler.setVisibility(View.VISIBLE);
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showGeneralError();
                if (statusCode == 404) {
                    startActivity(new Intent(MarkAttendanceViewActivity.this, LoginActivity.class));
                    finishAffinity();
                }
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    Document doc = Jsoup.parse(responseString);
                    Elements rows = doc.select(".table table table tr");
                    attendanceItems.clear();
                    attendanceItems.ensureCapacity(rows.size() - 1);
                    for (int i = 1; i < rows.size(); i++) {
                        try {
                            MarkAttendanceItem MarkAttendanceItem = new MarkAttendanceItem(rows.get(i).children());
                            attendanceItems.add(MarkAttendanceItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    attendanceAdapter.notifyDataSetChanged();
                    if (rows.size() <= 1) {
                        Toasty.info(context, "No Courses were assigned to you in this Session", Toasty.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    private void showGeneralError() {
        Toasty.error(context, "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }
}
