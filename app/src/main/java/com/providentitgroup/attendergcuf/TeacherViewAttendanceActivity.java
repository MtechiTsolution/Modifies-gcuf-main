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
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.AttendanceAdapter;
import com.providentitgroup.attendergcuf.adapters.TeacherViewAttendanceAdapter;
import com.providentitgroup.attendergcuf.models.AttendanceItem;
import com.providentitgroup.attendergcuf.models.TeacherViewAttendanceItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class TeacherViewAttendanceActivity extends AppCompatActivity {

    Context context;
    ArrayList<TeacherViewAttendanceItem> attendanceItems;
    RecyclerView attendanceItemsRecycler;
    TeacherViewAttendanceAdapter attendanceAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    ArrayList<String> sessionsList;
    ArrayList<String> sessionsLinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_attendance);

        if (!DataLocal.getBoolean(this, LoginActivity.IS_FACULTY)) {
            startActivity(new Intent(TeacherViewAttendanceActivity.this, LoginActivity.class));
            finishAffinity();
        }
        context = this;

        attendanceItems = new ArrayList<>(0);
        attendanceAdapter = new TeacherViewAttendanceAdapter(attendanceItems, context);

        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        attendanceItemsRecycler = findViewById(R.id.attendance_recycler_view);


        attendanceItemsRecycler.setAdapter(attendanceAdapter);
        attendanceItemsRecycler.setLayoutManager(new LinearLayoutManager(context));


        tabLayout = findViewById(R.id.tabs);
        sessionsLinks = new ArrayList<>();
        sessionsList = new ArrayList<>();

        setBottomNavigationListener();
        setTabLayout();
        requestAttendanceData(DataRequester.BASE_FACULTY_MODULE_URL + "attendance_viewAll.php");

    }

    private void setTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(sessionsLinks.size()>0 && !sessionsLinks.get(tab.getPosition()).equals(""))
                    requestAttendanceData(DataRequester.BASE_FACULTY_MODULE_URL + sessionsLinks.get(tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setBottomNavigationListener() {
        bottomNavigationView.setSelectedItemId(R.id.action_attendance);
        bottomNavigationView.getMenu().findItem(R.id.action_attendance).setTitle("Show Attendance");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_attendance:
                        return true;
                    case R.id.action_result:
                        if(!DataLocal.getBoolean(getApplicationContext(), LoginActivity.IS_FACULTY)) {
                            startActivity(new Intent(context, ResultActivity.class));
                            finishAffinity();
                        }else{
                            startActivity(new Intent(context, MarkAttendanceViewActivity.class));
                            finishAffinity();
                        }
                        break;
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
        tabLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        attendanceItemsRecycler.setVisibility(View.GONE);
        DataRequester.get(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                tabLayout.setVisibility(View.GONE);
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
                    startActivity(new Intent(TeacherViewAttendanceActivity.this, LoginActivity.class));
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
                            TeacherViewAttendanceItem teacherViewAttendanceItem = new TeacherViewAttendanceItem(rows.get(i).children());
                            if (!teacherViewAttendanceItem.searchInCourses(attendanceItems, attendanceItems.size()))
                                attendanceItems.add(teacherViewAttendanceItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    attendanceAdapter.notifyDataSetChanged();
                    if (rows.size() <= 1) {
                        Toasty.info(context, "No Courses were assigned to you in this Session", Toasty.LENGTH_LONG).show();
                    }
                    Elements linkList = doc.select(".select-bar table table table tr td a");
                    String selectedTabName = doc.select(".select-bar table table table tr td font").text().trim().substring(1);
                    Log.d("TAG", linkList.text());
                    Log.d("TAG", linkList.size() + "");
                    Log.d("TAG", linkList.text());
                    sessionsLinks.clear();
                    sessionsList.clear();
                    sessionsList.ensureCapacity(linkList.size() + 1);
                    sessionsLinks.ensureCapacity(linkList.size() + 1);

                    tabLayout.removeAllTabs();
                    tabLayout.addTab(tabLayout.newTab().setText(selectedTabName));
                    sessionsList.add(selectedTabName);
                    sessionsLinks.add("");
                    for (int i = (linkList.size() - 1); i >= 0; i--) {
                        String semesterName = linkList.get(i).text();
                        semesterName = semesterName.substring(1).trim();
                        tabLayout.addTab(tabLayout.newTab().setText(semesterName));
                        String link = linkList.get(i).attr("onclick");
                        link = link.substring(10, link.length() - 4);
                        sessionsList.add(semesterName);
                        sessionsLinks.add(link);
                    }
                    tabLayout.getTabAt(0).select();
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showGeneralError() {
        Toasty.error(context, "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }
}
