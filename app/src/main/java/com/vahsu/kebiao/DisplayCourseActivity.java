package com.vahsu.kebiao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.vahsu.kebiao.DBUtil.AppDatabase;
import com.vahsu.kebiao.DBUtil.CourseLNR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DisplayCourseActivity extends AppCompatActivity {

    List<CourseLNR> courseLNRList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    List<String> dates;
    private TextView[] top;
    private int week;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaycourse);
        top = new TextView[8];
        top[0] = findViewById(R.id.top_0);
        top[1] = findViewById(R.id.top_1);
        top[2] = findViewById(R.id.top_2);
        top[3] = findViewById(R.id.top_3);
        top[4] = findViewById(R.id.top_4);
        top[5] = findViewById(R.id.top_5);
        top[6] = findViewById(R.id.top_6);
        top[7] = findViewById(R.id.top_7);

        new initCourseList().execute();
    }

    private class initCourseList extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {

            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String today = String.valueOf(month) + "-" + String.valueOf(day);
            week = AppDatabase.getInstance(DisplayCourseActivity.this.getApplicationContext()).CourseDao().getWeekByDate(today);
            dates = AppDatabase.getInstance(DisplayCourseActivity.this.getApplicationContext()).CourseDao().getDatesByWeek(week);

            //课表信息List
            courseLNRList = AppDatabase.getInstance(DisplayCourseActivity.this.getApplicationContext()).CourseDao().getCourseByWeek(week);
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {

            String weekString = String.valueOf(week)+"周";
            top[0].setText(weekString);
            String[] days = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
            for(int i=1; i<8; i++){
                String text = dates.get(i-1)+"\n"+days[i-1];
                top[i].setText(text);
            }

            RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
            //设置网格布局，行数12
            GridLayoutManager layoutManager = new GridLayoutManager
                    (DisplayCourseActivity.this, 12,GridLayoutManager.HORIZONTAL,false){
                //禁止myRecyclerView滚动
                @Override
                public boolean canScrollHorizontally(){
                    return false;
                }

            };

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return courseLNRList.get(position).getCourseLength();

                }
            });
            myRecyclerView.setLayoutManager(layoutManager);
            myRecyclerView.setHasFixedSize(true);

            //设置适配器
            mAdapter = new CourseAdapter(DisplayCourseActivity.this.getApplicationContext(), courseLNRList);
            myRecyclerView.setAdapter(mAdapter);

        }
    }
}

