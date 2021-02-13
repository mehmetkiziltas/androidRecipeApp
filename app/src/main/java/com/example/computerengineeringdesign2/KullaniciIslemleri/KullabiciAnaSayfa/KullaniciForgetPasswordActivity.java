package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.computerengineeringdesign2.R;

public class KullaniciForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_forget_password);
    }

    public void passwordReset(View view) {
    }

    public void PasswordForgetGetBack(View view) {
        onBackPressed();
    }
}