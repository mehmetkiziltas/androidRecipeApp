package com.example.computerengineeringdesign2.AdminIslemleri;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor.AdminYemekOnayAdaptor;
import com.example.computerengineeringdesign2.ExtraAlanlar.KullaniciYemekDetaylariActivity;
import com.example.computerengineeringdesign2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class YemekOnayFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private RecyclerView recyclerView;
    private ArrayList<String> yemekAdiList;
    private ArrayList<String> downloadsUrlList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> kategoriList;
    private ArrayList<String> yemekIcerikListFB;

    private AdminYemekOnayAdaptor adminYemekOnayAdaptor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_yemek_onay, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        yemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        malzemelerList = new ArrayList<>();
        yapilisList = new ArrayList<>();
        kategoriList = new ArrayList<>();
        yemekIcerikListFB = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewYemekOnay);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getDataFormFirestore();
        adminYemekOnayAdaptor = new AdminYemekOnayAdaptor(yemekAdiList,downloadsUrlList,yemekIcerikListFB,malzemelerList,yapilisList,kategoriList,getContext());
        recyclerView.setAdapter(adminYemekOnayAdaptor);
        return view;
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Onaylanmamıs Listesi");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekAdi = (String) data.get("yemekAdiField");
                        String downloadsUrl = (String) data.get("downloadUrl");
                        String malzemeler = (String) data.get("malzemelerField");
                        String yapilisi = (String) data.get("yemekYapilisField");
                        String kategori = (String) data.get("yemekKategorisi");
                        String yemekIc = (String) data.get("yemekIcerigi");

                        yemekIcerikListFB.add(yemekIc);
                        yemekAdiList.add(yemekAdi);
                        downloadsUrlList.add(downloadsUrl);
                        malzemelerList.add(malzemeler);
                        yapilisList.add(yapilisi);
                        kategoriList.add(kategori);
                        adminYemekOnayAdaptor.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}