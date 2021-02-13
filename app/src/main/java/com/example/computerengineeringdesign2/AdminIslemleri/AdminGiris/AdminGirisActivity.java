package com.example.computerengineeringdesign2.AdminIslemleri.AdminGiris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.computerengineeringdesign2.AdminIslemleri.AdminActivity;
import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminGirisActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText inputEmail, inputPassword;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_giris);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.inputSinginEmail);
        inputPassword = findViewById(R.id.inputSinginPassword);

//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            Intent intent = new Intent(AdminGirisActivity.this, AdminActivity.class);
//            startActivity(intent);
//            finish();
//        }else{
//            Toast.makeText(getApplicationContext(), "Admin Paneline giriş yapamazsınız!!", Toast.LENGTH_LONG).show();
//        }
    }

    public void passwordForget(View view) {
        Intent intent = new Intent(AdminGirisActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public void singUpClicked(View view) {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.equals("mehmet@gmail.com") && password.equals("123456")) {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(AdminGirisActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Admin Paneline giriş yapamazsınız!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Admin Paneline giriş yapamazsınız!!", Toast.LENGTH_LONG).show();
        }
    }
}