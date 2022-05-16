package com.providentitgroup.attendergcuf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.AssignedCourseAdapter;
import com.providentitgroup.attendergcuf.models.AssignedCourseItem;
import com.providentitgroup.attendergcuf.models.EnrolledCourseItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class fAssignedCoursesActivity extends AppCompatActivity {

    Context context;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<AssignedCourseItem> coursesList;
    Set<AssignedCourseItem> courseUniqueList;
    LinearLayoutManager linearLayoutManager;
    AssignedCourseAdapter assignedCourseAdapter;
    TabLayout tabLayout;
    ArrayList<String> sessionList;
    ArrayList<String> sessionLinks;
    boolean flag=true;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_assigned_courses);
        context =this;

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.assigned_courses_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        coursesList = new ArrayList<>();
        sessionLinks = new ArrayList<>();
        sessionList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        assignedCourseAdapter = new AssignedCourseAdapter(coursesList,context);
        recyclerView.setAdapter(assignedCourseAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        requestData(DataRequester.BASE_FACULTY_MODULE_URL+"attendance_viewAll.php");

        setToolBar();
        setTabLayout();

    }
    private void setTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    if(sessionLinks.size()>0 && !sessionLinks.get(tab.getPosition()).equals(""))
                    requestData(DataRequester.BASE_FACULTY_MODULE_URL+sessionLinks.get(tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void requestData( String url) {
        DataRequester.get(context, url, null, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                tabLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showGeneralError();
                if(statusCode==404){
                    startActivity(new Intent(fAssignedCoursesActivity.this,LoginActivity.class));
                    finishAffinity();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    AssignedCourseItem assignedCourseItem;
                    Document doc = Jsoup.parse(responseString);
                    Elements rows =doc.select(".table table table tr");
//                    Log.d("TAG",rows.toString());
                    coursesList.clear();
                    coursesList.ensureCapacity(rows.size()-1);
                    for(int i=0; i<rows.size();i++){
                        try {
                            assignedCourseItem = new AssignedCourseItem(rows.get(i).children());
                            if(!assignedCourseItem.searchInCourses(coursesList,coursesList.size()))
                                coursesList.add(assignedCourseItem);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG",rows.get(i).children().text());
                    }

                    if(rows.size()<=1){
                        Toasty.info(context, "No Courses were assigned to you in this Session", Toasty.LENGTH_LONG).show();

                    }
                    assignedCourseAdapter.notifyDataSetChanged();
                    Log.d("TAG",coursesList.size()+"");

                    Elements linkList = doc.select(".select-bar table table table tr td a");
                    String selectedTabName = doc.select(".select-bar table table table tr td font").text().trim().substring(1);
                    Log.d("TAG",linkList.text());
                    Log.d("TAG",linkList.size()+"");
                    Log.d("TAG",linkList.text());
                    sessionLinks.clear();
                    sessionList.clear();
                    sessionList.ensureCapacity(linkList.size()+1);
                    sessionLinks.ensureCapacity(linkList.size()+1);

                    tabLayout.removeAllTabs();
                    tabLayout.addTab(tabLayout.newTab().setText(selectedTabName));
                    sessionList.add(selectedTabName);
                    sessionLinks.add("");
                    for (int i = (linkList.size()-1); i >=0 ; i--) {
                        String semesterName = linkList.get(i).text();
                        semesterName = semesterName.substring(1).trim();
                        tabLayout.addTab(tabLayout.newTab().setText(semesterName));
                        String link = linkList.get(i).attr("onclick");
                        link = link.substring(10, link.length() - 4);
                        sessionList.add(semesterName);
                        sessionLinks.add(link);
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
