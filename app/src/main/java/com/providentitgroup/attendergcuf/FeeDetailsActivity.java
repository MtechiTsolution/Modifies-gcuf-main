package com.providentitgroup.attendergcuf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.FeeAdapter;
import com.providentitgroup.attendergcuf.models.FeeItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class FeeDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Object> feeItemsList;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    FeeAdapter feeAdapter;
    Context context;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_details);


        context =this;
        feeItemsList = new ArrayList<>();
        recyclerView = findViewById(R.id.fee_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        progressBar = findViewById(R.id.progressBar);
        feeAdapter  = new FeeAdapter(feeItemsList,context);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setAdapter(feeAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(FeeDetailsActivity.this,2));

        loadFeeData();
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

    private void loadFeeData() {
        DataRequester.get(context, DataRequester.BASE_STUDENT_MODULE_URL + "feeDetail.php", null,
                new TextHttpResponseHandler() {
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

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    Document doc = Jsoup.parse(responseString);
                    Log.d("check2",responseString);
                    Elements rows = doc.select("table table tr");
                    for(int i=1;i<rows.size();i++){
                        try {
                            feeItemsList.add(new FeeItem(rows.get(i).children()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    feeAdapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                }
            }
        });
    }
}
