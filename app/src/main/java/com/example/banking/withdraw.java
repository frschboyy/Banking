package com.example.banking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class withdraw extends AppCompatActivity {
    ImageView exit;
    EditText amount;
    Button withdraw;

    FirebaseUser currUser;
    FirebaseAuth mAuth; //Authentication Variable
    FirebaseFirestore fStore; //Firestore
//    String[] userData;
//    DBSupport DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        exit = findViewById(R.id.exit);
        amount = findViewById(R.id.amount);
        withdraw = findViewById(R.id.with);

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

        withdraw.setOnClickListener(view -> {
            String value = amount.getText().toString();
            if (value.isEmpty()) {
                Toast.makeText(this, "Empty Field!!", Toast.LENGTH_SHORT).show();
            } else {
                double val = Double.parseDouble(value);

                if (currUser != null) {
                    String uid = currUser.getUid();
                    DocumentReference userRef = fStore.collection("users").document(uid);

                    // Update user balance
                    userRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            double balance = documentSnapshot.getDouble("Balance");
                            // check if withdrawal is possible
                            if (val > balance) {
                                Toast.makeText(this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
                            }
                            else if ( val <= 0){
                                Toast.makeText(this, "Invalid Amount. Try Again", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                balance = balance - val; // Update value
                                // Update In Firestore
                                Map<String, Object> update = new HashMap<>();
                                update.put("Balance", balance);

                                userRef.update(update).addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Withdrawal Registered", Toast.LENGTH_SHORT).show();
                                    Log.d("FirestoreUpdate", "Document successfully updated");
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(this, "Withdrawal Failed", Toast.LENGTH_SHORT).show();
                                    Log.e("FirestoreUpdate", "Error with update", e);
                                });
                            }
                            amount.setText("");
                            amount.requestFocus();
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("DocumentSnapshot", "Failed to get user data", e);
                    });
                }
//                Boolean check = DB.withdraw(val, userData[2]);
//                if(check)
//                    Toast.makeText(this, "Withdrawal Made!!",Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}