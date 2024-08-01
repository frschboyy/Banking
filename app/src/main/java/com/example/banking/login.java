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

public class login extends AppCompatActivity implements RegistrationCallback {
    EditText email, password;
    Button login;
    ImageView quest, show;
    TextView signup;
    boolean showPass = false;
    DBSupport DB;
    String[] userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        login = findViewById(R.id.signup);
        quest = findViewById(R.id.question);
        show = findViewById(R.id.showPass);
        signup = findViewById(R.id.login);

        DB = new DBSupport(this);
        userData = new String[3];


        login.setOnClickListener(vew -> {
            loginUtils.fieldValidation(email, password, this, this);

//            //Using in-built signInWithEmailAndPassword function to login users, this is also accompanied with a success listener and failure listener
//            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    //Toast if it worked
//                    Toast.makeText(getApplicationContext(), "Login worked", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    //Toast if it failed
//                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
        });

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(this, Signup.class);
            startActivity(intent);
        });

        quest.setOnClickListener(view -> loginUtils.showPassRequirements(this));

        show.setOnClickListener(view -> {
            loginUtils.togglePasswordVisibility(show, password, showPass);
            showPass = !showPass; // update state
        });
    }

    @Override
    public void onRegistrationSuccess() {
        Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currUser = mAuth.getCurrentUser();
        if(currUser != null){
            Intent intent = new Intent(this, homepage.class);
            startActivity(intent);
        }
    }
}