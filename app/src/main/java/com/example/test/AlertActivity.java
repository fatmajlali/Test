package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlertActivity extends AppCompatActivity {
    private Firebase Ref;
    EditText alertField;
    Button btnAlert;

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        alertField=findViewById(R.id.alertField);
        btnAlert=findViewById(R.id.btnAlert);

        databaseReference = db.getReference("Alert");
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAlert();
            }
        });

    }

    private void sendAlert() {
        String dataFieldText = alertField.getText().toString();
        String id =  databaseReference.push().getKey();

        if (!TextUtils.isEmpty(dataFieldText)){

            Feedback data = new Feedback(dataFieldText,id);

            databaseReference.child(id).setValue(data);

            Toast.makeText(this,"Alert has been saved",Toast.LENGTH_SHORT).show();

        }
        else {

            Toast.makeText(this,"Please enter your Alert", Toast.LENGTH_SHORT).show();

        }
    }
}