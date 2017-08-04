package com.example.abhishek.sensorrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;

/**
 * Created by Abhishek on 7/23/2017.
 */

public class SensorReaders extends AppCompatActivity {
    private String TAG = SensorReaders.class.getSimpleName();
    SensorManager sensorMgr;
    Sensor sensorAcc, sensorGyro, sensorLinearAcc, sensorGravity, sensorRotation, sensorMagnetic;
    Sensor sensorPressure;
    private long m_startTime;
    private TextView m_status;
    BroadcastReceiver receiver;
    private static final int NANOSECOND_TO_MILLISECOND = 1000000;
    private long avgDiff = 0;
    private long numberOfMeasurements = 0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    private PrintWriter accel_printWriter, gyro_printWriter, linaccel_printWriter, gravity_printWriter;
    private PrintWriter rotation_printWriter, magnetic_printWriter, pressure_printWriter;

    private final String ACCEL_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/accel_samples.txt";
    private final String GYRO_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/gyro_samples.txt";
    private final String LINACCEL_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/linaccel_samples.txt";
    private final String GRAVITY_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/gravity_samples.txt";
    private final String ROTATION_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/rotation_samples.txt";
    private final String MAGNETIC_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/magnetic_samples.txt";
    private final String PRESSURE_SAMPLES_FILE =
            Environment.getExternalStorageDirectory().getPath() + "/pressure_samples.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_status = (TextView)findViewById(R.id.status);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            Log.d(TAG, "Output file: " + ACCEL_SAMPLES_FILE);
            accel_printWriter = new PrintWriter(ACCEL_SAMPLES_FILE);
            //Log.d(TAG, "Output file: " + GYRO_SAMPLES_FILE);
            //gyro_printWriter = new PrintWriter(GYRO_SAMPLES_FILE);
            //Log.d(TAG, "Output file: " + LINACCEL_SAMPLES_FILE);
            //linaccel_printWriter = new PrintWriter(LINACCEL_SAMPLES_FILE);
            //Log.d(TAG, "Output file: " + GRAVITY_SAMPLES_FILE);
            //gravity_printWriter = new PrintWriter(GRAVITY_SAMPLES_FILE);
            //Log.d(TAG, "Output file: " + ROTATION_SAMPLES_FILE);
            //rotation_printWriter = new PrintWriter(ROTATION_SAMPLES_FILE);
            //Log.d(TAG, "Output file: " + MAGNETIC_SAMPLES_FILE);
            //magnetic_printWriter = new PrintWriter(MAGNETIC_SAMPLES_FILE);
            //Log.d(TAG, "Output file: " + PRESSURE_SAMPLES_FILE);
            //pressure_printWriter = new PrintWriter(PRESSURE_SAMPLES_FILE);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found exception");
        }

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorAcc = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sensorLinearAcc = sensorMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //sensorGyro = sensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE); // Sensor.TYPE_ALL
        //sensorGravity = sensorMgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //sensorRotation = sensorMgr.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        //sensorMagnetic = sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //sensorPressure = sensorMgr.getDefaultSensor(Sensor.TYPE_PRESSURE);

        PlayAudio pl = new PlayAudio();
        //pl.execute(getResources().openRawResource(R.raw.tone280hz));
        ParamsAsync pm = new ParamsAsync(getApplicationContext(), R.raw.goodbye);
        pl.execute(pm);

        // register a shutdown intent handler
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        registerReceiver(receiver, new IntentFilter("com.example.abhishek.sensorrecorder.intent.action.SHUTDOWN"));
    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();

        // m_locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10000.0f, onLocationChange);

        m_startTime = System.currentTimeMillis();
        //m_numGyroUpdates = 0;
        sensorMgr.registerListener(onSensorChange, sensorAcc , SensorManager.SENSOR_DELAY_FASTEST);
        //sensorMgr.registerListener(onSensorChange, sensorLinearAcc, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorMgr.registerListener(onSensorChange, sensorGyro, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorMgr.registerListener(onSensorChange, sensorGravity, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorMgr.registerListener(onSensorChange, sensorRotation , SensorManager.SENSOR_DELAY_FASTEST);
        //sensorMgr.registerListener(onSensorChange, sensorMagnetic , SensorManager.SENSOR_DELAY_FASTEST);
        //sensorMgr.registerListener(onSensorChange, sensorPressure , SensorManager.SENSOR_DELAY_FASTEST);
        registerReceiver(receiver, new IntentFilter("com.example.abhishek.sensorrecorder.intent.action.SHUTDOWN"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        // m_locMgr.removeUpdates(onLocationChange);
        sensorMgr.unregisterListener(onSensorChange);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - m_startTime;
        //Log.i(TAG, "Number of Gyroscope events: " + m_numGyroUpdates + ", Elapsed time: " + elapsedTime);
        accel_printWriter.flush();
        //gyro_printWriter.flush();
        //linaccel_printWriter.flush();
        //gravity_printWriter.flush();
        //rotation_printWriter.flush();
        //magnetic_printWriter.flush();
        //pressure_printWriter.flush();
        unregisterReceiver(receiver);
    }

    private SensorEventListener onSensorChange = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        synchronized public void onSensorChanged(SensorEvent event) {
            //++m_numGyroUpdates;
            // Log.d(TAG, "Gyroscope update");

            String[] strAcc = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0]),
                    Float.toString(event.values[1]),
                    Float.toString(event.values[2])};
            accel_printWriter.println(strAcc[0] + "," + strAcc[1] + "," + strAcc[2] + "," + strAcc[3]);

            /*// This timestep's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                float sqrmag = axisX*axisX + axisY*axisY + axisZ*axisZ;
                float omegaMagnitude = (float)Math.sqrt(sqrmag);

                // Normalize the rotation vector if it's big enough to get the axis
                // (that is, EPSILON should represent your maximum allowable margin of error)
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }*/

            /*String[] strGyro = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0]),
                    Float.toString(event.values[1]),
                    Float.toString(event.values[2])};
            gyro_printWriter.println(strGyro[0] + "," + strGyro[1] + "," + strGyro[2] + "," + strGyro[3]);*/

            /*String[] strLinAcc = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0]),
                    Float.toString(event.values[1]),
                    Float.toString(event.values[2])};
            linaccel_printWriter.println(strLinAcc[0] + "," + strLinAcc[1] + "," + strLinAcc[2] + "," + strLinAcc[3]);*/

            /*String[] strGravity = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0]),
                    Float.toString(event.values[1]),
                    Float.toString(event.values[2])};
            gravity_printWriter.println(strGravity[0] + "," + strGravity[1] + "," + strGravity[2] + "," + strGravity[3]);*/

            /*String[] strMagnetic = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0]),
                    Float.toString(event.values[1]),
                    Float.toString(event.values[2])};
            magnetic_printWriter.println(strMagnetic[0] + "," + strMagnetic[1] + "," + strMagnetic[2] + "," + strMagnetic[3]);*/

            /*String[] strRot = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0]),
                    Float.toString(event.values[1]),
                    Float.toString(event.values[2]),
                    Float.toString(event.values[3])};
            rotation_printWriter.println(strRot[0] + "," + strRot[1] + "," + strRot[2] + "," + strRot[3] + "," + strRot[4]);*/

            /*String[] strPress = {Long.toString((event.timestamp)),  //converting to millisecond
                    Float.toString(event.values[0])};
            pressure_printWriter.println(strPress[0] + "," + strPress[1]);*/

            /*switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER: String[] strAcc = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0]),
                        Float.toString(event.values[1]),
                        Float.toString(event.values[2])};
                    accel_printWriter.println(strAcc[0] + "," + strAcc[1] + "," + strAcc[2] + "," + strAcc[3]);
                    break;
                case Sensor.TYPE_GYROSCOPE: String[] strGyro = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0]),
                        Float.toString(event.values[1]),
                        Float.toString(event.values[2])};
                    gyro_printWriter.println(strGyro[0] + "," + strGyro[1] + "," + strGyro[2] + "," + strGyro[3]);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION: String[] strLinAcc = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0]),
                        Float.toString(event.values[1]),
                        Float.toString(event.values[2])};
                    linaccel_printWriter.println(strLinAcc[0] + "," + strLinAcc[1] + "," + strLinAcc[2] + "," + strLinAcc[3]);
                    break;
                case Sensor.TYPE_GRAVITY: String[] strGravity = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0]),
                        Float.toString(event.values[1]),
                        Float.toString(event.values[2])};
                    gravity_printWriter.println(strGravity[0] + "," + strGravity[1] + "," + strGravity[2] + "," + strGravity[3]);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD: String[] strMagnetic = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0]),
                        Float.toString(event.values[1]),
                        Float.toString(event.values[2])};
                    magnetic_printWriter.println(strMagnetic[0] + "," + strMagnetic[1] + "," + strMagnetic[2] + "," + strMagnetic[3]);
                    break;
                case Sensor.TYPE_ROTATION_VECTOR: String[] strRot = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0]),
                        Float.toString(event.values[1]),
                        Float.toString(event.values[2]),
                        Float.toString(event.values[3])};
                    magnetic_printWriter.println(strRot[0] + "," + strRot[1] + "," + strRot[2] + "," + strRot[3]);
                    break;
                case Sensor.TYPE_PRESSURE: String[] strPress = {Long.toString((event.timestamp)),  //converting to millisecond
                        Float.toString(event.values[0])};
                    magnetic_printWriter.println(strPress[0] + "," + strPress[1]);
                    break;
            }*/
        }
    };
}
