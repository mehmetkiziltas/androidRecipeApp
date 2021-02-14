package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class KullaniciKayitActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText inputEmail, inputPassword;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_kayit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.inputKullaniciKayitSinginEmail);
        inputPassword = findViewById(R.id.inputKullaniciKayitSinginPassword);
    }

    public void SingInClicked(View view) {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(KullaniciKayitActivity.this, AnaSayfaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(KullaniciKayitActivity.this, "Hata!! " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}