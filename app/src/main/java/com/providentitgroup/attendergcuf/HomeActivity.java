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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.models.FeeItem;
import com.providentitgroup.attendergcuf.models.TimeTableItem;

import static android.view.View.GONE;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;
import static com.providentitgroup.attendergcuf.LoginActivity.IS_FACULTY;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.nio.channels.InterruptedByTimeoutException;
import java.security.acl.Group;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    TextView timeTableBtn;
    TextView feeSectionBtn;
    TextView enrolledBtn;
    TextView qecRankingBtn, Chat;
    LinearLayout layoutfee,layoutqec;
    BottomNavigationView bottomNavigationView;
    Context context;
    private ArrayList[] timeTableItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        timeTableBtn = findViewById(R.id.time_table_activity_btn);
        feeSectionBtn = findViewById(R.id.fee_btn);
        layoutfee = findViewById(R.id.fee_layout);
        layoutqec = findViewById(R.id.qec_lyout);
        enrolledBtn = findViewById(R.id.enrolled_btn);
        qecRankingBtn = findViewById(R.id.qec_ranking_btn);
        Chat = findViewById(R.id.Group_chat);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);
        setFacultySettings();
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChatGroups.class);
                startActivity(intent);
            }
        });
        loadFeeData();

    }

    private void setFacultySettings() {
        if (DataLocal.getBoolean(this, IS_FACULTY)) {
            feeSectionBtn.setVisibility(View.GONE);
            layoutfee.setVisibility(View.GONE);
            layoutqec.setVisibility(View.GONE);
//            enrolledBtn.setVisibility(View.GONE);
            qecRankingBtn.setVisibility(View.GONE);
            enrolledBtn.setText("Assigned Courses");
            setEnrolledBtn();
            setTimeTableBtn();
            setQecRankingBtn();
            setBottomNavigationListener();

        } else {
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
                        if (!DataLocal.getBoolean(getApplicationContext(), IS_FACULTY)) {
                            startActivity(new Intent(context, AttendanceActivity.class));
                            finishAffinity();
                        } else {
                            startActivity(new Intent(context, TeacherViewAttendanceActivity.class));
                            finishAffinity();
                        }

                        break;

                    case R.id.message:

                        startActivity(new Intent(context, ChatGroups.class));
                        finishAffinity();


                        break;

                    case R.id.action_result:
                        if (!DataLocal.getBoolean(getApplicationContext(), IS_FACULTY)) {
                            startActivity(new Intent(context, ResultActivity.class));
                            finishAffinity();
                        } else {
                            startActivity(new Intent(context, MarkAttendanceViewActivity.class));
                            finishAffinity();
                        }

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
        if (!DataLocal.getBoolean(this, IS_FACULTY)) {
            enrolledBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, CoursesEnrolledActivity.class));
                }
            });
        } else {
            enrolledBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, fAssignedCoursesActivity.class));
                }
            });

        }
    }

    private void setFeeBtn() {
       if (!DataLocal.getBoolean(this, IS_FACULTY))
        feeSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, FeeDetailsActivity.class));
            }
        });
    }

    private void setTimeTableBtn() {
        timeTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TimeTableActivity.class));
            }
        });
    }

    private void setQecRankingBtn() {
        if (DataLocal.getBoolean(this, IS_FACULTY)) {
            qecRankingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, TeacherViewAttendanceActivity.class));
                }
            });
        } else {
            qecRankingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, QecRankingActivity.class));
                }
            });
        }

    }

    private void loadFeeData() {
        boolean check = DataLocal.getBoolean(context, IS_FACULTY);
        if (check) {
            loadTimeTableData();
            return;
        }
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
                        if (statusCode == 200) {
                            Document doc = Jsoup.parse(responseString);
                            Log.d("check2", responseString);
                            Elements rows = doc.select("table table tr");

                            try {
                                FeeItem feeItem = new FeeItem(rows.get(1).children());

                                if (DataLocal.isExists(context, CNIC)) {
                                    String name = feeItem.getStudentName();
                                    String rollnumber = feeItem.getRollNum();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Users");
                                    myRef.child(DataLocal.getString(context, CNIC)).child("name").setValue(name);
                                    myRef.child(DataLocal.getString(context, CNIC)).child("rollnumber").setValue(rollnumber);
                                    // Toasty.error(context,"User name"+ name+rollnumber,Toasty.LENGTH_LONG).show();

                                }
                            } catch (Exception e) {
                                Toasty.error(context, "User name error" + e, Toasty.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }


    private void loadTimeTableData() {
        timeTableItems = new ArrayList[7];
        String URL;
        if (!DataLocal.getBoolean(this, LoginActivity.IS_FACULTY)) {
            URL = DataRequester.BASE_STUDENT_MODULE_URL + "timeTable.php";
        } else {
            URL = DataRequester.BASE_FACULTY_MODULE_URL + "timeTable.php";
        }
        DataRequester.get(context, URL, null, new TextHttpResponseHandler() {
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

                Log.d("TAG", responseString);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    Document doc = Jsoup.parse(responseString);
                    Elements rows = doc.select(".table table tbody tr td");


                    Log.d("TAG1", rows.toString());
                    for (int i = 0; i < timeTableItems.length; i++) {
                        timeTableItems[i] = new ArrayList<>();
                    }
                    for (int i = 0, j = 0; i < rows.size(); i++) {
                        if (rows.get(i).text().length() > 10) {
                            try {
                                timeTableItems[j - 1].add(new TimeTableItem(rows.get(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        j++;
                        if (j == 8) {
                            j = 0;
                        }
                    }

                    ArrayList<Object> arrayList=timeTableItems[1];

                    final TimeTableItem timeTableItem = (TimeTableItem) arrayList.get(0);
                    if (DataLocal.isExists(context, CNIC)) {
                        String name = timeTableItem.getInstructorName();
                        String rollnumber = "Professor";
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        myRef.child(DataLocal.getString(context, CNIC)).child("name").setValue(name);
                        myRef.child(DataLocal.getString(context, CNIC)).child("rollnumber").setValue(rollnumber);
                        // Toasty.error(context,"User name"+ name+rollnumber,Toasty.LENGTH_LONG).show();

                    }



                }
            }
        });
    }
}