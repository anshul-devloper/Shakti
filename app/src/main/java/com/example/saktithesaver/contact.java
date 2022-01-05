package com.example.saktithesaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class contact extends AppCompatActivity {
     ListView listView;

    SQLiteDatabase sqLiteDatabase;
    DataBase myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        listView=findViewById(R.id.listView);
        myDB=new DataBase(this);

        final ArrayList<String> theList=new ArrayList<>();
        Cursor data=myDB.getListContent();
        if(data.getCount()==0){
            Toast.makeText(this, "There is no contact", Toast.LENGTH_SHORT).show();
        }else {
            while(data.moveToNext()){
                theList.add(data.getString(1));
                final  ListAdapter listAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList) ;

                listView.setAdapter(listAdapter);


            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int in, long l) {
                new AlertDialog.Builder(contact.this).setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle("Delete Contact")
                        .setMessage("Are you Sure ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                             String x=theList.get(in);
                            theList.remove(in);


                            sqLiteDatabase=myDB.getWritableDatabase();
                            sqLiteDatabase.delete(DataBase.TABLE_NAME, DataBase.Col2 + "=?", new String[]{x});
                            Toast.makeText(contact.this, "Contact Delete", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setNegativeButton("no",null).show();
            }
        });
    }
}