package com.example.dell.healthcarereminder;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Add_Medicine extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    DatabaseHelper db = new DatabaseHelper(this);
    String date1 ;
    SimpleDateFormat format;
    String time1 = "",time3="";
    ImageButton datebut,timebut,timebut2;
    Button addbut;
    EditText name;
    EditText date;
    EditText time;
    EditText time2;
    EditText frequency;
    EditText dayfreq;
    int flag = 0;
    int start,inter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__medicine);

        format=new SimpleDateFormat("yyyy/mm/dd");

        datebut = (ImageButton) findViewById(R.id.datebut);
        timebut = (ImageButton) findViewById(R.id.timebut);
        timebut2 = (ImageButton) findViewById(R.id.timebut2);
        addbut = (Button) findViewById(R.id.add);

        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        time2 = (EditText) findViewById(R.id.interval);
        frequency = (EditText)findViewById(R.id.frequency);
        dayfreq = (EditText)findViewById(R.id.dayfreq);

        addbut.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if(name.getText()!=null && date.getText()!=null && time1!=null && frequency.getText()!=null && time3!=null && dayfreq.getText()!=null)
                {
                    int total =start+(inter*Integer.parseInt(dayfreq.getText().toString()));
                    if(total>(24*60)){
                        Toast.makeText(Add_Medicine.this,"Invalid data",Toast.LENGTH_LONG).show();
                    }
                    else {
                        boolean result = db.insert_med_data(name.getText().toString(), date1, time1, frequency.getText().toString(), dayfreq.getText().toString(), time3);

                        if (result == true) {
                            Toast.makeText(Add_Medicine.this, "Reminder Set !", Toast.LENGTH_LONG).show();
                            int id = db.getid("medicine_table");
                            boolean result2 = db.insert_data("medicine_table",id, date1, time1);
                        }
                        else
                            Toast.makeText(Add_Medicine.this, "Error while setting reminder", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        timebut.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                flag = 0;
                DialogFragment tp= new TimePickerFragment();
                tp.show(getSupportFragmentManager(),"time picker");
            }
        });

        timebut2.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                flag = 1;
                DialogFragment tp= new TimePickerFragment();
                tp.show(getSupportFragmentManager(),"time picker");
            }
        });


        datebut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String selectdate = DateFormat.getDateInstance().format(c.getTime());
        TextView tv = (TextView)findViewById(R.id.date);
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
            date1 = s;
        }
        catch(Exception e)
        {
            System.out.println("parse failed");
        }
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv;
        if(flag ==0)
            tv = (TextView)findViewById(R.id.time);
        else
            tv = (TextView)findViewById(R.id.interval);
        String text="";
        if(hourOfDay<10 && minute<10)
            tv.setText("0"+ hourOfDay+" : 0"+minute);
        else if(hourOfDay<10)
            tv.setText("0"+ hourOfDay+" : "+minute);
        else if(minute<10)
            tv.setText(hourOfDay+" : 0"+minute);
        else
            tv.setText(hourOfDay+" : "+minute);
        if(flag == 0)
        {
            int tt=hourOfDay*60*60+minute*60;
            time1 = Integer.toString(tt);
            start= 60*hourOfDay + minute;
        }
        else
        {
            int tt=hourOfDay*60*60+minute*60;
            time3 = Integer.toString(tt);
            inter = 60*hourOfDay+minute;
        }
    }

    public void openMain() {
        Intent intent = new Intent("finish");
        sendBroadcast(intent);
        finish();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }
}