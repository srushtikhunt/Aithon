package com.example.aithon;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Contact extends AppCompatActivity {

    Button btn_add , btn_delete , btn_show ,btn_gob;
    EditText txt_name , txt_email , txt_number ;
    SQLiteOpenHelper openHelper ;
    SQLiteDatabase db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);btn_add = findViewById(R.id.add_btn);
        btn_delete = findViewById(R.id.delete_btn);
        btn_show = findViewById(R.id.show_btn);
        txt_name = findViewById(R.id.name_ptxt);
        txt_email = findViewById(R.id.email_ptxt);
        txt_number = findViewById(R.id.number_ptxt);


        openHelper = new DatabaseHelp(this);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = txt_name.getText().toString();
                String _email = txt_email.getText().toString();
                String _number = txt_number.getText().toString();

                db = openHelper.getWritableDatabase();

                boolean ch = insertData(_name,_email,_number);
                if(ch) {
                    Toast.makeText(getApplicationContext(), "INSERTED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    txt_name.setText("");
                    txt_email.setText("");
                    txt_number.setText("");
                }
                else
                    Toast.makeText(getApplicationContext(),"ERROR IN INSERTE",Toast.LENGTH_LONG).show();

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();
                String _number = txt_number.getText().toString();
                boolean de = Delete(_number);
                if(de) {
                    txt_name.setText("");
                    txt_email.setText("");
                    txt_number.setText("");
                    Toast.makeText(getApplicationContext(), "DATA DELETED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"ERROR IN DATA DELETE",Toast.LENGTH_LONG).show();
            }
        });


        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllData();
            }
        });


    }
    public Boolean insertData(String name , String email , String number)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelp.COLS_2, name);
        contentValues.put(DatabaseHelp.COLS_3, email);
        contentValues.put(DatabaseHelp.COLS_4, number);
        long id = db.insert(DatabaseHelp.TABLE_NAME, null,contentValues);
        return id != -1;
    }
    public Boolean Delete(String name)
    {
        return  db.delete(DatabaseHelp.TABLE_NAME,DatabaseHelp.COLS_4+"=?",new String[]{name})>0;
    }

    public void getAllData()
    {
        db = openHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from "+DatabaseHelp.TABLE_NAME,null);
        if(res.getCount()==0)
        {
            showMsg("Error" , "No Data Founf");
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            buffer.append("Name : ").append(res.getString(1)).append("\n");
            buffer.append("Number : ").append(res.getString(3)).append("\n");
            buffer.append("Email : ").append(res.getString(2)).append("\n\n");

        }
        showMsg("Data", buffer.toString());

    }

    public void showMsg(String title , String Msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.show();
    }


}
