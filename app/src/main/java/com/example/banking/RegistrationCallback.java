package com.example.banking;

import com.google.firebase.auth.FirebaseUser;

public interface RegistrationCallback {
    void onRegistrationSuccess();
    void onValidationFailure(String message);
}
