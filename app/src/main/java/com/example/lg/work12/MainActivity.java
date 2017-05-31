package com.example.lg.work12;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText interval;
    ImageView image;
    TextView timer;
    mTask task1;
    int count = 0;
    boolean go = false; // 실행중인지 확인
    int images[] = {R.drawable.canada,R.drawable.china,R.drawable.france,
            R.drawable.germany,R.drawable.india,R.drawable.indonesia,R.drawable.italy,
            R.drawable.japan,R.drawable.korea,R.drawable.russia};//사진의 아이디를 저장하는 배열
    String imgnames[] = {"캐나다","중국","프랑스","독일","인도","인도네시아",
            "이탈리아","일본","한국","러시아"};// 사진의 이름을 저장하는 배열

    @Override
    protected void onDestroy() { //앱이 종료되면
        task1.cancel(true);//task1도 종료해준다.
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    void init(){
        setTitle("어디로 떠나볼까?");
        interval = (EditText)findViewById(R.id.interval);
        timer = (TextView)findViewById(R.id.timer);
        image = (ImageView)findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick","Clicked");
                task1 = new mTask();
                if(!go){ // 실행중이 아니면 실행시작
                    if(count>9){
                        count = 0;
                    }
                    go = true;
                    image.setImageResource(images[count]);//사진을 바꿔줌
                    count++;
                    timer.setText("시작부터 0초");
                    timer.setVisibility(View.VISIBLE);
                    task1.execute(getInterval(interval));//task1을 돌리기 시작
                }
                else{
                    go = false;
                }
            }
        });
    }
    class mTask extends AsyncTask<Integer,Integer,Integer>{ //AsyncTask를 만들어줌
        @Override
        protected Integer doInBackground(Integer... params) {//background에서 동작할 내용들
            int result = 0;
            for(int i=1;;i++){
                try {
                    if(!go){//중간에 실행이 취소되면 반복문 탈출
                        result = i;
                        break;
                    }
                    Thread.sleep(1000);//1초동안 일시정지
                    if(!go){
                        result = i;
                        break;
                    }
                    publishProgress(i,params[0]); //onProgressUpdate()로 넘어감
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {//publishProgress()가 호출되면 동작
            super.onProgressUpdate(values);
            timer.setText("시작부터 "+values[0]+"초");//Thread.sleep으로 1초동안 일시정지하므로 1초씩 증가해주는게 가능하다
            if(values[0]%(values[1]/1000)==0){//이미지 변경시간과 같으면
                if(count>9){
                    count = 0;
                }
                image.setImageResource(images[count]);//사진을 바꿔줌
                count++;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {//doInBackground가 종료되면 동작
            super.onPostExecute(integer);
            timer.setText(imgnames[count-1]+"선택("+(integer-1)+"초)");//이 사진이 선택되었다고 출력
        }

        @Override
        protected void onCancelled() {//mTask가 cancel되면 동작
            super.onCancelled();
            if(isCancelled()){

            }
        }
    }
    int getInterval(EditText et){//초를 받아오는 메소드
        String num = et.getText().toString();
        if(num.equals("")){
            return 1000;
        }
        else{
            return Integer.parseInt(num)*1000;
        }
    }

    public void onMyClick(View v){//처음으로가 눌리면 실행
        task1.cancel(true);
        task1 = null;
        interval.setText("");
        timer.setText("");
        timer.setVisibility(View.INVISIBLE);
        image.setImageResource(R.drawable.start);
        count = 0;
        go = false;
    }
}
