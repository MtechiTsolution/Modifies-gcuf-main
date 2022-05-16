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
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.EnrolledCoursesAdapter;
import com.providentitgroup.attendergcuf.models.EnrolledCourseItem;
import com.providentitgroup.attendergcuf.models.QecCourseItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class QecRankingActivity extends AppCompatActivity {

    Context context;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Object> coursesList;
    LinearLayoutManager linearLayoutManager;
    EnrolledCoursesAdapter enrolledCoursesAdapter;
    TabLayout tabLayout;
    ArrayList<String> semestersList;
    ArrayList<String> semestersLinks;
    boolean flag=true;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qec_ranking);
        context =this;

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.qec_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        coursesList = new ArrayList<>();
        semestersLinks = new ArrayList<>();
        semestersList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        enrolledCoursesAdapter = new EnrolledCoursesAdapter(coursesList,context);
        recyclerView.setAdapter(enrolledCoursesAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        requestData(DataRequester.BASE_STUDENT_MODULE_URL+"qec_cat.php");

        setToolBar();
        setTabLayout();


    }

    private void setTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(!flag){
                    requestData(DataRequester.BASE_STUDENT_MODULE_URL+semestersLinks.get(tab.getPosition()));
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

    private void setToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void requestData( String url) {
        tabLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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
                    startActivity(new Intent(QecRankingActivity.this,LoginActivity.class));
                    finishAffinity();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    Document doc = Jsoup.parse(responseString);
                    Elements rows =doc.select(".table1 table table tr");
                    Log.d("TAG",doc.text());
                    coursesList.clear();
                    coursesList.ensureCapacity(rows.size()-1);
                    for(int i=1; i<rows.size();i++){
                        try {
                            coursesList.add(new QecCourseItem(rows.get(i).children()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG",rows.text());

                    }
                    enrolledCoursesAdapter.notifyDataSetChanged();
                    Log.d("TAG",coursesList.size()+"");

                    Elements linkList = doc.select(".select-bar table tr td a");

                    if(flag) {
                        for (int i =linkList.size()-1; i >=0 ; i--) {
                            String semesterName = linkList.get(i).text();
                            semesterName = semesterName.substring(0, semesterName.length() - 1).trim();
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
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showGeneralError() {
        Toasty.error(context, "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }
}
