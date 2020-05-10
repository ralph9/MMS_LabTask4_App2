package com.example.digitalpetanca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;



import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation") public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate;

    AnimatedView animatedView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    public static int x;
    public static int y;

    static int widthOfDevice;
    static int heightOfDevice;

    static double ACCELERATION = 1.5;
    static int BALL_WIDTH = 80;
    static int BALL_HEIGHT = 80;
    int secondsPassed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        // Set seconds counter to 0
        secondsPassed = 0;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();

        animatedView = new AnimatedView(this);
        setContentView(animatedView);

        //We retrieve the data from the screen of the device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightOfDevice = displayMetrics.heightPixels;
        widthOfDevice = displayMetrics.widthPixels;

        //Timer to record the time spent during the game, later sent on to the intent of
        //PopUpGameOver as an extra
        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        secondsPassed++;
                    }
                });
            }
        }, 1000, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    //To alter the precision of the sensor, must be there but in our case we leave it empty as the
    //values obtained both from the emulator and the device on which we installed the APK seem to
    //reflect the moves correctly
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Whenever the value of the accelerometer changes we calculate based on the new data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Whenever we're inside the boundaries we can continue to move the ball
            if(x< widthOfDevice-BALL_WIDTH && x >= 0 && y>=0 && y < heightOfDevice - BALL_HEIGHT) {
                x -= (int) event.values[0]*ACCELERATION;
                y += (int) event.values[1]*ACCELERATION;
            }
            //Otherwise we lost
            else{
                System.out.println("Game over");
                //Popup with try again button, we send as extra the seconds that passed
                //during the current game
                Intent retryPopUp = new Intent(this,PopUpGameOver.class);
                retryPopUp.putExtra("seconds", secondsPassed);
                startActivity(retryPopUp);
            }
        }
    }

    public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {

        //Creation of the view, with the ball parameters
        public AnimatedView(Context context) {
            super(context);
            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xFF0000AA);
            mDrawable.setBounds(x, y, x + BALL_WIDTH, y + BALL_HEIGHT);

        }

        //Method called to draw the ball in the new position given
        @Override
        protected void onDraw(Canvas canvas) {
            mDrawable.setBounds(x, y, x + BALL_WIDTH, y + BALL_HEIGHT);
            mDrawable.draw(canvas);
            invalidate();
        }
    }



}