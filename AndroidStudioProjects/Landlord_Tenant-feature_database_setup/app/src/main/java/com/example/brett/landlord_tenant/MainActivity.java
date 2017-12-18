package com.example.brett.landlord_tenant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String name = "";
    String identifier = "";
    String username = "";
    String landlordName ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!= null){
            name = extras.getString("name");
            identifier = extras.getString("identifier");
            username = extras.getString("username");
            landlordName = extras.getString("landlordUsername");
        }

        if(identifier.equals("landlord")){
            setContentView(R.layout.activity_landlord_main);
        }
        else if (identifier.equals("tenant")){
            setContentView(R.layout.activity_tenant_main);
        }
        else{
            setContentView(R.layout.activity_tenant_main);
        }

        TextView welcome_name = (TextView) findViewById(R.id.welcome_name);
        welcome_name.setText(name);

        Intent x = new Intent(getApplicationContext(), NotificationService.class);
        x.putExtra("name", name);
        x.putExtra("identifier", identifier);
        x.putExtra("landlord",landlordName);
        startService(x);
    }

    public void maintenance(View view){
        Intent intent = new Intent(this, maintenanceActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }

    public void messages(View view){
        Intent intent = new Intent(this, messagesActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }

    public void rent(View view){
        if(identifier.equals("tenant")){
            Intent intent = new Intent(this, TenantRentActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("tenantusername", username);
            intent.putExtra("landlordUsername", landlordName);
            intent.putExtra("identifier", identifier);
            startActivity(intent);
        }
        else if(identifier.equals("landlord")){
            Intent intent = new Intent(this, LandlordRentActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("landlordUsername", username);
            intent.putExtra("identifier", identifier);
            startActivity(intent);
        }

    }

    public void contacts(View view){
        Intent intent = new Intent(this, contactActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("landlordUsername", landlordName);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }

    public void utilities(View view){
        Intent intent = new Intent(this, utilitiesActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }

    public void lease(View view){
        Intent intent = new Intent(this, leaseActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }
}