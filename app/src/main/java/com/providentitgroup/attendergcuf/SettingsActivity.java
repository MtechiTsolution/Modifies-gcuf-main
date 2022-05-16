package com.providentitgroup.attendergcuf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.CONSTANTS;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import me.shaohui.bottomdialog.BottomDialog;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class SettingsActivity extends AppCompatActivity {


    Switch autoLoginSwitch;
    Button changePassBtn;
    Button logoutBtn;
    Context context;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        autoLoginSwitch = findViewById(R.id.auto_login_switch);
        changePassBtn = findViewById(R.id.change_pass_btn);
        logoutBtn = findViewById(R.id.log_out_btn);
        bottomNavigationView = findViewById(R.id.bottomNavigationLayout);

        context = this;

        setUpAutoLoginListener();
        setUpChangePassListener();
        setUpLogoutListener();
        setBottomNavigationListener();

    }

    private void setBottomNavigationListener() {
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
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
                    case R.id.action_result:
                        if(!DataLocal.getBoolean(getApplicationContext(), LoginActivity.IS_FACULTY)) {
                            startActivity(new Intent(context, ResultActivity.class));
                            finishAffinity();
                        }else{
                            startActivity(new Intent(context, MarkAttendanceViewActivity.class));
                            finishAffinity();
                        }
                        return  false;
                    case R.id.action_profile:
                        return true;
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

    private void setUpAutoLoginListener() {
        autoLoginSwitch.setChecked(DataLocal.getBoolean(context,CONSTANTS.AUTO_LOGIN));
        autoLoginSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DataLocal.saveBoolean(context, CONSTANTS.AUTO_LOGIN, b);
            }
        });
    }

    private void setUpChangePassListener() {
        final BottomDialog bottomDialog
                = new BottomDialog();
        bottomDialog.setFragmentManager(getSupportFragmentManager());
        bottomDialog.setLayoutRes(R.layout.layout_change_password_bottom_sheet);
        bottomDialog.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                final ExtendedEditText currentPass = v.findViewById(R.id.old_pass_text);
                final ExtendedEditText newPass = v.findViewById(R.id.new_pass_text);
                final ExtendedEditText newConfirmPass = v.findViewById(R.id.new_confirm_pass_text);
                final TextFieldBoxes currentPassBox = v.findViewById(R.id.old_pass);
                final TextFieldBoxes newPassBox = v.findViewById(R.id.new_pass);
                final TextFieldBoxes newConfirmPassBox = v.findViewById(R.id.new_confirm_pass);
                final Button chPasswordButton = v.findViewById(R.id.change_pass_btn);
                final ProgressBar progressBar = v.findViewById(R.id.ProgressBar);

                chPasswordButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cPass = currentPass.getText().toString().trim();
                        String nPass = newPass.getText().toString().trim();
                        String ncPass = newConfirmPass.getText().toString().trim();
                        if (cPass.length() <= 0) {
                            currentPassBox.setError("Please Enter Current Password!", true);
                            return;
                        }
                        if (nPass.length() < 8) {
                            newPassBox.setError("Password must be 8 character long.", true);
                            return;
                        }
                        if (!ncPass.matches(nPass)) {
                            newConfirmPassBox.setError("Password does not match with each other", true);
                            return;
                        }

                        RequestParams params = new RequestParams();
                        params.add("oldPass", cPass);
                        params.add("newPass", nPass);
                        String BASE_URL;
                        if(DataLocal.getBoolean(getApplicationContext(), LoginActivity.IS_FACULTY)) {
                            BASE_URL = DataRequester.BASE_FACULTY_MODULE_URL;
                        }else{
                            BASE_URL=DataRequester.BASE_STUDENT_MODULE_URL;
                        }
                        DataRequester.get(context, BASE_URL + "changePass.php", params, new TextHttpResponseHandler() {
                            @Override
                            public void onStart() {
                                progressBar.setVisibility(View.VISIBLE);
                                currentPass.setEnabled(false);
                                newPass.setEnabled(false);
                                newConfirmPass.setEnabled(false);
                                chPasswordButton.setEnabled(false);
                                super.onStart();
                            }

                            @Override
                            public void onFinish() {
                                progressBar.setVisibility(View.GONE);
                                currentPass.setEnabled(true);
                                newPass.setEnabled(true);
                                newConfirmPass.setEnabled(true);
                                chPasswordButton.setEnabled(true);
                                super.onFinish();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Toasty.info(context, "An Error Occurred! Please Check Your Internet Connection", Toasty.LENGTH_LONG).show();
                                bottomDialog.dismiss();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                if (statusCode == 200) {
                                    Document doc = Jsoup.parse(responseString);
                                    Elements rows = doc.select("table tr");
                                    Log.d("TAG",rows.text());
                                    if (rows.size() > 1) {
                                        currentPassBox.setError("Wrong Password! Password Change Failed", true);
                                    } else {
                                        Toasty.success(context, "Password Changed Successfully!", Toasty.LENGTH_LONG).show();
                                        currentPass.setText("");
                                        newPass.setText("");
                                        newConfirmPass.setText("");
                                        bottomDialog.dismiss();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
        bottomDialog.setTag("change_password");
        bottomDialog.setDimAmount(0.5f);
        bottomDialog.setCancelOutside(true);

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.show();
            }
        });

    }

    private void setUpLogoutListener() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_power)
                        .setTitle("Are You Sure?")
                        .setMessage("Press Logout if you want to continue or Cancel the request.")
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                DataLocal.destroyData(context);
                                Toasty.success(getApplicationContext(), "Logged Out Successfully!", Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });
    }
}
