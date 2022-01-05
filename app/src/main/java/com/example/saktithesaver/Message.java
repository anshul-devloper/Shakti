package com.example.saktithesaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.SEND_SMS;

public class Message extends AppCompatActivity {

    DataBase myDB;
    EditText textMessage;
    Button sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

         textMessage= findViewById(R.id.message);
         sendMessage =  findViewById(R.id.sendMessage);
        myDB= new DataBase(this);


    }
    public void sendMessage(View view){
        ArrayList<String> myList = new ArrayList<>();
        Cursor data = myDB.getListContent();
        if (data.getCount() == 0) {
            Toast.makeText(this, "no contact to show", Toast.LENGTH_SHORT).show();

        } else {
            String msg = textMessage.getText().toString();
            if(msg.length()>0){{
                Log.i( "msg: ",msg);
                while (data.moveToNext()) {
                    myList.add(data.getString(1));
                }
                if (!myList.isEmpty()) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        for (int i = 0; i < myList.size(); i++) {
                            sendSms(myList.get(i), msg,true);
                        }
                        textMessage.setText("");
                        Toast.makeText(getApplicationContext(), "SMS sent.",
                                Toast.LENGTH_LONG).show();

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(this, SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, 1);
                            }
                        }

                    }
                }
            }

            }else{
                Toast.makeText(this, "Type Message", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void sendSms(String number, String msg,boolean b) {

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(msg);
        sms.sendMultipartTextMessage(number, null, parts, null, null);

    }
}