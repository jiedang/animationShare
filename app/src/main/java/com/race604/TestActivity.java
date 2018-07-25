package com.race604;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;

import com.example.dangjie.animationtest.R;
import com.race604.animation.ObjectAnimation;
import com.race604.test.EyeuTabCircle;
import com.race604.ticker.TicketShowView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        EyeuTabCircle eyeuTabCircle = (EyeuTabCircle) findViewById(R.id.eyeu_tab_circle);
        ObjectAnimation objectAnimation = new ObjectAnimation(eyeuTabCircle, 0, 1);
        objectAnimation.setRepeatCount(-1);
        objectAnimation.setRepeatMode(Animation.RESTART);
        objectAnimation.setDuration(5000);
        eyeuTabCircle.startAnimation(objectAnimation);
    }

}
