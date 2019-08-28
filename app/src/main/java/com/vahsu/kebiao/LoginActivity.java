package com.vahsu.kebiao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vahsu.kebiao.DBUtil.AppDatabase;
import com.vahsu.kebiao.DBUtil.CourseEntity;
import com.vahsu.kebiao.HtmlUtil.GetHtml;
import com.vahsu.kebiao.HtmlUtil.ParseScheduleHtml;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private String studentIDText;
    private String passwordText;
    private Button loginButton;
    private EditText studentIDEdit;
    private EditText passwordEdit;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean isSwitchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        isSwitchUser = intent.getBooleanExtra("isSwitchUser", false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(isSwitchUser);
            actionBar.setDisplayHomeAsUpEnabled(isSwitchUser);
        }
        loginButton = (Button) findViewById(R.id.login);
        studentIDEdit =(EditText) findViewById(R.id.studentID);
        passwordEdit = (EditText) findViewById(R.id.password);
        preferences = getSharedPreferences("data",MODE_PRIVATE);

        studentIDText = preferences.getString("studentID", "");
        passwordText = preferences.getString("password","");
        studentIDEdit.setText(studentIDText);
        passwordEdit.setText(passwordText);

    }

    public void login(View view){

        loginButton.setClickable(false);
        studentIDText = studentIDEdit.getText().toString();
        passwordText = passwordEdit.getText().toString();
        editor = preferences.edit();
        editor.putString("studentID",studentIDText);
        editor.apply();

        //执行登录异步任务
        new LoginTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
}
        return super.onOptionsItemSelected(item);
    }

    //登录异步任务
    private class LoginTask extends AsyncTask<Void, Integer, GetHtml>{

        @Override
        protected void onPreExecute(){
            Toast.makeText(LoginActivity.this.getApplicationContext(),"正在登录",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected GetHtml doInBackground(Void... strings) {
            GetHtml getHtml = new GetHtml(studentIDText, passwordText);
            if(getHtml.loginIsSuccessful()) {
                ParseScheduleHtml parseScheduleHtml = new ParseScheduleHtml(getHtml.getCourseHtml());
                List<CourseEntity> courseEntityList = parseScheduleHtml.getCourseList();
                AppDatabase.getInstance(LoginActivity.this.getApplicationContext()).CourseDao().deleteAll();
                AppDatabase.getInstance(LoginActivity.this.getApplicationContext()).CourseDao().addAll(courseEntityList);

                String firstDate = courseEntityList.get(0).getDate();
                String lastDate = courseEntityList.get(courseEntityList.size()-1).getDate();
                String[] firstTemp = firstDate.split("-", 2);
                String[] lastTemp = lastDate.split("-", 2);
                //判断年份是否加一
                if (Integer.parseInt(firstTemp[0]) * 31 + Integer.parseInt(firstTemp[1]) >
                        Integer.parseInt(lastTemp[0]) * 31 + Integer.parseInt(lastTemp[1])){
                    firstDate = parseScheduleHtml.getFirstYear() + "-" + firstDate;
                    lastDate = parseScheduleHtml.getLastYear() + "-" + lastDate;
                } else if ("二".equals(parseScheduleHtml.getPeriod())){
                    firstDate = parseScheduleHtml.getLastYear() + "-" + firstDate;
                    lastDate = parseScheduleHtml.getLastYear() + "-" + lastDate;
                } else {
                    firstDate = parseScheduleHtml.getFirstYear() + "-" + firstDate;
                    lastDate = parseScheduleHtml.getFirstYear() + "-" + lastDate;
                }
                editor.putInt("totalWeek",courseEntityList.get(courseEntityList.size()-1).getWeek());
                editor.putString("firstDate",firstDate);
                editor.putString("lastDate",lastDate);
                editor.putString("password",passwordText);
                editor.putBoolean("hasCourseData", true);
                editor.apply();
            }
            return getHtml;
        }

        @Override
        protected void onPostExecute(GetHtml getHtml){
            if(getHtml.loginIsSuccessful()){
                Toast.makeText(LoginActivity.this.getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this.getApplicationContext(),DisplayCourseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if("1".equals(getHtml.loginState())||"2".equals(getHtml.loginState())||"4".equals(getHtml.loginState())){
                Toast.makeText(LoginActivity.this.getApplicationContext(),"密码或者用户名错误",Toast.LENGTH_LONG).show();
                loginButton.setClickable(true);
            } else {
                Toast.makeText(LoginActivity.this.getApplicationContext(),"登录失败",Toast.LENGTH_LONG).show();
                loginButton.setClickable(true);
            }
        }
    }
}
