package com.example.saktithesaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    Button b1,b3;
    EditText phone;
    ListView listView;
    DataBase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phone=findViewById(R.id.phone);
        b1=findViewById(R.id.Add);
        b3=findViewById(R.id.view);
        listView=findViewById(R.id.listView);
        myDB=new DataBase(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sr=phone.getText().toString();
                addData(sr);
                phone.setText("");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), contact.class);
                startActivity(intent);
            }
        });


    }


    private void addData(String newEntry){
        boolean insertData=myDB.addData(newEntry);
        if(insertData==true){
            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
        }

    }
}