package com.example.computerengineeringdesign2.AdminIslemleri.AdminGiris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.computerengineeringdesign2.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    public void PasswordForgetGetBack(View view) {
        onBackPressed();
    }

    public void passwordReset(View view) {

    }
}