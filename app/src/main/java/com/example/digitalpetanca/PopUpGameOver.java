package com.example.digitalpetanca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopUpGameOver extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        int secondsRetrieved;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                secondsRetrieved= Integer.parseInt(null);
            } else {
                //Retrieve int
                secondsRetrieved = extras.getInt("seconds");
            }
        } else {
            secondsRetrieved= (int) savedInstanceState.getSerializable("seconds");
        }

        Button tryAgainButton = findViewById(R.id.buttonTryAgain);

        //Attach click listener to the try again button
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //On click restart application
                triggerRebirth(getApplicationContext());
            }
        });


        //Retrieve data passed and display seconds
        TextView seconds = findViewById(R.id.textSeconds);
        seconds.setText("Time: " + String.valueOf(secondsRetrieved) + " seconds");
    }

    //Method to restart application
    public static void triggerRebirth(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        Runtime.getRuntime().exit(0);
    }
}