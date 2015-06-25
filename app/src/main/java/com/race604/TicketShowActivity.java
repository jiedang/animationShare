package com.race604;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;

import com.example.dangjie.animationtest.R;
import com.race604.animation.ObjectAnimation;
import com.race604.ticker.TicketShowView;

public class TicketShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_show);

        TicketShowView ticketShowView = (TicketShowView) findViewById(R.id.ticketView);
        ObjectAnimation objectAnimation = new ObjectAnimation(ticketShowView, 0, 100);
        objectAnimation.setRepeatCount(-1);
        objectAnimation.setRepeatMode(Animation.RESTART);
        objectAnimation.setDuration(1500);
        ticketShowView.startAnimation(objectAnimation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticket_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
