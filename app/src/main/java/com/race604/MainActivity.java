package com.race604;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.dangjie.animationtest.R;
import com.race604.flyrefresh.sample.FlyRefreshMainActivity;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);

        ArrayList<String> data = new ArrayList<String>();
        data.add("挤出");
        data.add("FlyRefresh");
        data.add("挤出 object");
        data.add("出票");
        data.add("圆环");

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                switch (i){

                    case 0 :
                        intent = new Intent(MainActivity.this, ExtrusionActivity.class);
                        break;
                    case 1 :
                        intent = new Intent(MainActivity.this, FlyRefreshMainActivity.class);
                        break;
                    case 2 :
                        intent = new Intent(MainActivity.this, ExtrusionActivityByObject.class);
                        break;

                    case 3 :
                        intent = new Intent(MainActivity.this, TicketShowActivity.class);
                        break;
                    case 4 :
                        intent = new Intent(MainActivity.this, CircleActivity.class);
                        break;
                }
                if(intent != null){
                    startActivity(intent);
                }
            }
        });

        setContentView(listView);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
