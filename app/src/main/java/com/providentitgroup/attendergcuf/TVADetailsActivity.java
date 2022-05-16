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

import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.TVADetailsAdapter;
import com.providentitgroup.attendergcuf.models.TVADetailsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class TVADetailsActivity extends AppCompatActivity {

    Context context;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<TVADetailsItem> attendanceItemsList;
    LinearLayoutManager linearLayoutManager;
    TVADetailsAdapter tvaDetailsAdapter;
    String link;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_v_a_details);

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            link = intentExtras.getString("link");
            Log.d("TAG",link);
        }else{
            finish();
            Log.d("TAG", "SHUT DOWN ACTIVITY 1");
        }
        context =this;
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.attendance_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        attendanceItemsList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        tvaDetailsAdapter = new TVADetailsAdapter(attendanceItemsList,context);
        recyclerView.setAdapter(tvaDetailsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        DataRequester.get(context, DataRequester.BASE_FACULTY_MODULE_URL+link, null, new TextHttpResponseHandler() {

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
                Log.d("TAG","ERROR HERE");
                if(statusCode==404){
                    finish();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    requestData(DataRequester.BASE_FACULTY_URL+"viewAllAtt.php");
                }
            }
        });

        setToolBar();
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
                Log.d("TAG","ERROR THERE");

                if(statusCode==404){
                    finish();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    Document doc = Jsoup.parse(responseString);
                    Elements rows =doc.select("table table table tr");
                    attendanceItemsList.clear();
                    attendanceItemsList.ensureCapacity(rows.size()-2);
                    for(int i=2; i<rows.size();i++){
                        try {
                            attendanceItemsList.add(new TVADetailsItem(rows.get(i).children()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG",rows.get(i).children().text());
                    }

                    tvaDetailsAdapter.notifyDataSetChanged();

                }
            }
        });
    }
    private void showGeneralError() {
        Toasty.error(context, "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }
}
