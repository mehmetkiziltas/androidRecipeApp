package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.util.Log;
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

public class KullaniciNeliOlsunAdaptor extends RecyclerView.Adapter<KullaniciNeliOlsunAdaptor.PostHolder>{
    private static final String TAG = "KullaniciNeliOlsunAdapt";

    private ArrayList<String> NeliOlsunList;
    private ArrayList<String> imageViewList;
    private RecyclerViewClickListener listener;

    public KullaniciNeliOlsunAdaptor(ArrayList<String> neliOlsunList, ArrayList<String> imageViewList, RecyclerViewClickListener listener) {
        this.NeliOlsunList = neliOlsunList;
        this.imageViewList = imageViewList;
        this.listener= listener;
    }

    @NonNull
    @Override
    public KullaniciNeliOlsunAdaptor.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_neli_olsun_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciNeliOlsunAdaptor.PostHolder holder, int position) {
        holder.neliOlsun.setText(NeliOlsunList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        Log.d(TAG, "onBindViewHolder: ");
    }
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return NeliOlsunList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView neliOlsun;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
            neliOlsun = itemView.findViewById(R.id.textViewNeliOlsunAdi);
            imageView = itemView.findViewById(R.id.imageViewNeliOlsunResmi);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v,getAdapterPosition());
            }
        }
    }
}
