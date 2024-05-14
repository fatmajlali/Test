package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private EditText fullName, email, cin, phone;
    private Button btnEdit, btnLogOut;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullName = findViewById(R.id.fullNameP);
        email = findViewById(R.id.emailP);
        cin = findViewById(R.id.cinP);
        phone = findViewById(R.id.phoneP);

        btnEdit = findViewById(R.id.btnEdit);
        btnLogOut = findViewById(R.id.btnLogOut);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        btnLogOut.setOnClickListener( v->{
            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("remember", false);
            editor.apply();
            firebaseAuth.signOut();
            startActivity(new Intent(ProfileActivity.this,SignInActivity.class));
            Toast.makeText(this, "Sign Out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnEdit.setOnClickListener(v-> {
            fullName.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);
            btnEdit.setText("Save");


            btnEdit.setOnClickListener(v1 -> {
                String fullNameS = fullName.getText().toString().trim();
                String phoneS = phone.getText().toString().trim();
                databaseReference.child("fullname").setValue(fullNameS);
                databaseReference.child("phone").setValue(phoneS);
                Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            });
            databaseReference = firebaseDatabase.getReference().child("Users").child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fullName.setText(snapshot.child("fullname").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    cin.setText(snapshot.child("cin").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            });

});
}};

