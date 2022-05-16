package com.providentitgroup.attendergcuf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.TextHttpResponseHandler;
import com.providentitgroup.attendergcuf.Utility.DataLocal;
import com.providentitgroup.attendergcuf.Utility.DataRequester;
import com.providentitgroup.attendergcuf.adapters.TimeTableAdapter;
import com.providentitgroup.attendergcuf.models.TimeTableItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class TimeTableActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArrayList[] timeTableItems;
    private final String[] weekNames = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        context = this;

        progressBar = findViewById(R.id.progressBar);
        timeTableItems = new ArrayList[7];

        Toolbar toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        // visibilty gone becouse time table not given on portal

      tabLayout.setVisibility(View.GONE);

        loadTimeTableData();
    }

    private void loadTimeTableData() {
        String URL;
        if (!DataLocal.getBoolean(this, LoginActivity.IS_FACULTY)) {
            URL = DataRequester.BASE_STUDENT_MODULE_URL + "timeTable.php";
        } else {
            URL = DataRequester.BASE_FACULTY_MODULE_URL + "timeTable.php";
        }
        DataRequester.get(context, URL, null, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
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
                                timeTableItems[j-1].add(new TimeTableItem(rows.get(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        j++;
                        if (j == 8) {
                            j = 0;
                        }
                    }

                    setupViewPager(viewPager);

                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ArrayList<String> daysIndexList = new ArrayList<>();
        DayTabAdapter adapter = new DayTabAdapter(getSupportFragmentManager(), 1);
        for (int i = 0; i < 7; i++) {
            if (timeTableItems[i].size() > 0) {
                adapter.addFrag(new TimeTableFragment(timeTableItems[i]), weekNames[i]);
                daysIndexList.add(weekNames[i]);
            }
        }
        Date date = Calendar.getInstance().getTime();
        String Day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()).toUpperCase();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(daysIndexList.indexOf(Day));
//gone visibilty
        tabLayout.setVisibility(View.GONE);
    }

    public class DayTabAdapter extends FragmentPagerAdapter {
        private ArrayList<String> tabsTitle;
        private ArrayList<Fragment> tabsList;

        public DayTabAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            tabsList = new ArrayList<>();
            tabsTitle = new ArrayList<>();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabsTitle.get(position);
        }

        void addFrag(TimeTableFragment timeTableFragment, String title) {
            tabsList.add(timeTableFragment);
            tabsTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return tabsList.get(position);
        }

        @Override
        public int getCount() {
            return tabsList.size();
        }

        public ArrayList<String> getTabsTitle() {
            return tabsTitle;
        }

        public void setTabsTitle(ArrayList<String> tabsTitle) {
            this.tabsTitle = tabsTitle;
        }

        public ArrayList<Fragment> getTabsList() {
            return tabsList;
        }

        public void setTabsList(ArrayList<Fragment> tabsList) {
            this.tabsList = tabsList;
        }
    }
}
