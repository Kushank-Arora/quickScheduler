package com.kushank.quickscheduler;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et = (EditText) findViewById(R.id.editText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText().toString().trim().equals(""))
                    return;
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                try {
                    String arr[] = getArray(et.getText().toString());
                    calIntent.putExtra(CalendarContract.Events.TITLE, arr[0].trim());
                    calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, arr[1].trim());

                    String desc = "";
                    if (arr.length > 5) {
                        for (int i = 5; i < arr.length; i++)
                            desc += arr[i] + "\n";

                    }

                    arr[2] = arr[2].trim();
                    if (arr[2].contains("OPENS AT: "))
                        arr[2] = arr[2].substring("OPENS AT: ".length());

                    arr[3] = arr[3].trim();
                    if (arr[3].contains("CLOSES AT: "))
                        arr[3] = arr[3].substring("CLOSES AT: ".length());

                    GregorianCalendar calDate1, calDate2;
                    calDate1 = getDate(arr[2]);
                    calDate2 = getDate(arr[3]);


                    calIntent.putExtra(CalendarContract.Events.DESCRIPTION, arr[4] + "\n" + desc);

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDate1.getTimeInMillis());
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calDate2.getTimeInMillis());
                    //Toast.makeText(MainActivity.this, calDate1.getTimeInMillis()+"", Toast.LENGTH_SHORT).show();
                    Log.d("Kushank",calDate1.getTimeInMillis()+"");
                    Log.d("Kushank",calDate2.getTimeInMillis()+"");
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }finally {
                    startActivity(calIntent);
                }
            }
        });


    }
    //  012345678901234567
    //S=AUG 11, 06:00 PM IST;
    private GregorianCalendar getDate(String s) {
        int mon, date, year=2017;
        int hour, min;
        Map<String, Integer> mp=new HashMap<>();
        mp.put("JAN",0);
        mp.put("FEB",1);
        mp.put("MAR",2);
        mp.put("APR",3);
        mp.put("MAY",4);
        mp.put("JUN",5);
        mp.put("JUL",6);
        mp.put("AUG",7);
        mp.put("SEP",8);
        mp.put("OCT",9);
        mp.put("NOV",10);
        mp.put("DEC",11);

        mon = mp.get(s.substring(0,3));
        date = Integer.parseInt(s.substring(4,6));

        hour  = Integer.parseInt(s.substring(8,10));
        min = Integer.parseInt(s.substring(11,13));

        if(s.substring(14,16).equals("PM"))
            hour+=12;
        else if(hour==12)
            hour=0;

        return new GregorianCalendar(year,mon,date,hour,min);
    }

    private String[] getArray(String s) {
        return s.split("\n");
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
            et.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if(clipboardManager.getPrimaryClip().getItemCount()==0)
            return;
        String s = clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(this).toString();
        if(s != null)
            et.setText(s);
    }
}
