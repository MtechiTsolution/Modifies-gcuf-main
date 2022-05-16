package com.providentitgroup.attendergcuf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.models.FeeItem;

import static android.view.View.GONE;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.nio.channels.InterruptedByTimeoutException;
import java.security.acl.Group;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    TextView timeTableBtn;
    TextView feeSectionBtn;
    TextView enrolledBtn;
    TextView qecRankingBtn,Chat;
    BottomNavigationView bottomNavigationView;
    Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context =this;
        timeTableBtn = findViewById(R.id.time_table_activity_btn);
        feeSectionBtn = findViewById(R.id.fee_btn);
        enrolledBtn = findViewById(R.id.enrolled_btn);
        qecRankingBtn = findViewById(R.id.qec_ranking_btn);
        Chat =findViewById(R.id.Group_chat);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        setFacultySettings();
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,ChatGroups.class);
                startActivity(intent);
            }
        });
        loadFeeData();

    }

    private void setFacultySettings() {
        if(DataLocal.getBoolean(this, LoginActivity.IS_FACULTY)) {
            feeSectionBtn.setVisibility(View.GONE);
//            enrolledBtn.setVisibility(View.GONE);
            qecRankingBtn.setVisibility(View.GONE);
            enrolledBtn.setText("Assigned Courses");
            setEnrolledBtn();
            setTimeTableBtn();
            setQecRankingBtn();
            setBottomNavigationListener();

        }else{
            setTimeTableBtn();
            setFeeBtn();
            setEnrolledBtn();
            setQecRankingBtn();
            setBottomNavigationListener();
        }
    }

    private void setBottomNavigationListener() {
        bottomNavigationView.setSelectedItemId(R.id.action_home);

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

                    case R.id.message:

                            startActivity(new Intent(context, ChatGroups.class));
                            finishAffinity();



                        break;

                    case R.id.action_result:
                        if(!DataLocal.getBoolean(getApplicationContext(), LoginActivity.IS_FACULTY)) {
                            startActivity(new Intent(context, ResultActivity.class));
                            finishAffinity();
                        }else{
                            startActivity(new Intent(context, MarkAttendanceViewActivity.class));
                            finishAffinity();}

                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(context, SettingsActivity.class));
                        finishAffinity();
                        return false;
                    case R.id.action_home:
                        return true;
                }
                return false;
            }
        });
    }
    private void setEnrolledBtn() {
        if(!DataLocal.getBoolean(this,LoginActivity.IS_FACULTY)) {
            enrolledBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, CoursesEnrolledActivity.class));
                }
            });
        }else{
            enrolledBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, fAssignedCoursesActivity.class));
                }
            });

        }
    }

    private void setFeeBtn() {
        feeSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,FeeDetailsActivity.class));
            }
        });
    }

    private void setTimeTableBtn() {
        timeTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,TimeTableActivity.class));
            }
        });
    }
    private void setQecRankingBtn() {
        if(DataLocal.getBoolean(this, LoginActivity.IS_FACULTY)) {
            qecRankingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this,TeacherViewAttendanceActivity.class));
                }
            });
        }else{
            qecRankingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this,QecRankingActivity.class));
                }
            });
        }

    }

    private void loadFeeData() {
        DataRequester.get(context, DataRequester.BASE_STUDENT_MODULE_URL + "feeDetail.php", null,
                new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {
                       // progressBar.setVisibility(View.VISIBLE);
                        //recyclerView.setVisibility(View.GONE);
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                       // progressBar.setVisibility(View.GONE);
                       // recyclerView.setVisibility(View.VISIBLE);
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

                                try {
                                    FeeItem feeItem=new FeeItem(rows.get(1).children());

                                    if(DataLocal.isExists(context,CNIC)){
                                    String name=feeItem.getStudentName();
                                    String rollnumber=feeItem.getRollNum();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Users");
                                     myRef.child(DataLocal.getString(context,CNIC)).child("name").setValue(name);
                                     myRef.child(DataLocal.getString(context,CNIC)).child("rollnumber").setValue(rollnumber);
                                       // Toasty.error(context,"User name"+ name+rollnumber,Toasty.LENGTH_LONG).show();

                                    }
                                } catch (Exception e) {
                                    Toasty.error(context,"User name error"+ e,Toasty.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                        }
                    }
                });
    }
}
