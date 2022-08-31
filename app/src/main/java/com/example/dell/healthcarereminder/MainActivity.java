package com.example.dell.healthcarereminder;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }
    private Button buttonshow;
    TableLayout table;
    TableRow row;
    TextView col1,col2,col3;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        table = (TableLayout)findViewById(R.id.table);
        table.setColumnStretchable(0,true);
        table.setColumnStretchable(1,true);
        table.setColumnStretchable(2,true);
        table.setColumnStretchable(3,true);
        fetch_table();

        ctx = this;

        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        final FloatingActionButton mFab= (FloatingActionButton) findViewById(R.id.add_action);
        FloatingActionButton mExer= (FloatingActionButton) findViewById(R.id.addexer);
        FloatingActionButton mGen= (FloatingActionButton) findViewById(R.id.addgen);
        FloatingActionButton mWater= (FloatingActionButton) findViewById(R.id.addwater);
        FloatingActionButton mMed= (FloatingActionButton) findViewById(R.id.addmed);
        final LinearLayout exLaylout=(LinearLayout)findViewById(R.id.add_eLayout);
        final LinearLayout genLaylout=(LinearLayout)findViewById(R.id.add_generalLayout);
        final LinearLayout wLaylout=(LinearLayout)findViewById(R.id.add_waterLayout);
        final LinearLayout medLaylout=(LinearLayout)findViewById(R.id.add_mediLayout);
        final Animation mshow=AnimationUtils.loadAnimation(MainActivity.this,R.anim.show_button);
        final Animation mhide=AnimationUtils.loadAnimation(MainActivity.this,R.anim.hide_button);
        final Animation sLay=AnimationUtils.loadAnimation(MainActivity.this,R.anim.show_layout);
        final Animation hLay=AnimationUtils.loadAnimation(MainActivity.this,R.anim.hide_layout);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exLaylout.getVisibility()==View.VISIBLE && genLaylout.getVisibility()==View.VISIBLE){
                    exLaylout.setVisibility(View.GONE);
                    genLaylout.setVisibility(View.GONE);
                    wLaylout.setVisibility(View.GONE);
                    medLaylout.setVisibility(View.GONE);
                    exLaylout.startAnimation(hLay);
                    genLaylout.startAnimation(hLay);
                    wLaylout.startAnimation(hLay);
                    medLaylout.startAnimation(hLay);
                    mFab.startAnimation(mhide);
                }
                else{
                    exLaylout.setVisibility(View.VISIBLE);
                    genLaylout.setVisibility(View.VISIBLE);
                    wLaylout.setVisibility(View.VISIBLE);
                    medLaylout.setVisibility(View.VISIBLE);
                    exLaylout.startAnimation(sLay);
                    genLaylout.startAnimation(sLay);
                    wLaylout.startAnimation(sLay);
                    medLaylout.startAnimation(sLay);
                    mFab.startAnimation(mshow);
                }
            }
        });

        mExer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exLaylout.setVisibility(View.GONE);
                genLaylout.setVisibility(View.GONE);
                wLaylout.setVisibility(View.GONE);
                medLaylout.setVisibility(View.GONE);
                exLaylout.startAnimation(hLay);
                genLaylout.startAnimation(hLay);
                wLaylout.startAnimation(hLay);
                medLaylout.startAnimation(hLay);
                mFab.startAnimation(mhide);
                openAddExercise();
            }
        });
        mGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exLaylout.setVisibility(View.GONE);
                genLaylout.setVisibility(View.GONE);
                wLaylout.setVisibility(View.GONE);
                medLaylout.setVisibility(View.GONE);
                exLaylout.startAnimation(hLay);
                genLaylout.startAnimation(hLay);
                wLaylout.startAnimation(hLay);
                medLaylout.startAnimation(hLay);
                mFab.startAnimation(mhide);
                openAddGeneral();
            }
        });
        mWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exLaylout.setVisibility(View.GONE);
                genLaylout.setVisibility(View.GONE);
                wLaylout.setVisibility(View.GONE);
                medLaylout.setVisibility(View.GONE);
                exLaylout.startAnimation(hLay);
                genLaylout.startAnimation(hLay);
                wLaylout.startAnimation(hLay);
                medLaylout.startAnimation(hLay);
                mFab.startAnimation(mhide);
                openAddWater();
            }
        });

        mMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exLaylout.setVisibility(View.GONE);
                genLaylout.setVisibility(View.GONE);
                wLaylout.setVisibility(View.GONE);
                medLaylout.setVisibility(View.GONE);
                exLaylout.startAnimation(hLay);
                genLaylout.startAnimation(hLay);
                wLaylout.startAnimation(hLay);
                medLaylout.startAnimation(hLay);
                mFab.startAnimation(mhide);
                openAddMedicine();
            }
        });
        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish")) {

                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish"));
    }


    public void openAddExercise(){
        Intent intent =new Intent(this,Add_Exercise.class);
        startActivity(intent);
    }

    public void openAddGeneral()
    {
        Intent intent = new Intent(this,Add_General.class);
        startActivity(intent);
    }

    public void openAddMedicine()
    {
        Intent intent = new Intent(this,Add_Medicine.class);
        startActivity(intent);
    }

    public void openAddWater()
    {
        Intent intent = new Intent(this,Add_Water.class);
        startActivity(intent);
    }


    public void fetch_table()
    {
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList reminderList = new ArrayList();
        reminderList = db.fetch_records();
        print_table(reminderList);
    }

    //public void print_table(String col1_name, String col2_date, String col3_time, final String table_name, final int id)
    public void print_table(ArrayList reminderList)
    {
        final DatabaseHelper db = new DatabaseHelper(this);
        TextView[] textArray = new TextView[reminderList.size()*10];
        Button[] buttonArray = new Button[reminderList.size()];
        TableRow[] tr_head = new TableRow[reminderList.size()];

        for(int i=0;i<reminderList.size();i++)
        {
            tr_head[i] = new TableRow(this);
            tr_head[i].setId(i+1);
            tr_head[i].setBackgroundColor(Color.GRAY);
            tr_head[i].setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));

            Reminderclass reminderDetails = new Reminderclass();
            reminderDetails = (Reminderclass) reminderList.get(i);
            String date = reminderDetails.getDate();
            String name = reminderDetails.getName();
            String time = reminderDetails.getTime();
            int timer = Integer.parseInt(time);
            int hours = timer/(60*60);
            int min = (timer-(hours*(60*60)))/60;
            String ap="";
            time="";
            if(hours>=12)
            {
                hours=hours-12;
                ap="PM";
            }
            else
            {
                ap = "AM";
            }
            if(hours<10)
            {
                time=time+"0"+hours;
            }
            else
            {
                time=time+hours;
            }
            if(min<10)
            {
                time=time+":0"+min;
            }
            else
            {
                time=time+":"+min;
            }
            time = time+" "+ap;
            final int mainid = reminderDetails.getMain_id();
            final String table_name = reminderDetails.getTableName();
            final int id = reminderDetails.getid();
            int j=i*3;
            textArray[j] = new TextView(this);
            textArray[j].setId(j+111);
            textArray[j].setText(name);
            textArray[j].setTextColor(Color.BLACK);
            textArray[j].setGravity(Gravity.CENTER);
            tr_head[i].addView(textArray[j]);

            textArray[j+1] = new TextView(this);
            textArray[j+1].setId(i+111);
            textArray[j+1].setText(date);
            textArray[j+1].setGravity(Gravity.CENTER);
            textArray[j+1].setTextColor(Color.BLACK);
            tr_head[i].addView(textArray[j+1]);

            textArray[j+2] = new TextView(this);
            textArray[j+2].setId(i+111);
            textArray[j+2].setText(time);
            textArray[j+2].setGravity(Gravity.CENTER);
            textArray[j+2].setTextColor(Color.BLACK);
            tr_head[i].addView(textArray[j+2]);

            buttonArray[i] = new Button(this);
            buttonArray[i].setId(i+211);
            buttonArray[i].setText("DELETE");
            tr_head[i].addView(buttonArray[i]);
            buttonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.delete(table_name,id,mainid);
                    finish();
                    startActivity(getIntent());
                }
            });
            //Log.d("DATABASE","AAGYAA");
            table.addView(tr_head[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT)
                    );

        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }


}

