package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciNeliOlsunAdaptor;
import com.example.computerengineeringdesign2.ExtraAlanlar.KullaniciYemekDetaylariActivity;
import com.example.computerengineeringdesign2.KullaniciIslemleri.KullaniciBuGunNeOlsun.KullaniciNeliOlsunActivity;
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

public class NeliOlsunFragment extends Fragment {
    private static final String TAG = "NeliOlsunFragment";

    private KullaniciNeliOlsunAdaptor kullaniciNeliOlsunAdaptor;
    private KullaniciNeliOlsunAdaptor.RecyclerViewClickListener listener;

    private RecyclerView recyclerView;
    private ArrayList<String> NeliOlsunList;
    private ArrayList<String> downloadsUrlList;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neli_olsun, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        NeliOlsunList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewNeliOlsun);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getDataFormFirestore();
        kullaniciNeliOlsunAdaptor = new KullaniciNeliOlsunAdaptor(NeliOlsunList,downloadsUrlList,listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.reorder_24px));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(kullaniciNeliOlsunAdaptor);

        listener = new KullaniciNeliOlsunAdaptor.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d(TAG, "onClick() called with: view = [" + view + "], position = [" + position + "]");
                Intent intent = new Intent(getContext(), KullaniciNeliOlsunActivity.class);
                intent.putExtra("YemekNeliOlsun",NeliOlsunList.get(position));
                startActivity(intent);
            }
        };

        return view;
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Neli Olsun");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekIcindeNeVar = (String) data.get("yemekIcerigi");
                        String downloadsUrl = (String) data.get("downloadUrl");

                        NeliOlsunList.add(yemekIcindeNeVar);
                        downloadsUrlList.add(downloadsUrl);
                        kullaniciNeliOlsunAdaptor.notifyDataSetChanged();
                        Log.d(TAG, "onEvent: ");
                    }
                }
            }
        });
    }
}