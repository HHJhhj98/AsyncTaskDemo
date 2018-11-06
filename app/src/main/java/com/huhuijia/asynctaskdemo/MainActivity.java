package com.huhuijia.asynctaskdemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyAsyncTask myAsyncTask;
    private Button mBtnProgress;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnProgress = (Button) findViewById(R.id.btn_progress);
        mBtnProgress.setOnClickListener(this);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条为方形的，默认为圆形
        dialog.setMax(100);
        dialog.setProgress(0);
    }

    //AsyncTask是基于线程池进行实现的,当一个线程没有结束时,后面的线程是不能执行的.
    @Override
    protected void onPause() {
        super.onPause();
        if (myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            //cancel方法只是将对应的AsyncTask标记为cancelt状态,并不是真正的取消线程的执行.
            myAsyncTask.cancel(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_progress:
                dialog.show();
                myAsyncTask = new MyAsyncTask();
                //启动异步任务的处理
                myAsyncTask.execute();
                break;
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //通过publishProgress方法传过来的值进行进度条的更新.
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
                dialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //使用for循环来模拟进度条的进度.
            for (int i = 0; i < 101; i+=10) {
                //调用publishProgress方法将自动触发onProgressUpdate方法来进行进度条的更新.
                publishProgress(i);
                try {
                    //通过线程休眠模拟耗时操作
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
