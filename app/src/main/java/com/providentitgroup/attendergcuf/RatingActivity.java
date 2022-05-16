package com.providentitgroup.attendergcuf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.Utility.DataValidator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class RatingActivity extends AppCompatActivity {

    String qecLink;
    String instructorName;
    String courseName;
    ConstraintLayout startEndContainer;
    ConstraintLayout quizContainer;
    ProgressBar dataLoader;
    ProgressBar progressBar;
    TextView question;
    SmileRating smileRating;
    ImageView closeBtn;
    ImageView nextBtn;
    Button startEndBtn;
    TextView evaluationTitle;
    TextView instructorNameText;
    TextView courseNameText;
    ArrayList<Integer> results;
    String comments;
    ArrayList<String> questions;
    EditText commentBox;
    int index;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            qecLink = bundle.getString("qecLink");
            instructorName = bundle.getString("teacherName");
            courseName = bundle.getString("courseName");
            if(qecLink==null || qecLink.isEmpty() || instructorName == null || instructorName.isEmpty() ||
                    courseName==null || courseName.isEmpty()
            ){
                Toasty.info(getApplicationContext(),"This Course was already evaluated by you.",Toasty.LENGTH_LONG).show();
                finish();
            }
        }

        lottieAnimationView = findViewById(R.id.lottie_view);
        lottieAnimationView.setAnimation(R.raw.ratings);
        lottieAnimationView.playAnimation();
        commentBox = findViewById(R.id.editText);
        questions = new ArrayList<>();
        results = new ArrayList<>();
        startEndBtn = findViewById(R.id.button);
        startEndContainer = findViewById(R.id.start_end_container);
        quizContainer = findViewById(R.id.quiz_container);
        dataLoader = findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.rating_progress_bar);
        question = findViewById(R.id.question);
        smileRating = findViewById(R.id.smile_rating);
        closeBtn = findViewById(R.id.close_btn);
        nextBtn = findViewById(R.id.next_btn);
        evaluationTitle = findViewById(R.id.title);
        instructorNameText= findViewById(R.id.teacher_name);
        courseNameText = findViewById(R.id.course_name);

        commentBox.setImeActionLabel("Close Keyboard", KeyEvent.KEYCODE_ENTER);

        setRatingNames();
        setCloseBtnListener();

        loadData();

    }

    private void setRatingNames() {
        smileRating.setNameForSmile(BaseRating.TERRIBLE,"0%");
        smileRating.setNameForSmile(BaseRating.BAD,"25%");
        smileRating.setNameForSmile(BaseRating.OKAY,"50%");
        smileRating.setNameForSmile(BaseRating.GOOD,"75%");
        smileRating.setNameForSmile(BaseRating.GREAT,"100%");
    }

    private void setCloseBtnListener() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RatingActivity.this,QecRankingActivity.class));
                finish();
            }
        });
    }

    private void loadData() {
        DataRequester.get(this, DataRequester.BASE_STUDENT_MODULE_URL + qecLink, null, new TextHttpResponseHandler() {


            @Override
            public void onStart() {
                dataLoader.setVisibility(View.VISIBLE);
                quizContainer.setVisibility(View.GONE);
                startEndContainer.setVisibility(View.GONE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                dataLoader.setVisibility(View.GONE);
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode == 200){
                    Document doc = Jsoup.parse(responseString);
                    Elements rows = doc.select("table table tr");
                    Log.d("TAG",rows.text());

                    instructorNameText.setText(instructorName);
                    courseNameText.setText(courseName);
                    if(rows.size()==2){
                        lottieAnimationView.setAnimation(R.raw.success);
                        lottieAnimationView.playAnimation();
                        evaluationTitle.setText("Course Has Been Evaluated!");
                        startEndBtn.setText("Continue");
                        startEndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(RatingActivity.this,QecRankingActivity.class));
                                finish();
                            }
                        });
                        startEndContainer.setVisibility(View.VISIBLE);
                    }else{
                        evaluationTitle.setText("Instructor and Course Evaluation");
                        for(int i=1;i<rows.size();i++){
                            if(rows.get(i).children().size()>1){
                                questions.add(DataValidator.toTitleCase(rows.get(i).child(1).text().trim()));
                                results.add(0);
                            }
                        }
                        startEndBtn.setText("Start Evaluation");
                        startEndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startQuiz();
                            }
                        });
                        startEndContainer.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void startQuiz() {
        index =0;
        question.setText(questions.get(index));
        smileRating.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        commentBox.setVisibility(View.GONE);
        smileRating.setSelectedSmile(BaseRating.NONE);
        startEndContainer.setVisibility(View.GONE);
        quizContainer.setVisibility(View.VISIBLE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(smileRating.getRating()==0){
                    Toasty.normal(getApplicationContext(),"Please Rate this question first!",Toasty.LENGTH_SHORT).show();
                    return;
                }
                Log.d("RATING",smileRating.getRating()+"");

                results.set(index,smileRating.getRating());
                Log.d("RESULT_SIZE",results.size()+"");

                int progress = ((index+1)*100)/questions.size();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(progress,true);
                }else{
                    progressBar.setProgress(progress);
                }
                index++;
                if(questions.size()-1>index){
                    question.setText(questions.get(index));
                    smileRating.setSelectedSmile(BaseRating.NONE);
                }else{
                    finalQuiz();
                }
            }
        });
    }

    private void finalQuiz() {
        question.setText("Write Any Comments Or Suggestions Here to improve the course and teaching methods");
        commentBox.setVisibility(View.VISIBLE);
        smileRating.setVisibility(View.INVISIBLE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments =commentBox.getText().toString().trim();
                new AlertDialog.Builder(RatingActivity.this)
                        .setTitle("Submit The Review")
                        .setMessage("Press Submit to send your evaluation. Your review will help to make this course more fruitful in future.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    sendReportForEvaluation();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).create().show();
            }
        });
    }

    private void sendReportForEvaluation() throws MalformedURLException, UnsupportedEncodingException {

        String tempStr =prepareResult();
        Map<String, String> map = splitQuery(new URL(DataRequester.BASE_STUDENT_MODULE_URL+qecLink));
        Log.d("TAG",map.toString());
        Log.d("TAG",tempStr);

        RequestParams params = new RequestParams();
        params.add("totalVal","20");
        params.add("courseTitle", map.get("courseTitle"));
        params.add("teacherName",map.get("teacherName"));
        params.add("sessionValues12",map.get("sessionValues12"));
        params.add("CCT_RECNO",map.get("CCT_RECNO"));
        params.add("semaster2",map.get("semaster2"));
        params.add("STD_CCT_RECNO",map.get("STD_CCT_RECNO"));
        params.add("deptName",map.get("deptName"));
        params.add("tempStr",tempStr);
        params.add("COMMENTS",comments);
        DataRequester.get(this, DataRequester.BASE_STUDENT_MODULE_URL + "qec_rating.php", params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {

                dataLoader.setVisibility(View.GONE);
                closeBtn.setVisibility(View.GONE);
                lottieAnimationView.setAnimation(R.raw.sending);
                lottieAnimationView.playAnimation();
                evaluationTitle.setText("Sending Evaluation Report");
                quizContainer.setVisibility(View.GONE);
                startEndContainer.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(statusCode == 200){
                    Document doc = Jsoup.parse(responseString);
                    Elements rows = doc.select("table table tr");
                    Log.d("TAG",rows.text());

                    instructorNameText.setText(instructorName);
                    courseNameText.setText(courseName);
                    if(rows.size()==2){
                        lottieAnimationView.setAnimation(R.raw.success);
                        lottieAnimationView.playAnimation();
                        evaluationTitle.setText("Course Has Been Evaluated!");
                        startEndBtn.setText("Continue");
                        startEndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(RatingActivity.this,QecRankingActivity.class));
                                finish();
                            }
                        });
                        startEndContainer.setVisibility(View.VISIBLE);
                    }else{
                        lottieAnimationView.setAnimation(R.raw.ratings);
                        lottieAnimationView.playAnimation();
                        Toasty.success(getApplicationContext(),"An Error Occurred! Re-Evalute the Course.",Toasty.LENGTH_LONG).show();
                        evaluationTitle.setText("Instructor and Course Evaluation");
                        for(int i=1;i<rows.size();i++){
                            if(rows.get(i).children().size()>1){
                                questions.add(DataValidator.toTitleCase(rows.get(i).child(1).text().trim()));
                                results.add(0);
                            }
                        }
                        startEndBtn.setText("Start Evaluation");
                        startEndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startQuiz();
                            }
                        });
                        startEndContainer.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    private String prepareResult() {
        StringBuilder str = new StringBuilder("tempStr=");
        for(int i=0;i<results.size();i++){
            Log.d("TAGINDEX",i+"");
            Log.d("TAGINDEXSIZE",results.size()+"");
            switch (results.get(i)){
                case 1:
                    str.append((i < 12) ? String.format("_E_%s_V_%s_V_P", i + 1, 5) : String.format("_E_%s_V_%s_V_C", i + 1, 5));
                    break;
                case 2:
                    str.append((i < 12) ? String.format("_E_%s_V_%s_V_P", i + 1, 4) : String.format("_E_%s_V_%s_V_C", i + 1, 5));
                    break;
                case 3:
                    str.append((i < 12) ? String.format("_E_%s_V_%s_V_P", i + 1, 3) : String.format("_E_%s_V_%s_V_C", i + 1, 5));
                    break;
                case 4:
                    str.append((i < 12) ? String.format("_E_%s_V_%s_V_P", i + 1, 2) : String.format("_E_%s_V_%s_V_C", i + 1, 5));
                    break;
                case 5:
                    str.append((i < 12) ? String.format("_E_%s_V_%s_V_P", i + 1, 1) : String.format("_E_%s_V_%s_V_C", i + 1, 5));
                    break;
            }
        }
        return str.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,QecRankingActivity.class));
        finish();
    }
    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
