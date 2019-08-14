package com.vahsu.kebiao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vahsu.kebiao.DBUtil.AppDatabase;
import com.vahsu.kebiao.DBUtil.CourseLNRTL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity {

    private boolean isFirst = true;
    List<CourseLNRTL> courseLNRTLList = new ArrayList<>();
    private CourseAdapter mAdapter;
    List<String> dates;
    String lastDate;
    int totalWeek;
    private TextView[] top;
    private Spinner top_0;

    private Calendar calendar;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaycourse);
        calendar = Calendar.getInstance();
        top_0 = (Spinner) findViewById(R.id.top_0);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        totalWeek = preferences.getInt("totalWeek", 0);
        lastDate = preferences.getString("lastDate","0-0");
        String[] week = new String[totalWeek];
        for (int i = 0; i < week.length; i++) {
            week[i] = String.valueOf(i + 1) + "周";
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.week_item, week);
        spinnerAdapter.setDropDownViewResource(R.layout.week_item);
        top_0.setAdapter(spinnerAdapter);
        top_0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    new showCourseList(position + 1).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        top = new TextView[7];
        top[0] = findViewById(R.id.top_1);
        top[1] = findViewById(R.id.top_2);
        top[2] = findViewById(R.id.top_3);
        top[3] = findViewById(R.id.top_4);
        top[4] = findViewById(R.id.top_5);
        top[5] = findViewById(R.id.top_6);
        top[6] = findViewById(R.id.top_7);

        RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        //设置网格布局，行数12
        GridLayoutManager layoutManager = new GridLayoutManager
                (DisplayCourseActivity.this, 12, GridLayoutManager.HORIZONTAL, false) {
            //禁止myRecyclerView滚动
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

        };

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return courseLNRTLList.get(position).getLength();

            }
        });
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setHasFixedSize(true);
        //设置适配器
        mAdapter = new CourseAdapter(courseLNRTLList);
        mAdapter.setOnClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                CourseLNRTL courseLNRTL = courseLNRTLList.get(position);
                String title = courseLNRTL.getCourseName();
                if (null != courseLNRTL.getType()){
                    title += " [" + courseLNRTL.getType() + "]";
                }
                String message = "";
                if (null != courseLNRTL.getClassroom()){
                    message =  courseLNRTL.getClassroom();
                }
                if (null != courseLNRTL.getLecturer()){
                    message += "\n" + courseLNRTL.getLecturer();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定",null)
                        .create().show();
            }
        });
        myRecyclerView.setAdapter(mAdapter);

        new showCourseList().execute();
    }

    private class showCourseList extends AsyncTask<String, Integer, String> {
        private int week = -1;

        public showCourseList(int week) {
            this.week = week;
        }

        public showCourseList() {
        }

        @Override
        protected String doInBackground(String... strings) {

            if (week == -1) {
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String today = String.valueOf(month) + "-" + String.valueOf(day);
                String[] lastTemp = lastDate.split("-",2);
                int lastMonth = Integer.parseInt(lastTemp[0]);
                int lastDay = Integer.parseInt(lastTemp[1]);
                if ((month * 100 + day) > (lastMonth * 100 + lastDay)){
                    week = totalWeek;
                } else {
                    week = AppDatabase.getInstance(DisplayCourseActivity.this.getApplicationContext()).CourseDao().getWeekByDate(today);
                }
            }
            dates = AppDatabase.getInstance(DisplayCourseActivity.this.getApplicationContext()).CourseDao().getDatesByWeek(week);

            //课表信息List
            courseLNRTLList.clear();
            courseLNRTLList.addAll(AppDatabase.getInstance(DisplayCourseActivity.this.getApplicationContext()).CourseDao().getCourseByWeek(week));
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            top_0.setSelection(week - 1, false);
            String[] days = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
            for (int i = 0; i < 7; i++) {
                String text = dates.get(i) + "\n" + days[i];
                top[i].setText(text);
            }
            mAdapter.resetColor();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_course_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchUser:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确定要切换账号？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(DisplayCourseActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("取消", null)
                        .create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

