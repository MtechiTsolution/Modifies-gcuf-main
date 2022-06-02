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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.AttendanceAdapter;
import com.providentitgroup.attendergcuf.adapters.ChatAdapter;
import com.providentitgroup.attendergcuf.adapters.TeacherViewAttendanceAdapter;
import com.providentitgroup.attendergcuf.models.AttendanceItem;
import com.providentitgroup.attendergcuf.models.TeacherViewAttendanceItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class ChatGroups extends AppCompatActivity {

    Context context;
    ArrayList<AttendanceItem> attendanceItems;
    RecyclerView attendanceItemsRecycler;
    ChatAdapter chatAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    ArrayList<String> semestersList;
    ArrayList<String> semestersLinks;
    boolean flag=true,check=false;
    public static final String IS_FACULTY = "is_faculty";
    TextView txv;

    ArrayList<TeacherViewAttendanceItem> fattendanceItems;

    TeacherViewAttendanceAdapter attendanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_groups);

        context = this;

        check=DataLocal.getBoolean(context,IS_FACULTY);



        progressBar= findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        attendanceItemsRecycler = findViewById(R.id.attendance_recycler_view);


        attendanceItemsRecycler.setLayoutManager(new LinearLayoutManager(context));


        tabLayout = findViewById(R.id.tabs);
        semestersLinks = new ArrayList<>();
        semestersList = new ArrayList<>();

        setBottomNavigationListener();
        setTabLayout();
        Toast.makeText(context, ""+check, Toast.LENGTH_SHORT).show();
        if(check){

            fattendanceItems = new ArrayList<>(0);
            attendanceAdapter = new TeacherViewAttendanceAdapter(fattendanceItems, context,1);
            attendanceItemsRecycler.setAdapter(attendanceAdapter);
            fac_requestAttendanceData(DataRequester.BASE_FACULTY_MODULE_URL + "attendance_viewAll.php");
        }else{
            attendanceItems = new ArrayList<>(0);
            chatAdapter = new ChatAdapter(attendanceItems,context);
            attendanceItemsRecycler.setAdapter(chatAdapter);
            requestAttendanceData(DataRequester.BASE_STUDENT_MODULE_URL + "attendance.php");
        }



    }

    private void setTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(!flag){
                    requestAttendanceData(DataRequester.BASE_STUDENT_MODULE_URL+semestersLinks.get(tab.getPosition()));
                }else{
                    flag=false;
                }

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
        bottomNavigationView.setSelectedItemId(R.id.message);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.message:
                        return  true;
                    case R.id.action_attendance:
                        startActivity(new Intent(context, AttendanceActivity.class));
                        finishAffinity();
                        break;
                    case R.id.action_result:
                        startActivity(new Intent(context, ResultActivity.class));
                        finishAffinity();
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

    public void requestAttendanceData( String url) {
        tabLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        attendanceItemsRecycler.setVisibility(View.GONE);
        DataRequester.get(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onStart() {

                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, "fail"+flag, Toast.LENGTH_SHORT).show();
                showGeneralError();
                if(statusCode==404){
                    startActivity(new Intent(ChatGroups.this,LoginActivity.class));
                    finishAffinity();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
//
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, responseString);
//                    sendIntent.setType("text/plain");
//                    startActivity(sendIntent);

                    Document doc = Jsoup.parse(responseString);
                    Elements rows =doc.select(".table table table tr");
                    attendanceItems.clear();
                    attendanceItems.ensureCapacity(rows.size()-2);
                    for(int i=2; i<rows.size();i++){
                        try {
                            attendanceItems.add(new AttendanceItem(rows.get(i).children()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    chatAdapter.notifyDataSetChanged();

                    Elements linkList = doc.select(".select-bar table tr td a");

                    if(flag) {
                        for (int i = linkList.size()-1; i >=0 ; i--) {
                            String semesterName = linkList.get(i).text();
                            semesterName = semesterName.substring(0,semesterName.length()-1).trim();
                            tabLayout.addTab(tabLayout.newTab().setText(semesterName));

                            String link = linkList.get(i).attr("onclick");
                            link = link.substring(10, link.length() - 4);
                            semestersList.add(semesterName);
                            semestersLinks.add(link);
                        }
                        tabLayout.getTabAt(0).select();
                    }
                    tabLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    attendanceItemsRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showGeneralError() {
        Toasty.error(context, "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }


    public void fac_requestAttendanceData(String url) {
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
                    startActivity(new Intent(ChatGroups.this, LoginActivity.class));
                    finishAffinity();
                }
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    Document doc = Jsoup.parse(responseString);
                    Elements rows = doc.select(".table table table tr");
                    fattendanceItems.clear();
                    fattendanceItems.ensureCapacity(rows.size() - 1);
                    for (int i = 1; i < rows.size(); i++) {
                        try {
                            TeacherViewAttendanceItem teacherViewAttendanceItem = new TeacherViewAttendanceItem(rows.get(i).children());
                            if (!teacherViewAttendanceItem.searchInCourses(fattendanceItems, fattendanceItems.size()))
                                fattendanceItems.add(teacherViewAttendanceItem);
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

}