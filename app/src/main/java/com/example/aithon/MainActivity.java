package com.example.aithon;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity  extends AppCompatActivity implements SensorEventListener {

    Button btn_add, btn_notify;
    WebView wv1;
    SQLiteOpenHelper openHelper ;
    SQLiteDatabase db ;
    TextView hbeat,tem;
    public String[] data;
    BackgroundTask bt;
    int FLAG,COT;
    private SensorManager sensorManager;
    private Sensor mGyro, mLight;
    Float  gy1, gy2, gy3, le;
    String xyz_data;
    AlertDialog.Builder builder;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_addcon);
        btn_notify = findViewById(R.id.btn_notify);
        openHelper = new DatabaseHelp(this);
        hbeat = findViewById(R.id.h_beat);
        tem = findViewById(R.id.temprature);
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on Mobile Data! ")
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alert = builder.create();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if(!CheckConnection(this))
        {
            alert.show();
        }
        else
        {
            doiton();
            setlocationpage();
            writeXYZ();

            //------------------------------------------------------------------------------------

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            assert sensorManager != null;
            mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (mGyro != null) {
                sensorManager.registerListener((SensorEventListener) MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
                System.out.println( "on Create: Registered Gyro listener.");
            } else {
                System.out.println("EeeeeeeeeeeeeRRRRRRRRROOOOOooo 86");
            }
            mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (mLight != null) {
                sensorManager.registerListener((SensorEventListener) MainActivity.this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
                System.out.println("on Create: Registered Light listener.");
            } else {
                System.out.println("EeeeeeeeeeeeeRRRRRRRRROOOOOooo 93");
            }

            //------------------------------------------------------------------------------------
        }




    }

    public static boolean CheckConnection(Context ctx)
    {
        NetworkInfo.State dataenabled,wifi;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        dataenabled = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if(dataenabled == NetworkInfo.State.CONNECTED || wifi== NetworkInfo.State.CONNECTED)
            return true;
        else {
            Toast.makeText(ctx,"No internet Connection",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void setlocationpage()
    {
        WebView myWebView = (WebView) findViewById(R.id.view1);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setGeolocationEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        System.out.println("lllllllllocoocooooooooooooccccccccccccaaaaaaaaaatittttttiiiiioooooon");
        myWebView.loadUrl("https://vesaithon.000webhostapp.com/app/setlocation.php");
    }

    private void setAll() {
        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                data = output.split(" ");
                hbeat.setText(data[0].substring(1));
                tem.setText(data[1]);
                FLAG = Integer.valueOf(data[2].substring(0,data[0].length()-2));
                //System.out.println("fffffffffflllllllllllaaaaaaaaagggg :"+data[2].substring(0,data[0].length()-2));
                if(FLAG == 2)
                    sendNotification();
                bt=null;
            }
        });

        bt.execute("getsetall");
    }

    public void doiton()
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    while(true)
                    {
                        setAll();
                        Thread.sleep(3000);
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }


    public void openActivity2() {
        Intent intent = new Intent(this, Contact.class);
        startActivity(intent);
    }

    public void sendNotification() {
        if(FLAG == 1)
        {
            showMsg("Notify","Notification Already sended");
            return;
        }
        else {
            db = openHelper.getWritableDatabase();
            Cursor res = db.rawQuery("select * from " + DatabaseHelp.TABLE_NAME, null);
            Cursor temp = res;
            String r_mailid = "";
            if (res.getCount() == 0) {
                Toast.makeText(MainActivity.this, "NO ANY CONTECT IN LIST", Toast.LENGTH_SHORT).show();
            }
            while (res.moveToNext()) {
                //Toast.makeText(MainActivity.this, res.getCount(), Toast.LENGTH_SHORT).show();
                String number = res.getString(3);
                String msg = " your relative ABCD'S health is not good. \nplease visit given link \nvesaithon.000webhostapp.com/home";
                sendSMS(number, msg);
                r_mailid = r_mailid + res.getString(2) + ",";
            }
            try {
                sendmailtor(r_mailid);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "MAIL Failed to Send to ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendSMS(String number, String msg)
    {
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(number,null,msg,null,null);
            Toast.makeText(MainActivity.this, "SMS Sent to "+number, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, "SMS Failed to Send to "+number, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendmailtor(String r_mailid)  {
        r_mailid = r_mailid.substring(0, r_mailid.length()-1)+" ";
        final String finalR_mailid = r_mailid;
        new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender(
                            "sejsinh18@gmail.com",
                            "friday2001");
                    //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");
                    sender.sendMail("Health Com", "your relative ABCD's condition is not good kindly check given link vesaithon.000webhostapp.com/home",
                            "sejsinh18@gmail.com",
                            finalR_mailid);
                    //this.wait(3000);
                    //Toast.makeText(getApplicationContext(),"In try part",Toast.LENGTH_LONG).show();
                    //showMsg("mail send to",finalR_mailid);
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    //showMsg("mail fail send to",finalR_mailid);
                }
            }
        }).start();
        flagup();
    }

    public void showMsg(String title , String Msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.show();
    }


    public void flagup() {
        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                bt=null;
            }
        });
        String f = "1";
        bt.execute("flag",f);
    }

    public void flagdown(View view) {

        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                bt=null;
            }
        });
        String f = "0";
        bt.execute("flag",f);
        showMsg("i'm fine","Everything is ohk");
    }



    public Boolean checkmobiledata()
    {
        Context context = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            return  (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
        }
        return false;
    }

    //-----------------------------------------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            System.out.println( "on Create: X:" + sensorEvent.values[0] + " Y:" + sensorEvent.values[1] + " Z:" + sensorEvent.values[2]);
            //gyroMeterTV1.setText("gy 1 : " + sensorEvent.values[0]);
            gy1 = sensorEvent.values[0];
            //gyroMeterTV2.setText("gy 2 : " + sensorEvent.values[1]);
            gy2 = sensorEvent.values[1];
            //gyroMeterTV3.setText("gy 3 : " + sensorEvent.values[2]);
            gy3 = sensorEvent.values[2];
        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            //lightTV.setText("lightTV : " + sensorEvent.values[0]);
            le = sensorEvent.values[0];
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //-----------------------------------------------------------------------------------------------
    public void writeXYZ()
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        COT++;
                        xyz_data = xyz_data + " " + gy1 + " " + gy2 + " " + gy3 ;
                        Thread.sleep(500);
                        if(COT%4 == 0)
                        {
                            COT = 0;

                            bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    System.out.println("xxxxxxxxxoot "+output);
                                    bt=null;
                                }
                            });
                            System.out.println("357 xyz_data"+xyz_data);
                            bt.execute("set_xyz",xyz_data);
                            xyz_data = " ";
                            Thread.sleep(2000);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error in xxxxxxxxxxyyyyyyyyyzzzzzzzzzzzzz");
                }
            }
        }).start();
    }


}
