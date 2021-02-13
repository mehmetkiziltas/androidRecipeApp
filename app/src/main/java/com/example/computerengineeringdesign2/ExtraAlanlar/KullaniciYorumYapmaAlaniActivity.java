package com.example.computerengineeringdesign2.ExtraAlanlar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa.AnaSayfaActivity;
import com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa.YemekEklemeActivity;
import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class KullaniciYorumYapmaAlaniActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private String gelenKategoriDeger;
    private String gelenYemekAdiDeger;
    private String gelenResimDeger;

    private TextView yemekAdi,kategoriAdi,kullaniciAdiSoyadi;
    private ImageView yemekResmi;
    private EditText yemekYorum;
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_yorum_yapma_alani);
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataYorumlar();

        yemekAdi = findViewById(R.id.textViewYemekYorumYemekAdi);
        kategoriAdi = findViewById(R.id.textViewYemekYorumYemekKategorisi);
        kullaniciAdiSoyadi = findViewById(R.id.textViewYemekYorumYapanAdiSoyadi);
        yemekResmi = findViewById(R.id.imageViewYemekYorumYemekResmi);
        yemekYorum = findViewById(R.id.editTextTextYemekYorum);
        myButton = findViewById(R.id.buttonYemekYorumYap);
        yemekAdi.setText(gelenYemekAdiDeger);
        kategoriAdi.setText(gelenKategoriDeger);
        Picasso.get().load(gelenResimDeger).into(yemekResmi);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> postData = new HashMap<>();
                postData.put("downloadUrl",gelenResimDeger);
                postData.put("useEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                postData.put("yemekKategorisi", gelenKategoriDeger);
                postData.put("yemekAdiField", gelenYemekAdiDeger);
                postData.put("YorumField", yemekYorum.getText().toString());
                postData.put("YorumYapanAdSoyad", kullaniciAdiSoyadi.getText().toString());


                firebaseFirestore.collection("Yorumlar").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(KullaniciYorumYapmaAlaniActivity.this, "Yorumunuz Başarıyla Kaydedildi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(KullaniciYorumYapmaAlaniActivity.this, AnaSayfaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(KullaniciYorumYapmaAlaniActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage().toString());
                    }
                });
            }
        });
    }

    private void getDataYorumlar() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gelenKategoriDeger = bundle.getString("yemekKategorisi");
            gelenYemekAdiDeger = bundle.getString("yemekAdiField");
            gelenResimDeger = bundle.getString("downloadUrl");
        }
    }
}