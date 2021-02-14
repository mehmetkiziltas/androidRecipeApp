package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciAnaSayfaAdapter;
import com.example.computerengineeringdesign2.ExtraAlanlar.YemekTarifiSecmeActivity;
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

public class AnaSayfaFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private KullaniciAnaSayfaAdapter kullaniciAnaSayfaAdapter;
    private KullaniciAnaSayfaAdapter.RecyclerViewClickListener listener;

    private RecyclerView recyclerView;
    private ArrayList<String> KategoriAdiList;
    private ArrayList<String> downloadsUrlList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        KategoriAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewKullaniciAnaSayfa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getDataFormFirestore();
        kullaniciAnaSayfaAdapter = new KullaniciAnaSayfaAdapter(KategoriAdiList,downloadsUrlList,listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.reorder_24px));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(kullaniciAnaSayfaAdapter);
        listener = new KullaniciAnaSayfaAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), YemekTarifiSecmeActivity.class);
                intent.putExtra("yemekKategorisi",KategoriAdiList.get(position));
                startActivity(intent);
            }
        };
        return view;
    }


    public void getDataFormFirestore() {
        KategoriAdiList.removeAll(KategoriAdiList);
        downloadsUrlList.removeAll(downloadsUrlList);
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String kategoriAdi = (String) data.get("yemekKategorisi");
                        String downloadsUrl = (String) data.get("downloadUrl");

                        KategoriAdiList.add(kategoriAdi);
                        downloadsUrlList.add(downloadsUrl);
                        kullaniciAnaSayfaAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}