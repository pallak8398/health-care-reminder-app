package com.example.dell.healthcarereminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;


public class Add_Exercise extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener  {

    DatabaseHelper db = new DatabaseHelper(this);
    String date="";
    String time="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__exercise);
        Button button = (Button)findViewById(R.id.setdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        Button buttontime = (Button)findViewById(R.id.setTime);
        buttontime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        Button buttonsave = (Button)findViewById(R.id.add_exercise);
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = (TextView)findViewById(R.id.exer_title);
                TextView freq = (TextView)findViewById(R.id.r_days);
                boolean result = db.insert_data_exercise(title.getText().toString(),date,time,freq.getText().toString());
                if(result == true) {
                    Toast.makeText(Add_Exercise.this, "Reminder Set !", Toast.LENGTH_LONG).show();
                    int id = db.getid("exercise_table");
                    boolean result2 = db.insert_data("exercise_table", id, date, time);
                }
                else
                    Toast.makeText(Add_Exercise.this,"Error while setting reminder",Toast.LENGTH_LONG).show();
            }
        });

        Button back=findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String selectdate = DateFormat.getDateInstance().format(c.getTime());
        TextView tv = (TextView)findViewById(R.id.exer_date);
        tv.setText(selectdate);
        String s="";
        month++;
        if(month<10)
        {
            if(dayOfMonth<10)
            {
                s=year+"-0"+month+"-0"+dayOfMonth;
            }
            else
            {
                s=year+"-0"+month+"-"+dayOfMonth;
            }
        }
        else
        {
            if(dayOfMonth<10)
            {
                s=year+"-"+month+"-0"+dayOfMonth;
            }
            else
            {
                s=year+"-"+month+"-"+dayOfMonth;
            }
        }
        try{
            date = s;
        }
        catch(Exception e)
        {
            System.out.println("parse failed");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv = (TextView)findViewById(R.id.TextTime);
        String text="";
        if(hourOfDay<10 && minute<10)
            tv.setText("0"+ hourOfDay+" : 0"+minute);
        else if(hourOfDay<10)
            tv.setText("0"+ hourOfDay+" : "+minute);
        else if(minute<10)
            tv.setText(hourOfDay+" : 0"+minute);
        else
            tv.setText(hourOfDay+" : "+minute);
        int hr=hourOfDay*60*60;
        int min = minute*60;
        int  tt = hr+min;
        time = Integer.toString(tt);
    }

    public void openMain() {
        Intent intent = new Intent("finish");
        sendBroadcast(intent);
        finish();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }
}
