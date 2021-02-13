package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

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

public class KullaniciAnaSayfaAdapter extends RecyclerView.Adapter<KullaniciAnaSayfaAdapter.PostHolder> {
    private ArrayList<String> kategoriAdiList;
    private ArrayList<String> imageViewList;
    private RecyclerViewClickListener listener;

    public KullaniciAnaSayfaAdapter(ArrayList<String> kategoriAdiList, ArrayList<String> imageViewList, RecyclerViewClickListener listener) {
        this.kategoriAdiList = kategoriAdiList;
        this.imageViewList = imageViewList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_ana_sayfa_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.kategoriAdi.setText(kategoriAdiList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return kategoriAdiList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView kategoriAdi;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
            kategoriAdi = itemView.findViewById(R.id.textViewKategoriAdi);
            imageView = itemView.findViewById(R.id.imageViewKategoriResmi);

        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v,getAdapterPosition());
            }
        }
    }
}
