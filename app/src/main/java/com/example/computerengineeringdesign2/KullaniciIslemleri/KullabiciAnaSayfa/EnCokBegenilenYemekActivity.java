package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciEnCokBegenilenYemekAdaptor;
import com.example.computerengineeringdesign2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Map;

public class EnCokBegenilenYemekActivity extends AppCompatActivity {

    private KullaniciEnCokBegenilenYemekAdaptor kullaniciEnCokBegenilenYemekAdaptor;

    private RecyclerView recyclerView;

    private ArrayList<String> kategoriAdiListFB;
    private ArrayList<String> imageViewListFB;
    private ArrayList<String> yemekAdiListFB;
    private ArrayList<String> malzemelerListFB;
    private ArrayList<String> yapilisListFB;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_cok_begenilen_yemek);

        firebaseFirestore = FirebaseFirestore.getInstance();

        kategoriAdiListFB = new ArrayList<>();
        imageViewListFB = new ArrayList<>();
        yemekAdiListFB = new ArrayList<>();
        malzemelerListFB = new ArrayList<>();
        yapilisListFB = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewEnCokBegenilenYemek);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDataFormFirestore();
        kullaniciEnCokBegenilenYemekAdaptor = new KullaniciEnCokBegenilenYemekAdaptor(kategoriAdiListFB,imageViewListFB,yemekAdiListFB,malzemelerListFB,yapilisListFB);
        recyclerView.setAdapter(kullaniciEnCokBegenilenYemekAdaptor);
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(EnCokBegenilenYemekActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekKategorileri = (String) data.get("yemekKategorisi");

                        CollectionReference collectionReference = firebaseFirestore.collection(yemekKategorileri);
                        collectionReference.orderBy("begeniSayisi", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(EnCokBegenilenYemekActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                                if (value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String yemekKategori = (String) data.get("yemekKategorisi");
                                        String yemekAdi = (String) data.get("yemekAdiField");
                                        String downloadUrl = (String) data.get("downloadUrl");
                                        String malzemeler = (String) data.get("malzemelerField");
                                        String yapilis = (String) data.get("yemekYapilisField");

                                        kategoriAdiListFB.add(yemekKategori);
                                        imageViewListFB.add(downloadUrl);
                                        yemekAdiListFB.add(yemekAdi);
                                        malzemelerListFB.add(malzemeler);
                                        yapilisListFB.add(yapilis);
                                        kullaniciEnCokBegenilenYemekAdaptor.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}