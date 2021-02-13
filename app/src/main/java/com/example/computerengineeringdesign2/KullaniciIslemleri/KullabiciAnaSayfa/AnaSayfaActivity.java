package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.computerengineeringdesign2.AdminIslemleri.AdminGiris.AdminGirisActivity;
import com.example.computerengineeringdesign2.AdminIslemleri.AdminYemekEkleme.YemekEkleFragment;
import com.example.computerengineeringdesign2.KullaniciIslemleri.KullaniciBuGunNeOlsun.BuGunFragment;
import com.example.computerengineeringdesign2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnaSayfaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAnaSayfa, recyclerViewBuGun, recyclerViewFavorilerim, recyclerViewNeliOlsun;
    private BottomNavigationView bottomNavigationViewMainNav;
    private NavigationView navigationViewLeftMenu;
    private FrameLayout frameLayout;
    private AnaSayfaFragment anaSayfaFragment;
    private BuGunFragment buGunFragment;
    private FavorilerimActivity favorilerimFragment;
    private NeliOlsunFragment neliOlsunFragment;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseFirestore = FirebaseFirestore.getInstance();
        navigationViewLeftMenu = findViewById(R.id.nav_left_menu);

        bottomNavigationViewMainNav = findViewById(R.id.main_nav);
        frameLayout = findViewById(R.id.MainframeLayout);

        anaSayfaFragment = new AnaSayfaFragment();
        buGunFragment = new BuGunFragment();
        favorilerimFragment = new FavorilerimActivity();
        neliOlsunFragment = new NeliOlsunFragment();
        setFragment(anaSayfaFragment);

        bottomNavigationViewMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_Ana_Sayfa:
                        setFragment(anaSayfaFragment);
                        return true;
                    case R.id.nav_Bu_Gun:
                        setFragment(buGunFragment);
                        return true;
                    case R.id.nav_neli_olsun:
                        setFragment(neliOlsunFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        navigationViewLeftMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_favorilerim:
                        Intent intentFavorilerim = new Intent(AnaSayfaActivity.this, FavorilerimActivity.class);
                        startActivity(intentFavorilerim);
                        return true;
                    case R.id.yeni_yemek_ekle:
                        Intent intent = new Intent(AnaSayfaActivity.this, YemekEklemeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.admin_girisi:
                        Intent intentadmin = new Intent(AnaSayfaActivity.this, AdminGirisActivity.class);
                        startActivity(intentadmin);
                        return true;
                    case R.id.en_cok_begenilen_yemek:
                        Intent intentEnCokBegenilenYemek = new Intent(AnaSayfaActivity.this,EnCokBegenilenYemekActivity.class);
                        startActivity(intentEnCokBegenilenYemek);
                        return true;
                    case R.id.nav_kullanici_cikis_yap:
                        FirebaseAuth.getInstance().signOut();
                        Intent intentKullaniciGiris = new Intent(AnaSayfaActivity.this,KullaniciGirisActivity.class);
                        startActivity(intentKullaniciGiris);
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.MainframeLayout, fragment);
        fragmentTransaction.commit();
    }
}