package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciNeliOlsunYemekSecmeAdaptor extends RecyclerView.Adapter<KullaniciNeliOlsunYemekSecmeAdaptor.PostHolder>{

    private ArrayList<String> yemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> yemekKategorisiList;

    private Context mContext;
    private RecyclerViewClickListener listener;

    public KullaniciNeliOlsunYemekSecmeAdaptor(ArrayList<String> yemekAdiList, ArrayList<String> imageViewList, ArrayList<String> yemekKategorisiList, Context mContext, RecyclerViewClickListener listener) {
        this.yemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        this.yemekKategorisiList = yemekKategorisiList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KullaniciNeliOlsunYemekSecmeAdaptor.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_neli_olsun_yemek_secme_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciNeliOlsunYemekSecmeAdaptor.PostHolder holder, int position) {
        holder.yemekAdi.setText(yemekAdiList.get(position));
        holder.yemekKategorisi.setText(yemekKategorisiList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
    }
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return yemekAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView yemekAdi,yemekKategorisi;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
            yemekKategorisi = itemView.findViewById(R.id.textViewKullaniciNeliOlsunYemekKategoriAdi);
            yemekAdi = itemView.findViewById(R.id.textViewKullaniciNeliOlsunYemekSecmeAdi);
            imageView = itemView.findViewById(R.id.imageViewKullaniciNeliOlsunYemekSecmeResmi);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, getAdapterPosition());
            }
        }
    }
}
