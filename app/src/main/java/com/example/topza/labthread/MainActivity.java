package com.example.topza.labthread;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int counter;
    TextView tvCounter;
    int test;

    Thread thread;
    Handler handler;

    HandlerThread backgroundHandlerThread;
    Handler backgroundHandler;
    Handler mainHandler;

    SampleASyncTask sampleASyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        tvCounter = (TextView) findViewById(R.id.tvCounter);

        //Thread Method 1: Thread - not use
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Run in background
//                for (int i = 0; 1 < 100; i++){
//                    counter++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        return;
//                    }
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tvCounter.setText(counter + "");
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();

        //Thread Method 2: Thread with Handler
//        handler = new Handler(Looper.getMainLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                //Run in Main Thread
//                tvCounter.setText(msg.arg1 + "");
//            }
//        };
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Run in background
//                for (int i = 0; 1 < 100; i++){
//                    counter++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        return;
//                    }
//
//                    Message msg = new Message();
//                    msg.arg1 = counter;
//                    handler.sendMessage(msg);
//                }
//            }
//        });
//        thread.start();

        //Thread Method 3: Handler Only
//        handler = new Handler(getMainLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                counter++;
//                tvCounter.setText(counter + "");
//                if(counter < 100)
//                    sendEmptyMessageDelayed(0, 1000);
//            }
//        };
//        handler.sendEmptyMessageDelayed(0 , 1000);

        // Thread Method 5: AsyncTask
        sampleASyncTask = new SampleASyncTask();
//        sampleASyncTask.execute(0, 100);
        sampleASyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        backgroundHandlerThread.quit();
//        thread.interrupt();
        sampleASyncTask.cancel(true);
    }

    class SampleASyncTask extends AsyncTask<Integer, Float, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            //Run in Background Thread
            int start = params[0]; // 0
            int end = params[1]; // 100
            for (int i = start; i < end; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return false;
                }
                publishProgress(i + 0.0f);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            // Run on Main Thread
            super.onProgressUpdate(values);
            Float value = values[0];
            tvCounter.setText(value + " %");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // Run on Main Thread
            super.onPostExecute(aBoolean);
            test = 100;
            // work with aBoolean
        }
    }
}
