package com.vahsu.kebiao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        boolean hasCourseData = preferences.getBoolean("hasCourseData",false);
        if (hasCourseData){
            Intent intent = new Intent(this,DisplayCourseActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}

