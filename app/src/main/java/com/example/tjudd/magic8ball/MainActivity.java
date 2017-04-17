package com.example.tjudd.magic8ball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private TextView mFortuneTextView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ArrayList<String> mResponses;
    private long mLastUpdateTime;
    private double prevXPos;
    private double prevYPos;
    private double prevZPos;
    private double THRESHOLD;
    private long diffTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFortuneTextView = (TextView) findViewById(R.id.textView);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        mLastUpdateTime = 0;
        prevXPos = 0;
        prevYPos = 0;
        prevZPos = 0;
        diffTime = 0;
        THRESHOLD = 800;

        mResponses = new ArrayList<>();
        mResponses.add("yes");
        mResponses.add("no");
        mResponses.add("maybe");
        mResponses.add("the answer is unclear");
        mResponses.add("try again...");
    }

    public void updateUI() {
        Integer intRandom = (int) (Math.random()*mResponses.size()-1);
        mFortuneTextView.setText(mResponses.get(intRandom));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

            long curTime = System.currentTimeMillis();
            if(curTime - mLastUpdateTime > 100) {
                diffTime = (curTime - mLastUpdateTime);
                mLastUpdateTime = curTime;
                double speed = Math.abs(prevXPos+prevYPos+prevZPos - x - y - z) / diffTime * 10000;

               Log.v("Speed", String.valueOf(speed));
               if ((int)speed > THRESHOLD) {
                    updateUI();
               }
               prevXPos = x;
               prevYPos = y;
               prevZPos = z;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
