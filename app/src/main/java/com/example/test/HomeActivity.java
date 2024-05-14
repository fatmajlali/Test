package com.example.test;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.Adapter.AdviseAdapter;
import com.example.test.Domain.AdviseDomain;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  {
private ImageView btnSignOut;
private ImageView btnFeedback;
private ImageView btnAlert;
private ImageView btnProfile;
    FirebaseAuth mAuth;

    private RecyclerView.Adapter adapterAdvise;
    private RecyclerView recyclerViewAdvise;

    private CardView ScanCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        btnFeedback = findViewById(R.id.btnFeedback);
        btnAlert = findViewById(R.id.btnAlert);
        btnProfile = findViewById(R.id.btnProfile);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {

                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
btnAlert.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v1) {

        Intent intent = new Intent(HomeActivity.this, AlertActivity.class);
        startActivity(intent);
    }
});
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();
            }
        });
    }


    private void signOutUser() {
        Intent StartActivity = new Intent(HomeActivity.this,SignInActivity.class);
        StartActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(StartActivity);
        finish();
    }



    @Override
    protected void onStart()
    {
        super.onStart();


        //RecycleView Call
        initRecyclerView();

        ScanCard = findViewById(R.id.cardViewScan);
        ScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a SR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
        if(intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){

               // Toast.makeText(HomeActivity.this, contents,Toast.LENGTH_LONG).show();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                reference.orderByChild("Phone").equalTo(contents.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean phoneNumberExists = false;
                        for(DataSnapshot datas: dataSnapshot.getChildren()){
                            if (datas.exists()) {
                                // Phone number exists in at least one document within the "users" node
                                phoneNumberExists = true;
                                break; // Exit loop since we found a match
                            }
                        }
                        if (phoneNumberExists) {
                            // Phone number exists in at least one document within the "users" node
                            Toast.makeText(HomeActivity.this, "Phone number "+contents+" exists", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Phone number exists in at least one document within the 'users' node.");
                        } else {
                            // Phone number does not exist in any document within the "users" node
                            Toast.makeText(HomeActivity.this, "Phone number does not exist in any document within the 'users' node.", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Phone number does not exist in any document within the 'users' node.");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error

                        Log.d(TAG, "Error querying database: " + databaseError.getMessage());
                    }
                });



            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void initRecyclerView() {
        ArrayList<AdviseDomain> items = new ArrayList<>();
        items.add(new com.example.test.Domain.AdviseDomain("The standard Lorem Ipsum passage, used since the 1500s", "is simply dummy text of the printing and typesetting industry", "trends"));
        items.add(new AdviseDomain("Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "trends2"));
        items.add(new AdviseDomain("Lorem Ipsum", "is simply dummy text of the printing and typesetting industry", "trends"));

        recyclerViewAdvise = findViewById(R.id.RecyclerViewAdvise);
        recyclerViewAdvise.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterAdvise = new AdviseAdapter((items));
        recyclerViewAdvise.setAdapter(adapterAdvise);
    }


}