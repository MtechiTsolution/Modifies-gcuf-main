package com.providentitgroup.attendergcuf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.CardStackCallback;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.StudentCardAdapter;
import com.providentitgroup.attendergcuf.models.Student;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class MarkFinalAttendanceActivity extends AppCompatActivity {

    String link;
    String method;
    Context context;
    ArrayList<Student> studentList;
    RecyclerView studentRecyclerView;
    StudentCardAdapter studentCardAdapter;
    ProgressBar progressBar;
    Button submitBtn;
    TextView totalStudent;
    TextView presentStudent;
    TextView absentStudent;
    TextView title;
    TextView subTitle;
    String urlStruct;
    public static final List<Direction> CUSTOM = Arrays.asList(Direction.Left, Direction.Right, Direction.Top);

    String TAG="DUMMY";

    private CardStackLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_final_attendance);
        studentRecyclerView = findViewById(R.id.recycler_view);

        context=this;
        submitBtn = findViewById(R.id.submit_attendance_btn);
        totalStudent=findViewById(R.id.total_student);
        presentStudent = findViewById(R.id.present_student);
        absentStudent = findViewById(R.id.absent_students);
        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subtitle);
        studentRecyclerView = findViewById(R.id.recycler_view);
        progressBar=findViewById(R.id.progressBar);
        studentList = new ArrayList<>();
        studentRecyclerView.setVisibility(View.GONE);
        studentCardAdapter = new StudentCardAdapter(studentList,context);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    studentList.get(manager.getTopPosition()-1).setAttendance(1);
                }
                if (direction == Direction.Top){
                    studentList.get(manager.getTopPosition()-1).setAttendance(-1);
                }
                if (direction == Direction.Left){
                    studentList.get(manager.getTopPosition()-1).setAttendance(0);
                }
                if (manager.getTopPosition() == studentCardAdapter.getItemCount()){
                    progressBar.setVisibility(View.INVISIBLE);
                    studentRecyclerView.setVisibility(View.INVISIBLE);
                    totalStudent.setText(String.format("Total Student: %d",studentList.size() ));
                    presentStudent.setText(String.format("Present Student : %d", presentStudents()));
                    absentStudent.setText(String.format("Absent Student : %d", absentStudents()));
                    changeView(true);

                }
            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                Log.d(TAG, "onCardAppeared: " + position + ", nama: ");
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " );
            }


        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(60.0f);
        manager.setDirections(CUSTOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        studentRecyclerView.setLayoutManager(manager);
        studentRecyclerView.setAdapter(studentCardAdapter);
        studentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        changeView(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            link = bundle.getString("link");
            method = bundle.getString("method");
            Log.d("TAG",link);
            Log.d("TAG",method);

        }else{
            finish();
        }
        if(link==null || method==null || method.equals("") || link.equals("")){
            finish();
        }
        setUpSubmitAttendanceListener();
        requestData();


    }

    private void setUpSubmitAttendanceListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlStruct.equals("")){
                  Toasty.error(getApplicationContext(),"Error Occurred! Please Try Again",Toasty.LENGTH_SHORT).show();
                  finish();
                }else{
                    submitAttendance();
                }
            }
        });
    }

    private void submitAttendance() {
        String tempStr="&tempStr=";
        for(Student s : studentList){
            if(s.getAttendance()==1){
                tempStr =tempStr+"E_tv_"+s.getSecretNumber();
            }else if(s.getAttendance()==0){
                tempStr =tempStr+"E_fv_"+s.getSecretNumber();
            }else{
                tempStr =tempStr+"E_lv_"+s.getSecretNumber();
            }
        }
        urlStruct+=tempStr;
        Log.d("URL",urlStruct);
        DataRequester.get(context, DataRequester.BASE_FACULTY_MODULE_URL + urlStruct, null, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                changeView(false);
                studentRecyclerView.setVisibility(View.INVISIBLE);
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
                    startActivity(new Intent(MarkFinalAttendanceActivity.this,LoginActivity.class));
                    finishAffinity();
                }
                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    Toasty.success(getApplicationContext(), "Attendance Marked Successfully!", Toasty.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private int presentStudents() {
        int count=0;
        for(Student s:studentList){
           if(s.getAttendance()==1)count++;
        }
        return count;
    }

    private int absentStudents() {
        int count=0;
        for(Student s:studentList){
            if(s.getAttendance()==0)count++;
        }
        return count;
    }

    private void requestData() {
        DataRequester.get(context, DataRequester.BASE_FACULTY_MODULE_URL + link, null, new TextHttpResponseHandler() {
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
                    startActivity(new Intent(MarkFinalAttendanceActivity.this,LoginActivity.class));
                    finishAffinity();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode==200){
                    Document doc = Jsoup.parse(responseString);
                    Elements rows =doc.select("table table table tr");
                    urlStruct= doc.select("#button3").attr("onclick").trim();
                    urlStruct=urlStruct.substring(13,urlStruct.length());
                    urlStruct=urlStruct.substring(0,urlStruct.length()-2);
                    Log.d("URL",urlStruct);
                    urlStruct =urlStruct
                            +"&classVal="+doc.select("#classid").attr("value").trim()
                            +"&semasterVal="+doc.select("#Semester").attr("value").trim()
                            +"&courseVal="+doc.select("#subject").attr("value").trim()
                            +"&teacherVal="+doc.select("#tName").attr("value").trim()
                            +"&subSession="+doc.select("#subSection").attr("value").trim()
                            +"&tCreditHour="+doc.select("#tCreditHour").attr("value").trim()
                            +"&tCreditHour="+doc.select("#tch").attr("value").trim()
                            +"&lectNumber="+doc.select("#lectNumber").attr("value").trim()
                            +"&countValue="+doc.select("#countValue").attr("value").trim()
                            +"&LECT_DAY="+doc.select("#LECT_DAY").attr("value").trim()
                            +"&LECT_NO="+doc.select("#LECT_NO").attr("value").trim()
                            +"&recNo="+doc.select("#recNo").attr("value").trim()
                            +"&lectDate="+doc.select("#lectDate").attr("value").trim()
                            +"&lectTime="+doc.select("#lectTime").attr("value").trim()
                            +"&roomNumber="+doc.select("#roomNumber").attr("value").trim()
                            +"&LEC_TYPE="+doc.select("#LEC_TYPE").attr("value").trim()
                            +"&BLOCK_ID="+doc.select("#BLOCK_ID").attr("value").trim()
                            +"&FLOOR="+doc.select("#FLOOR").attr("value").trim()
                            +"&CCT_RECNO="+doc.select("#CCT_RECNO").attr("value").trim()
                            +"&TT_RECNO="+doc.select("#TT_RECNO").attr("value").trim();
                    Log.d("URL",urlStruct);
                    studentList.clear();
                    studentList.ensureCapacity(rows.size()-1);
                    for(int i=1; i<rows.size();i++){
                        try {
                            studentList.add(new Student(rows.get(i).children()));
                            Log.d("ROW",rows.get(i).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    studentList.remove(0);
                    processData();


                }
            }
        });
    }
    private void paginate() {
        ArrayList<Student> old = studentCardAdapter.getItems();
        ArrayList<Student> baru = studentList;
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        studentCardAdapter.setItems(baru);
        hasil.dispatchUpdatesTo(studentCardAdapter);
    }
    private void processData() {
        Log.d("TAG","DONE");
        switch (method) {
            case "manual":
                paginate();
                studentRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                changeView(false);

                break;
            case "present":
                Log.d("MOAZ",studentList.get(0).getSecretNumber());
                for (int i=0;i<studentList.size();i++) {
                    studentList.get(i).setAttendance(1);
                    Log.d("L"+(i+1), studentList.get(i).getSecretNumber());
                }
                changeView(true);
                progressBar.setVisibility(View.INVISIBLE);
                totalStudent.setText(String.format("Total Student: %d", studentList.size()));
                presentStudent.setText(String.format("Present Student : %d", studentList.size()));
                absentStudent.setText(String.format("Absent Student : %d", 0));
                break;
            case "absent":
                changeView(true);
                for (Student student : studentList) {
                    student.setAttendance(0);
                    Log.d("L2", student.getSecretNumber());
                }
                progressBar.setVisibility(View.INVISIBLE);
                totalStudent.setText(String.format("Total Student: %d", studentList.size()));
                presentStudent.setText(String.format("Present Student : %d", 0));
                absentStudent.setText(String.format("Absent Student : %d", studentList.size()));
                break;
            default:
                Log.d("TAG", "INVALID");
                break;
        }
    }

    private void changeView(boolean b) {
        if(!b){
            submitBtn.setVisibility(View.INVISIBLE);
            totalStudent.setVisibility(View.INVISIBLE);
            presentStudent.setVisibility(View.INVISIBLE);
            absentStudent.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            subTitle.setVisibility(View.INVISIBLE);
        }else{
            submitBtn.setVisibility(View.VISIBLE);
            totalStudent.setVisibility(View.VISIBLE);
            presentStudent.setVisibility(View.VISIBLE);
            absentStudent.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            subTitle.setVisibility(View.VISIBLE);
        }
    }
    private void showGeneralError() {
        Toasty.error(getApplicationContext(), "An Error Occurred! Please Check Your Internet Connection or Try Again", Toasty.LENGTH_LONG).show();
    }

}
