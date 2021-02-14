package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciNeliOlsunDetayAdaptor extends RecyclerView.Adapter<KullaniciNeliOlsunDetayAdaptor.PostHolder>{

    private ArrayList<String> yemekAdiList;
    private ArrayList<String> kategoriAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> yorumYapanAdiSoyadiList;
    private ArrayList<String> yapilanYorumList;

    private Context mContext;
    private RecyclerViewClickListener listener;

    public KullaniciNeliOlsunDetayAdaptor(ArrayList<String> yemekAdiList, ArrayList<String> kategoriAdiList,
                                          ArrayList<String> imageViewList, ArrayList<String> malzemelerList, ArrayList<String> yapilisList,
                                          ArrayList<String> yorumYapanAdiSoyadiList, ArrayList<String> yapilanYorumList, Context mContext, RecyclerViewClickListener listener) {
        this.yemekAdiList = yemekAdiList;
        this.kategoriAdiList = kategoriAdiList;
        this.imageViewList = imageViewList;
        this.malzemelerList = malzemelerList;
        this.yapilisList = yapilisList;
        this.yorumYapanAdiSoyadiList = yorumYapanAdiSoyadiList;
        this.mContext = mContext;
        this.listener = listener;
        this.yapilanYorumList = yapilanYorumList;
    }

    @NonNull
    @Override
    public KullaniciNeliOlsunDetayAdaptor.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_neli_olsun_yemek_detay_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciNeliOlsunDetayAdaptor.PostHolder holder, int position) {
        try {
            holder.kategoriAdi.setText(kategoriAdiList.get(position));
            holder.yemekAdi.setText(yemekAdiList.get(position));
            holder.malzemeler.setText(malzemelerList.get(position));
            holder.yapilis.setText(yapilisList.get(position));
            holder.yorumYapanAdSoyad.setText(yorumYapanAdiSoyadiList.get(position));
            holder.yorum.setText(yapilanYorumList.get(position));
            Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        } catch (Exception e) {
            holder.kategoriAdi.setText("");
            holder.yemekAdi.setText("");
            holder.malzemeler.setText("");
            holder.yapilis.setText("");
            holder.yorumYapanAdSoyad.setText("");
            holder.yorum.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return yemekAdiList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView kategoriAdi;
        private TextView yemekAdi;

        private EditText malzemeler, yapilis, yorumYapanAdSoyad, yorum;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            kategoriAdi = itemView.findViewById(R.id.textViewKullaniciNeliOlsunKategoriDetay);
            imageView = itemView.findViewById(R.id.imageViewKullaniciNeliOlsunYemekRasmi);
            yemekAdi = itemView.findViewById(R.id.textViewNeliOlsunYemekAdiDetay);
            malzemeler = itemView.findViewById(R.id.editTextTextKullaniciNeliOlsunMalzemelerDetay);
            yapilis = itemView.findViewById(R.id.editTextTextKullaniciNeliOlsunYemekYapilisDetay);
            yorumYapanAdSoyad = itemView.findViewById(R.id.editTextTextKullaniciNeliOlsunYorumYapanAdiSoyadiDetay);
            yorum = itemView.findViewById(R.id.editTextTextKullaniciNeliOlsunYapilanYorumDetay);

        }
    }
}
