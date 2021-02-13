package com.example.computerengineeringdesign2.AdminIslemleri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.computerengineeringdesign2.AdminIslemleri.AdminYemekEkleme.AdminYorumlarActivity;
import com.example.computerengineeringdesign2.AdminIslemleri.AdminYemekEkleme.KategoriEkleFragment;
import com.example.computerengineeringdesign2.AdminIslemleri.AdminYemekEkleme.YemekEkleFragment;
import com.example.computerengineeringdesign2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewMainAdminNav;
    private FrameLayout frameLayout;
    private NavigationView navigationView;

    private KategoriEkleFragment kategoriEkleFragment;
    private YemekEkleFragment yemekEkleFragment;
    private YemekOnayFragment yemekOnayFragment;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseFirestore = FirebaseFirestore.getInstance();
        bottomNavigationViewMainAdminNav = findViewById(R.id.Admin_main_nav);
        frameLayout = findViewById(R.id.MainframeLayout);
        navigationView = findViewById(R.id.nav_admin_left_menu);


        kategoriEkleFragment = new KategoriEkleFragment();
        yemekEkleFragment = new YemekEkleFragment();
        yemekOnayFragment = new YemekOnayFragment();
        setFragment(kategoriEkleFragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adminYemekYorumlari:
                        Intent intent = new Intent(AdminActivity.this, AdminYorumlarActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.admin_neli_olsun:
                        Intent intentNeliOlsun = new Intent(AdminActivity.this,NeliOlsunBelirlemeActivity.class);
                        startActivity(intentNeliOlsun);
                        return true;
                    default:
                        return false;
                }
            }
        });

        bottomNavigationViewMainAdminNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.admin_kategori_ekle:
                        setFragment(kategoriEkleFragment);
                        return true;
                    case R.id.admin_yemek_ekle:
                        setFragment(yemekEkleFragment);
                        return true;
                    case R.id.admin_yemek_onayla:
                        setFragment(yemekOnayFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.AdminMainframeLayout, fragment);
        fragmentTransaction.commit();
    }
}