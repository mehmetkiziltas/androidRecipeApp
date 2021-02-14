package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class KullaniciForgetPasswordActivity extends AppCompatActivity {

    private EditText userEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_forget_password);

        userEmail = findViewById(R.id.inputEmail);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void passwordKullaniciReset(View view) {

        String email = userEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(KullaniciForgetPasswordActivity.this, "Please Enter Email..!", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(KullaniciForgetPasswordActivity.this, "Please visit your email", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(KullaniciForgetPasswordActivity.this, KullaniciGirisActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(KullaniciForgetPasswordActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void PasswordForgetGetBack(View view) {

    }
}