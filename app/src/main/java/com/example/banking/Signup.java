package com.example.banking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Signup extends AppCompatActivity implements RegistrationCallback {
    Button signUp;
    EditText name, phone, mail, pass, confirmPass;
    ImageView quest, show;
    TextView login;
    boolean showPass = false;
//    DBSupport DB;
//    String[] userData;
    FirebaseUser currUser;
    FirebaseAuth mAuth; //Authentication Variable
    FirebaseFirestore fStore; //Firestore Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPass);
        login = findViewById(R.id.login);
        quest = findViewById(R.id.question);
        show = findViewById(R.id.showPass);
        signUp = findViewById(R.id.signup);

        //Get Instance of Firebase Authentication Service
        mAuth = FirebaseAuth.getInstance();

        //Get Instance of Firebase Firestore Service
        fStore = FirebaseFirestore.getInstance();

//        DB = new DBSupport(this);
//        userData = new String[3];

        signUp.setOnClickListener(view -> {
            loginUtils.fieldValidation(name, phone, mail, pass, confirmPass, this, this);
//            //Signup code with custom email and password, consider using this method as it has more free reign control for beginners in firebase. This is also accompanied with a success listener and failure listener
//            mAuth.signInWithEmailAndPassword(mail.getText().toString(), confirmPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    //Toast if it worked
//                    Toast.makeText(getApplicationContext(), "Signup worked", Toast.LENGTH_SHORT).show();
//
//                    //If a user can successfully login we want to create a child node in our firestore that will store other user details, this can be uniquely identified by the userID made by firebase
//
//                    //Create the Url path to store the firestore data make sure you concatenate the Uid at the end.
//                    String storePathUrl = "MobileDistUsers/userData/" + mAuth.getUid();
//
//                    //Create an hashmap of all the user data you want to store
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("Name", name.getText().toString());
//                    map.put("Phone", phone.getText().toString());
//                    map.put("Mail", mail.getText().toString());
//
//                    //Store it using fStore make sure to add Success and failure listeners
//                    fStore.collection(storePathUrl).add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            //Toast if it worked
//                            Toast.makeText(getApplicationContext(), "Firestore Successfully added", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            //Toast if it worked
//                            Toast.makeText(getApplicationContext(), "Firestore failed adding", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    //Toast if it failed
//                    Toast.makeText(getApplicationContext(), "Signup failed " + e, Toast.LENGTH_SHORT).show();
//                }
//            });
        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        });

        quest.setOnClickListener(view -> loginUtils.showPassRequirements(this));

        show.setOnClickListener(view -> {
            loginUtils.togglePasswordVisibility(show, pass, showPass);
            showPass = !showPass; // update state
        });
    }

    @Override
    public void onRegistrationSuccess() {
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, homepage.class);
//        String email = mail.getText().toString().strip().toLowerCase();
//        Cursor res = DB.getUserData(email);
//
//        res.moveToNext();
//        userData[0] = res.getString(0);
//        userData[1] = res.getString(1);
//        userData[2] = email;
//
//
//        intent.putExtra("userdata", userData);
        startActivity(intent);
    }

    @Override
    public void onValidationFailure(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}