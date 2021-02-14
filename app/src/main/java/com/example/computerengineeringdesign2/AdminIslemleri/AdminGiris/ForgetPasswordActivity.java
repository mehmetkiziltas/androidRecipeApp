package com.example.computerengineeringdesign2.AdminIslemleri.AdminGiris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText userEmail;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        userEmail = findViewById(R.id.inputKullaniciEmail);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void PasswordForgetGetBack(View view) {
        onBackPressed();
    }

    public void passwordReset(View view) {
        String email = userEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgetPasswordActivity.this, "Please Enter Email..!", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ForgetPasswordActivity.this,"Please visit your email",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgetPasswordActivity.this, AdminGirisActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgetPasswordActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}