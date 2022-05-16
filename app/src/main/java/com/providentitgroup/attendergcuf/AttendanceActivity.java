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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.AttendanceAdapter;
import com.providentitgroup.attendergcuf.models.AttendanceItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;


public class AttendanceActivity extends AppCompatActivity {

    Context context;
    ArrayList<AttendanceItem> attendanceItems;
    RecyclerView attendanceItemsRecycler;
    AttendanceAdapter attendanceAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    ArrayList<String> semestersList;
    ArrayList<String> semestersLinks;
    boolean flag=true;
    TextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);



        context = this;

        attendanceItems = new ArrayList<>(0);
        attendanceAdapter = new AttendanceAdapter(attendanceItems,context);

        progressBar= findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        attendanceItemsRecycler = findViewById(R.id.attendance_recycler_view);


        attendanceItemsRecycler.setAdapter(attendanceAdapter);
        attendanceItemsRecycler.setLayoutManager(new LinearLayoutManager(context));


        tabLayout = findViewById(R.id.tabs);
        semestersLinks = new ArrayList<>();
        semestersList = new ArrayList<>();

        setBottomNavigationListener();
        setTabLayout();
        requestAttendanceData(DataRequester.BASE_STUDENT_MODULE_URL + "attendance.php");
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
        bottomNavigationView.setSelectedItemId(R.id.action_attendance);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_attendance:
                        return true;
                    case R.id.message:
                        startActivity(new Intent(context, ChatGroups.class));
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
                showGeneralError();
                if(statusCode==404){
                    startActivity(new Intent(AttendanceActivity.this,LoginActivity.class));
                    finishAffinity();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){

//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, responseString);
//                    sendIntent.setType("text/plain");
//                    startActivity(sendIntent);

                    Document doc =Jsoup.parse(responseString);
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
                    attendanceAdapter.notifyDataSetChanged();
                      
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

}
