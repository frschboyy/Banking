package com.example.banking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class balance extends AppCompatActivity {
    ImageView exit;
    TextView amount;

    FirebaseUser currUser;
    FirebaseAuth mAuth; //Authentication Variable
    FirebaseFirestore fStore;
//    String[] userData;
//    DBSupport DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        exit = findViewById(R.id.exit);
        amount =  findViewById(R.id.amount);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
//        DB = new DBSupport(this);

//        Intent intent = getIntent();
//        userData = intent.getStringArrayExtra("userdata");
        exit.setOnClickListener(view -> {
            Intent act = new Intent(this, homepage.class);
//            act.putExtra("userdata", userData);
            startActivity(act);
        });

        if (currUser != null){
            String uid = currUser.getUid();
            DocumentReference userRef = fStore.collection("users").document(uid);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    // Retrieve user data
                    double balance = documentSnapshot.getDouble("Balance");
                    amount.setText(String.valueOf(balance));
                }
            }).addOnFailureListener(e ->{
                Log.e("DocumentSnapshot", "Failed to get user data", e);
            });
        }
//        amount.setText(String.valueOf(DB.checkBalance(userData[2])));
    }
}