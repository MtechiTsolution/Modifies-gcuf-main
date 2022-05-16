package com.providentitgroup.attendergcuf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import es.dmoral.toasty.Toasty;

public class SelectMarkAttendanceTypeActivity extends AppCompatActivity {

    Button markAllPresent;
    Button markAllAbsent;
    Button markAllManually;
    String link;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mark_attendance_type);

        markAllAbsent = findViewById(R.id.mark_student_absent);
        markAllPresent = findViewById(R.id.mark_present_all_btn);
        markAllManually = findViewById(R.id.mark_student_individually_btn);
        toolbar = findViewById(R.id.toolbar);
        setToolBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            link = bundle.getString("link");
            Log.d("TAG",link);

        }else{
            finish();
        }
        if(link==null || link.equals("")){
            finish();
        }
        markAllManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMarkAttendanceTypeActivity.this,MarkFinalAttendanceActivity.class);
                intent.putExtra("link",link);
                intent.putExtra("method","manual");
                startActivity(intent);
                finish();
            }
        });
        markAllPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMarkAttendanceTypeActivity.this,MarkFinalAttendanceActivity.class);
                intent.putExtra("link",link);
                intent.putExtra("method","present");
                startActivity(intent);
                finish();
            }
        });
        markAllAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMarkAttendanceTypeActivity.this,MarkFinalAttendanceActivity.class);
                intent.putExtra("link",link);
                intent.putExtra("method","absent");
                startActivity(intent);
                finish();
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

}
