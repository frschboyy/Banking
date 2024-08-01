package com.example.banking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class homepage extends AppCompatActivity {
    ImageView dep, dep2, with, with2, check, check2;
    TextView welcome;
    Intent act;
    Button logout;
//    String[] data;
    FirebaseUser currUser;
    FirebaseAuth mAuth; //Authentication Variable
    FirebaseFirestore fStore; //Firestore Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        dep = findViewById(R.id.deposit);
        dep2 = findViewById(R.id.deposit2);
        with = findViewById(R.id.withdraw);
        with2 = findViewById(R.id.withdraw2);
        check = findViewById(R.id.check);
        check2 = findViewById(R.id.check2);
        welcome = findViewById(R.id.welcome);
        logout = findViewById(R.id.logout);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
//        Intent intent = getIntent();
//
//        if (intent != null && intent.hasExtra("userdata")) {
//            data = intent.getStringArrayExtra("userdata");
//            if (data != null && data.length > 0) {
//                welcome.setText("Welcome " + data[0]);
//            } else {
//                welcome.setText("Welcome");
//            }
//        } else {
//            welcome.setText("Welcome");
//        }
        if (currUser != null){
            String uid = currUser.getUid();
            DocumentReference userRef = fStore.collection("users").document(uid);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    // Retrieve user data
                    String name = documentSnapshot.getString("name");
                    welcome.setText("Welcome " + currUser.getDisplayName());
                }
                else welcome.setText("Welcome");
            }).addOnFailureListener(e ->{
                Log.e("DocumentSnapshot", "Failed to get user data", e);
            });
        }
        else welcome.setText("Welcome");

        logout.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish();
        });

        dep.setOnClickListener(view -> {
            act = new Intent(this,deposit.class);
//            act.putExtra("userdata", data);
            startActivity(act);
        });
        dep2.setOnClickListener(view -> {
            act = new Intent(this,deposit.class);
//            act.putExtra("userdata", data);
            startActivity(act);
        });

        with.setOnClickListener(view -> {
            act = new Intent(this,withdraw.class);
//            act.putExtra("userdata", data);
            startActivity(act);
        });
        with2.setOnClickListener(view -> {
            act = new Intent(this,withdraw.class);
//            act.putExtra("userdata", data);
            startActivity(act);
        });

        check.setOnClickListener(view -> {
            act = new Intent(this,balance.class);
//            act.putExtra("userdata", data);
            startActivity(act);
        });
        check2.setOnClickListener(view -> {
            act = new Intent(this,balance.class);
//            act.putExtra("userdata", data);
            startActivity(act);
        });
    }
}