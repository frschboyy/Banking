package com.example.banking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

//import java.util.Random;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class loginUtils {
    static credentialVal cred;

//    static DBSupport DB;
//    static Random rand = new Random();

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public static void fieldValidation(EditText mail, EditText pass, Context context, RegistrationCallback callback){
        String mailStr = mail.getText().toString().strip().toLowerCase();
        String passStr = pass.getText().toString();

        if (mailStr.isEmpty() || passStr.isEmpty()){
            callback.onValidationFailure("Empty Field!");
            return;
        }

        cred = new credentialVal(mailStr, passStr);
        if (!cred.validEmail()){
            mail.requestFocus();
            callback.onValidationFailure("Invalid Email!!");
            return;
        }
        if (!cred.validPassword()){
            pass.requestFocus();
            callback.onValidationFailure("Password Fails Requirements!!");
            return;
        }

        // Login Existing User - Firebase Authentication
        mAuth.signInWithEmailAndPassword(mailStr, passStr).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currUser = mAuth.getCurrentUser();
//                db.collection("users")
//                        .get()
//                        .addOnCompleteListener(task1 -> {
//                            if (task1.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task1.getResult()) {
//                                    Log.d("QuerySnapshot", document.getId() + " => " + document.getData());
//                                }
//                            } else {
//                                Log.w("Error reading", "Error getting documents.", task1.getException());
//                            }
//                        });
                // Sign In Success
                if (currUser != null){
                    String uid = currUser.getUid();
                    callback.onRegistrationSuccess();
                }

            } else {
                // If sign up fails, display a message to the user.
                Toast.makeText(context, "Sign in Failed!!", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        else{
            DB = new DBSupport(context);

            Boolean checkUser = DB.checkEmailPassword(mailStr, passStr);

            if(!checkUser){
                Toast.makeText(context, "Incorrect Login Credentials!!", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                Toast.makeText(context, "Login Successful!!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;*/
    }

    public static void fieldValidation(EditText name, EditText phone, EditText mail, EditText pass, EditText confirm, Context context, RegistrationCallback callback){

        String nameStr = name.getText().toString().strip().toUpperCase();
        String phoneStr = phone.getText().toString();
        String mailStr = mail.getText().toString().strip().toLowerCase();
        String passStr = pass.getText().toString();
        String confirmStr = confirm.getText().toString();

        if(nameStr.isEmpty()|phoneStr.isEmpty()|mailStr.isEmpty()|passStr.isEmpty()|confirmStr.isEmpty()){
            callback.onValidationFailure("Empty Field!");
            return;
        }

        cred = new credentialVal(nameStr, phoneStr, mailStr, passStr, confirmStr);
        if (!cred.validName()){
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            callback.onValidationFailure("Invalid Name!!");
            return;
        }
        if (!cred.validEmail()){
            mail.requestFocus();
            callback.onValidationFailure("Invalid Email!!");
            return;
        }
        if (!cred.validPassword()){
            pass.requestFocus();
            callback.onValidationFailure("Password Fails Requirements!!");
            return;
        }
        if (!cred.validConfirm()) {
            confirm.requestFocus();
            callback.onValidationFailure("Passwords Do Not Match!!");
            return;
        }

        // Create User - Firebase Authentication
        mAuth.createUserWithEmailAndPassword(mailStr, passStr).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign up success
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {

                    // Set new user display name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nameStr)
                            // set other profile information here
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d("Updated", "User profile updated.");
                                }
                            });

                    // Create a reference to the user's document in Firestore
                    DocumentReference userRef = fStore.collection("users").document(user.getUid());

                    // Create data object in Firestore
                    Map<String, Object> data = new HashMap<>();
                    data.put("ID", user.getUid());
                    data.put("Name", nameStr);
                    data.put("Email", user.getEmail());
                    data.put("Phone Number", phoneStr);
                    data.put("Balance", 0);

                    // Add a new document with a generated ID
                    userRef.set(data)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Registration", "User data added successfully");
                            }).addOnFailureListener(e -> {
                                Log.w("Error adding", "Error adding document", e);
                            });

                    callback.onRegistrationSuccess();
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(context, "User object not found", Toast.LENGTH_SHORT).show();
                    callback.onRegistrationSuccess();
                }
            } else{
                // If sign up fails, display a message to the user.
                Toast.makeText(context, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

            /* Create User - SQLite
            DB = new DBSupport(context);
            Boolean checkUser = DB.checkEmail(mailStr);

            if(checkUser){
                Toast.makeText(context, "Email Is Associated With Another Account!!", Toast.LENGTH_LONG).show();
                mail.requestFocus();
                return false;
            }

            // generate unique user ID
            boolean exists = true;
            String id = null;
            while(exists){
                id = randID(); // check if ID is associated with an existing user
                exists = DB.checkID(id);
            }

            Boolean insert = DB.insertData(id, nameStr, phoneStr, mailStr, passStr);
            if (insert) {
                Boolean init = DB.initializeAccount(mailStr); // initialize new account
                if (!init) {
                    DB.deleteUser(mailStr);
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            else{
                Toast.makeText(context, "Signup Failed!!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false; */

    public static void showPassRequirements(Context context){
        AlertDialog.Builder req = new AlertDialog.Builder(context);
        String req1 = "-> 8+ characters.\n";
        String req2 = "-> 1+ uppercase letter.\n";
        String req3 = "-> 1+ lowercase letter.\n";
        String req4 = "-> 1+ special character\n";
        String req5 = "-> 1+ digits\n";

        StringBuilder message = new StringBuilder();

        message.append(req1).append(req2).append(req3).append(req4).append(req5);
        req.setMessage(message);
        req.setTitle("Password Requirements!");
        req.setCancelable(true);

        AlertDialog alert = req.create();
        alert.show();
    }

    public static void togglePasswordVisibility(ImageView show, EditText pass, boolean showPass){
        if(!showPass) {
            show.setImageResource(R.drawable.hide);
            pass.setTransformationMethod(null);
        } else {
            show.setImageResource(R.drawable.show);
            pass.setTransformationMethod(new PasswordTransformationMethod());
        }

        // maintain cursor position
        pass.setSelection(pass.getText().length());
    }

    /*/ randomly generates 5 digit alphanumeric string
    public static String randID(){
        // Define the characters allowed in the random string
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randID = new StringBuilder();

        // generate 5 random characters and append them to the string
        for (int i = 0; i < 5; i++) {
            int randIndex = rand.nextInt(characters.length());
            char randChar = characters.charAt(randIndex);
            randID.append(randChar);
        }
        return randID.toString();
    }*/
}
