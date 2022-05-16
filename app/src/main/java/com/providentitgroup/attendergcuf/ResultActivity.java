package com.providentitgroup.attendergcuf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.ResultAdapter;
import com.providentitgroup.attendergcuf.models.ResultItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class ResultActivity extends AppCompatActivity {

    Context context;
    ArrayList<ResultItem> resultItems;
    RecyclerView resultItemsRecycler;
    ResultAdapter resultAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    LinearLayout resultNotFound;
    TabLayout tabLayout;
    ArrayList<String> semestersList;
    ArrayList<String> semestersLinks;
    TextView semesterGPA;
    TextView semesterCGPA;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        context = this;

        resultItems = new ArrayList<>(0);
        resultAdapter = new ResultAdapter(resultItems, context);

        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        resultItemsRecycler = findViewById(R.id.result_recycler_view);
        resultNotFound = findViewById(R.id.not_found_result);
        semesterCGPA = findViewById(R.id.semester_cgpa);
        semesterGPA = findViewById(R.id.semester_gpa);
        semesterGPA.setVisibility(View.INVISIBLE);
        semesterCGPA.setVisibility(View.INVISIBLE);

        resultItemsRecycler.setAdapter(resultAdapter);
        resultItemsRecycler.setLayoutManager(new LinearLayoutManager(context));

        tabLayout = findViewById(R.id.tabs);
        semestersLinks = new ArrayList<>();
        semestersList = new ArrayList<>();

        setBottomNavigationListener();
        setTabLayout();
        requestResultData(DataRequester.BASE_STUDENT_MODULE_URL +
                "academicsDetail.php");
    }
    private void setTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(!flag){
                    requestResultData(DataRequester.BASE_STUDENT_MODULE_URL+semestersLinks.get(tab.getPosition()));
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
        bottomNavigationView.setSelectedItemId(R.id.action_result);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_attendance:
                        startActivity(new Intent(context, AttendanceActivity.class));
                        finishAffinity();
                        break;
                    case R.id.action_result:
                        return  true;
                    case R.id.action_profile:
                        startActivity(new Intent(context, SettingsActivity.class));
                        finishAffinity();
                        return false;
                    case R.id.action_home:
                        startActivity(new Intent(context, HomeActivity.class));
                        finishAffinity();
                        break;
                    case R.id.message:
                        startActivity(new Intent(context, ChatGroups.class));
                        finishAffinity();
                        break;
                }
                return false;
            }
        });
    }

    public void requestResultData(String url) {
        tabLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        resultItemsRecycler.setVisibility(View.GONE);
        resultNotFound.setVisibility(View.GONE);
        DataRequester.get(context, url, null, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                semesterCGPA.setVisibility(View.INVISIBLE);
                semesterGPA.setVisibility(View.INVISIBLE);
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
                    startActivity(new Intent(ResultActivity.this,LoginActivity.class));
                    finishAffinity();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    Document doc = Jsoup.parse(responseString);
                    Elements rows = doc.select(".table table table tr");

                    resultItems.clear();

                    if(rows.size()>4){
                        resultItems.ensureCapacity(rows.size() - 7);
                        for (int i = 3; i < rows.size()-4; i++) {
                            try {
                                resultItems.add(new ResultItem(rows.get(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        semesterGPA.setText(String.format("SEMESTER  GPA : %s", rows.get(rows.size()-3).children().get(1).text().trim()));
                        semesterCGPA.setText(String.format("SEMESTER CGPA : %s", rows.get(rows.size()-2).children().get(1).text().trim()));
                        semesterGPA.setVisibility(View.VISIBLE);
                        semesterCGPA.setVisibility(View.VISIBLE);
                    }else{
                        semesterGPA.setVisibility(View.INVISIBLE);
                        semesterCGPA.setVisibility(View.INVISIBLE);
                        resultItems.ensureCapacity(0);
                        //No Result Found Handle in some other way
                        resultNotFound.setVisibility(View.VISIBLE);
                    }

                    resultAdapter.notifyDataSetChanged();

                    Elements linkList = doc.select(".select-bar table tr td a");

                    if(flag) {
                        for (int i = linkList.size()-1; i >= 0; i--) {
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
                    resultItemsRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showGeneralError() {
        Toasty.error(context, "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }
}