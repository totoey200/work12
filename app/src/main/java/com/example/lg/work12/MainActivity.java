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
    boolean go = false;
    int images[] = {R.drawable.canada,R.drawable.china,R.drawable.france,
            R.drawable.germany,R.drawable.india,R.drawable.indonesia,R.drawable.italy,
            R.drawable.japan,R.drawable.korea,R.drawable.russia};
    String imgnames[] = {"캐나다","중국","프랑스","독일","인도","인도네시아",
            "이탈리아","일본","한국","러시아"};

    @Override
    protected void onDestroy() {
        task1.cancel(true);
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
                if(!go){
                    go = true;
                    image.setImageResource(images[count]);
                    count++;
                    timer.setText("시작부터 0초");
                    timer.setVisibility(View.VISIBLE);
                    task1.execute(getInterval(interval));
                }
                else{
                    go = false;
                }
            }
        });
    }
    class mTask extends AsyncTask<Integer,Integer,Integer>{
        @Override
        protected Integer doInBackground(Integer... params) {
            int result = 0;
            for(int i=1;;i++){
                try {
                    Thread.sleep(1000);
                    if(!go){
                        result = i;
                        break;
                    }
                    publishProgress(i,params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            timer.setText("시작부터 "+values[0]+"초");
            if(values[0]%(values[1]/1000)==0){
                if(count>9){
                    count = 0;
                }
                image.setImageResource(images[count]);
                count++;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            timer.setText(imgnames[count-1]+"선택("+(integer-1)+"초)");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(isCancelled()){

            }
        }
    }
    int getInterval(EditText et){
        String num = et.getText().toString();
        if(num.equals("")){
            return 1000;
        }
        else{
            return Integer.parseInt(num)*1000;
        }
    }

    public void onMyClick(View v){
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
