package com.example.dell.healthcarereminder;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Add_Water extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper db = new DatabaseHelper(this);
    EditText chooseTime1;
    EditText chooseTime2;
    TimePickerDialog timePickerDialog1;
    TimePickerDialog timePickerDialog2;
    Calendar calendar1;
    int currentHour1;
    int currentMinute1;
    String amPm1;
    int currentHour2;
    int currentMinute2;
    String amPm2;
    EditText interval1;
    //EditText repeat1;
    int content_interval;
    //int content_repeat;
    Button save,back;
    String start,end,date="";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__water);

        chooseTime1=findViewById(R.id.etChooseStartTime);
        chooseTime1.setOnClickListener(this);
        chooseTime2=findViewById(R.id.etChooseEndTime);
        chooseTime2.setOnClickListener(this);
        save=findViewById(R.id.save_btn);
        save.setOnClickListener(this);
        back=findViewById(R.id.back_water);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.etChooseStartTime:
                calendar1 = Calendar.getInstance();
                currentHour1 = calendar1.get(Calendar.HOUR_OF_DAY);
                currentMinute1 = calendar1.get(Calendar.MINUTE);
                timePickerDialog1 = new TimePickerDialog(Add_Water.this, new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm1 = "PM";
                        } else {
                            amPm1 = "AM";
                        }
                        start=String.format("%02d:%02d", hourOfDay, minutes);
                        chooseTime1.setText(start);
                        int tt=hourOfDay*60*60+minutes*60;
                        start = Integer.toString(tt);
                    }

                }, currentHour1, currentMinute1, false);

                timePickerDialog1.show();
                break;

            case R.id.etChooseEndTime:
                currentHour2 = currentHour1;
                currentMinute2 = currentMinute1;
                timePickerDialog2 = new TimePickerDialog(Add_Water.this, new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm2 = "PM";
                        } else {
                            amPm2 = "AM";
                        }
                        end=String.format("%02d:%02d", hourOfDay, minutes);
                        chooseTime2.setText(end);
                        int tt=hourOfDay*60*60+minutes*60;
                        end = Integer.toString(tt);

                    }

                }, currentHour2, currentMinute2, false);

                timePickerDialog2.show();
                break;
            case R.id.save_btn:
                interval1=findViewById(R.id.setInterval);
                String content=interval1.getText().toString();
                content_interval=Integer.parseInt(content);
                //repeat1=findViewById(R.id.setRepeat);
                //String content1=repeat1.getText().toString();
                boolean result = db.insert_data_water(start,end,content_interval);
                if(result == true) {
                    Toast.makeText(Add_Water.this, "Reminder Set !", Toast.LENGTH_LONG).show();
                    int id = db.getid("water_table");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date1 = new Date();
                    System.out.println(dateFormat.format(date1));
                    String temp = dateFormat.format(date1);
                    int i=0;
                    for(i=0;temp.charAt(i)!=' ';i++)
                    {
                        if(temp.charAt(i)=='/')
                        {
                            date=date+"-";
                        }
                        else
                            date=date+temp.charAt(i);
                    }
                    boolean result2 = db.insert_data("water_table",id,date , start);
                }
                else
                    Toast.makeText(Add_Water.this,"Error while setting reminder",Toast.LENGTH_LONG).show();
                break;
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