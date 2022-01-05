package com.example.saktithesaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity2 extends AppCompatActivity {

    Button b, b1;
    Button message;
    Button safty;
    DataBase myDB;

    String x = "", y = "";
    String address = "";


    LocationManager locationManager;
    LocationListener locationListener;

    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        Log.i( "onRequestPermissionsR ",String.valueOf(location));
                                        sendAddress(location);
                                    }
                                }
                            });

                }}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b = findViewById(R.id.addContact);
        b1 = findViewById(R.id.button2);
        message=findViewById(R.id.message);
        safty=findViewById(R.id.safty);
        myDB = new DataBase(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);



        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPS();

        }
        else {
            startTrack();
        }




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();

            }
        });
    }
    public void viewContact(View view){
        Intent intent = new Intent(getApplicationContext(), contact.class);
        startActivity(intent);
    }

    public void message(View view){
        Intent intent = new Intent(getApplicationContext(), Message.class);
        startActivity(intent);
    }
    public  void safety(View view){
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel: 8851476119"));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(i);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }
        }
    }

    private void loadData() {
        ArrayList<String> myList = new ArrayList<>();
        Cursor data = myDB.getListContent();
        if (data.getCount() == 0) {
            Toast.makeText(this, "no contact to show", Toast.LENGTH_SHORT).show();

        } else {
            String msg = "I NEED HELP! MY LOCATION= latitude: " + x + " longitude: " + y + " " + address;
            Log.i( "msg: ",msg);


            while (data.moveToNext()) {
                myList.add(data.getString(1));
            }
            if (!myList.isEmpty()) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < myList.size(); i++) {
                        sendSms(myList.get(i), msg,true);
                    }
                    call();

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(this, SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, 1);
                        }
                    }

                }
            }

        }
    }

    private void sendSms(String number, String msg,boolean b) {

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(msg);
        sms.sendMultipartTextMessage(number, null, parts, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    private void call() {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel: 8851476119"));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(i);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }
        }
    }

    private void startTrack() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i( "onLocationChanged: ",String.valueOf(location));
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {


            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.i( "onRequestPermissionsR ",String.valueOf(location));
                                sendAddress(location);
                            }
                        }
                    });
        }



    }


    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));


            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendAddress(Location location) {
        if(location!=null) {
            double let = location.getLatitude();
            double lng = location.getLongitude();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<Address> listAddresses;
                listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (listAddresses != null && listAddresses.size() > 0) {

                    Log.i("Address", listAddresses.get(0).toString());
                    if (listAddresses.get(0).getThoroughfare() != null) {
                        address += listAddresses.get(0).getThoroughfare() + " ";
                    }

                    if (listAddresses.get(0).getLocality() != null) {
                        address += listAddresses.get(0).getLocality() + " ";
                    }

                    if (listAddresses.get(0).getPostalCode() != null) {
                        address += listAddresses.get(0).getPostalCode() + " ";
                    }

                    if (listAddresses.get(0).getAdminArea() != null) {
                        address += listAddresses.get(0).getAdminArea()+" ";
                    }
                    if(listAddresses.get(0).getAddressLine(0)!=null){
                        address+=listAddresses.get(0).getAddressLine(0);
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            x = String.valueOf(let);
            Log.i( "x latitude: " ,x);
            y = String.valueOf(lng);
            Log.i("y longitude ",y);


        }
        else{
            Toast.makeText(this, "unable to find location", Toast.LENGTH_SHORT).show();
        }
    }
}