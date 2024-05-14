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

public class FeedbackActivity extends AppCompatActivity {
private Firebase Ref;
 EditText dataField;
 Button btnSend;

 FirebaseDatabase db = FirebaseDatabase.getInstance();

 DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        dataField=findViewById(R.id.dataField);
        btnSend=findViewById(R.id.btnSend);

        databaseReference = db.getReference("Feedback");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

    }

    public void sendData(){
        String dataFieldText = dataField.getText().toString();
        String id =  databaseReference.push().getKey();

        if (!TextUtils.isEmpty(dataFieldText)){

            Feedback feedback = new Feedback(dataFieldText,id);

            databaseReference.child(id).setValue(feedback);

            Toast.makeText(this,"Feedback has been saved",Toast.LENGTH_SHORT).show();

        }
        else {

            Toast.makeText(this,"Please enter your Feedback", Toast.LENGTH_SHORT).show();

        }
    }

}