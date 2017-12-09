package com.example.brett.landlord_tenant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class createAccount extends AppCompatActivity {

    CheckBox account_landlord;
    CheckBox account_tenant;
    Button createAccount;

    SharedPreferences mPrefs;
    String name;
    String email;
    String phone;
    String pass;
    String passConfirm;

    EditText username;
    EditText emailAddress;
    EditText phoneNumber;
    EditText password;
    EditText passwordConfirm;
    EditText firstName;
    EditText lastName;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    List<Tenant> tenants;
    List<Landlord> landlord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        checkDatabaseReference();
        mPrefs = getSharedPreferences("key", Context.MODE_PRIVATE);

        username = (EditText) findViewById(R.id.account_username);
        emailAddress = (EditText) findViewById(R.id.account_email);
        phoneNumber = (EditText) findViewById(R.id.account_phone);
        password = (EditText) findViewById(R.id.account_password);
        passwordConfirm = (EditText) findViewById(R.id.account_confirm);


        account_landlord = (CheckBox) findViewById(R.id.landlord_checkBox);
        account_tenant = (CheckBox) findViewById(R.id.tenant_checkBox);
        createAccount = (Button) findViewById(R.id.account_create_button);

        account_landlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_landlord.isChecked() || account_tenant.isChecked()){
                    account_tenant.setChecked(false);
                }
            }
        });

        account_tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_landlord.isChecked() || account_tenant.isChecked()){
                    account_landlord.setChecked(false);
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(createAccount.this);

                builder.setTitle("Your information is accurate?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!findType(username.getText().toString())){
                            makeAccount();
                        }
                        Toast.makeText(createAccount.this, "Account created", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor ed = mPrefs.edit();

        username = (EditText) findViewById(R.id.account_username);
        emailAddress = (EditText) findViewById(R.id.account_email);
        phoneNumber = (EditText) findViewById(R.id.account_phone);
        password = (EditText) findViewById(R.id.account_password);
        passwordConfirm = (EditText) findViewById(R.id.account_confirm);
        firstName = (EditText) findViewById(R.id.account_first);
        lastName = (EditText) findViewById(R.id.account_last);

        ed.putString("name", username.getText().toString());
        ed.putString("email", emailAddress.getText().toString());
        ed.putString("phone", phoneNumber.getText().toString());
        ed.putString("pass", password.getText().toString());
        ed.putString("passConfirm", passwordConfirm.getText().toString());

        ed.commit();
    }

    public void  makeAccount(){
        Tenant tenant = new Tenant();
        Landlord landlord = new Landlord();
        username = (EditText) findViewById(R.id.account_username);
        emailAddress = (EditText) findViewById(R.id.account_email);
        phoneNumber = (EditText) findViewById(R.id.account_phone);
        password = (EditText) findViewById(R.id.account_password);
        passwordConfirm = (EditText) findViewById(R.id.account_confirm);
        firstName = (EditText) findViewById(R.id.account_first);
        lastName = (EditText) findViewById(R.id.account_last);


        account_landlord = (CheckBox) findViewById(R.id.landlord_checkBox);
        account_tenant = (CheckBox) findViewById(R.id.tenant_checkBox);
        if(account_tenant.isChecked()){
            if(passwordConfirm.getText().toString() != password.getText().toString()){
                Toast.makeText(this, passwordConfirm.getText().toString() + " " + password.getText().toString(), Toast.LENGTH_SHORT).show();
            }
            else{
                int i = Integer.parseInt(phoneNumber.getText().toString());
                tenant.setmUserName(username.getText().toString());
                tenant.setmEmail(emailAddress.getText().toString());
                tenant.setmPassword(password.getText().toString());
                tenant.setmFirstName(firstName.getText().toString());
                tenant.setmLastName(lastName.getText().toString());
                tenant.setmPhoneNumber(i);

                tenant.updateDatabase();
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
            }
        }
        else if (account_landlord.isChecked()){
            if(!passwordConfirm.getText().toString().equals(password.getText().toString())){
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }
            else{
                int i = Integer.parseInt(phoneNumber.getText().toString());
                landlord.setmUsername(username.getText().toString());
                landlord.setmEmail(emailAddress.getText().toString());
                landlord.setmPassword(password.getText().toString());
                landlord.setmFirstName(firstName.getText().toString());
                landlord.setmLastName(lastName.getText().toString());
                landlord.setmPhoneNumber(i);

                landlord.updateDatabase();
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "No account type selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkDatabaseReference(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("users")){
                    if(dataSnapshot.hasChild("tenants")){
                        getTenantList();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("users")){
                    if(dataSnapshot.hasChild("landlords")){
                        getLandlordList();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTenantList(){
        myRef.child("users").child("tenants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Tenant>> genericTypeIndicator = new GenericTypeIndicator<List<Tenant>>(){};
                tenants = dataSnapshot.getValue(genericTypeIndicator);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getLandlordList(){
        myRef.child("users").child("tenants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Landlord>> genericTypeIndicator = new GenericTypeIndicator<List<Landlord>>(){};
                landlord = dataSnapshot.getValue(genericTypeIndicator);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean findType(String username){
        boolean check = false;
        if(tenants != null){
            if(!tenants.isEmpty()){
                for(int i =0; i<tenants.size();i++){
                    if(username == tenants.get(i).getmUserName()){
                        check = true;
                    }
                }
            }
        }

        if(landlord != null){
            if(!landlord.isEmpty()){
                for(int i =0; i<landlord.size();i++){
                    if(username == landlord.get(i).getmUsername()){
                        check =true;
                    }
                }
            }
        }
        return check;
    }
}
