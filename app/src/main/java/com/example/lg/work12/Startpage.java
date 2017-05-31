package com.example.lg.work12;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Startpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        Handler h = new Handler();
        h.postDelayed(new Runnable() { // 3초뒤에 MainActivity를 시작함
            @Override
            public void run() {
                Intent intent = new Intent(Startpage.this,MainActivity.class);
                startActivity(intent); // MainActivity시작
                finish(); // 현재 Activity종료
            }
        },3000);
    }
}
