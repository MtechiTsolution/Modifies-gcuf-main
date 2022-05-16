package com.providentitgroup.attendergcuf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.CONSTANTS;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class LoginActivity extends AppCompatActivity {

    TextFieldBoxes cnicCover;
    TextFieldBoxes passCover;
    ExtendedEditText cnicInput;
    ExtendedEditText passInput;
    RadioGroup loginTypeSelector;
    RadioButton facultyRadioBtn;
    RadioButton studentRadioBtn;
    Switch keepMeLoggedInSwitch;
    Switch rememberMeSwitch;
    Button loginBtn;
    ProgressBar progressBar;
    boolean isKeepLoggedIn;
    boolean isRememberMe;
    Context context;
    public static final String PASSWORD ="password";
    public static final String CNIC ="cnic";
    public static final String AUTO_LOGIN ="auto_login";
    public static final String IS_FACULTY = "is_faculty";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cnicCover = findViewById(R.id.cnic_input);
        passCover = findViewById(R.id.pass_input);
        cnicInput = findViewById(R.id.cnic_text);
        passInput = findViewById(R.id.pass_text);
        loginTypeSelector = findViewById(R.id.login_type_selection);
        facultyRadioBtn = findViewById(R.id.faculty_btn);
        studentRadioBtn = findViewById(R.id.student_btn);
        keepMeLoggedInSwitch = findViewById(R.id.keep_me_logged_in_switch);
        rememberMeSwitch = findViewById(R.id.remember_me_switch);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);

        context = this;

        loadUserData();
        autoLogin();
        configListeners();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cnic = cnicInput.getText().toString().trim();
                String pass = passInput.getText().toString().trim();
                boolean isFaculty = loginTypeSelector.getCheckedRadioButtonId() == R.id.faculty_btn;
                isKeepLoggedIn = keepMeLoggedInSwitch.isChecked();
                isRememberMe = rememberMeSwitch.isChecked();

                if(cnic.length()!=15){
                    cnicCover.setError("CNIC Not Valid. It should be 13 digits long with dashes.",true);
                    return;
                }
                if(pass.length()<1){
                    passCover.setError("Password Cannot Be Empty.",true);
                    return;
                }
                DataLocal.saveBoolean(context,"isRememberMe",isRememberMe);

                if(isRememberMe){
                    DataLocal.saveString(context,CNIC,cnic);
                    DataLocal.saveString(context,PASSWORD,pass);
                    DataLocal.saveBoolean(context,IS_FACULTY,isFaculty);
                    DataLocal.saveBoolean(context,AUTO_LOGIN,isKeepLoggedIn);
                }else{
                    DataLocal.destroyData(context);
                }
//                if(isFaculty){
//                    Toasty.error(context,"Faculty Login is Not Available Yet!", Toasty.LENGTH_LONG).show();
//                    return;
//                }
                sendLoginRequest(cnic, pass, isFaculty);
            }
        });

    }

    private void configListeners() {
        rememberMeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DataLocal.saveBoolean(context,CONSTANTS.REMEMBER_ME,b);
                if(!b){
                    keepMeLoggedInSwitch.setChecked(false);
                }
            }
        });
        keepMeLoggedInSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rememberMeSwitch.setChecked(true);
                    DataLocal.saveBoolean(context,CONSTANTS.AUTO_LOGIN,b);
                }else{
                    DataLocal.saveBoolean(context,CONSTANTS.AUTO_LOGIN,b);
                }
            }
        });
    }

    private void autoLogin() {
        if(DataLocal.getBoolean(context, CONSTANTS.AUTO_LOGIN)){
            keepMeLoggedInSwitch.setChecked(true);
            rememberMeSwitch.setChecked(true);

            if(DataLocal.isExists(context,CNIC)){
                Toasty.info(context,"Auto Login in Progress. Please Wait! ", Toasty.LENGTH_LONG).show();
                sendLoginRequest(
                        DataLocal.getString(context,CNIC),
                        DataLocal.getString(context,PASSWORD),
                        DataLocal.getBoolean(context,IS_FACULTY));
            }

        }
    }

    private void loadUserData() {
        if(DataLocal.isExists(context,CNIC)){
            rememberMeSwitch.setChecked(true);
            cnicInput.setText(DataLocal.getString(context,CNIC));
            passInput.setText(DataLocal.getString(context,PASSWORD));
            rememberMeSwitch.setChecked(DataLocal.getBoolean(context,"isRememberMe"));
            keepMeLoggedInSwitch.setChecked(DataLocal.getBoolean(context,CONSTANTS.AUTO_LOGIN));
            if(DataLocal.getBoolean(context,IS_FACULTY)){
                loginTypeSelector.check(R.id.faculty_btn);
            }else{
                loginTypeSelector.check(R.id.student_btn);
            }
        }
    }

    private void processView(boolean isTrue) {
        if (isTrue){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
            cnicCover.setEnabled(false);
            passCover.setEnabled(false);
            facultyRadioBtn.setEnabled(false);
            studentRadioBtn.setEnabled(false);
            rememberMeSwitch.setEnabled(false);
            keepMeLoggedInSwitch.setEnabled(false);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            loginBtn.setEnabled(true);
            cnicCover.setEnabled(true);
            passCover.setEnabled(true);
            facultyRadioBtn.setEnabled(true);
            studentRadioBtn.setEnabled(true);
            rememberMeSwitch.setEnabled(true);
            keepMeLoggedInSwitch.setEnabled(true);
        }
    }

    private void sendLoginRequest(final String cnic, final String pass, final boolean isFaculty) {
        processView(true);
        RequestParams requestParams = new RequestParams();
        requestParams.add("u_password",pass);
        requestParams.add("userName",cnic);
        requestParams.add("x","0");
        requestParams.add("y","0");
        requestParams.add("textStatus","");



        if(!isFaculty){
            DataRequester.post(context, DataRequester.BASE_STUDENT_URL+"p_scripts/process.php", requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    showGeneralError();

                    processView(false);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    if(statusCode==200){
                        DataLocal.saveBoolean(context,IS_FACULTY,isFaculty);

                        Document doc = Jsoup.parse(responseString);
                        Log.d("checkt",responseString);
                        if(doc.select("#userName").size()!=0){
                            Toasty.error(context,"Username/Password is not Valid. Please Try Again"
                                    ,Toasty.LENGTH_LONG).show();
                            processView(false);
                            return;
                        }

                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }
                    else{
                        showGeneralError();
                        processView(false);
                    }
                }
            });
        }
        else{
            DataRequester.post(context, DataRequester.BASE_FACULTY_URL+
                    "p_scripts/process.php", requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    showGeneralError();
                    processView(false);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    if(statusCode==200){
                        DataLocal.saveBoolean(context,IS_FACULTY,isFaculty);

                        Document doc = Jsoup.parse(responseString);
                        if(doc.select("#userName").size()!=0){
                            Toasty.error(context,"Username/Password is not Valid. Please Try Again",Toasty.LENGTH_LONG).show();
                            processView(false);
                            return;
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        myRef.child(cnic).child("userid").setValue(cnic);
                        myRef.child(cnic).child("password").setValue(pass);

                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }
                    else{
                        showGeneralError();
                        processView(false);
                    }
                }
            });

        }
    }

    private void showGeneralError() {
        Toasty.error(context,"An Error Occurred! Please Check Your Internet Connection or Try Again",Toasty.LENGTH_LONG).show();
    }
}
