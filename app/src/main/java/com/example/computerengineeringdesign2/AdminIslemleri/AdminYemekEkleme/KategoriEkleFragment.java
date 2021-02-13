package com.example.computerengineeringdesign2.AdminIslemleri.AdminYemekEkleme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class KategoriEkleFragment extends Fragment {

    private ImageView imageViewAdminKategoriResmi;
    private Bitmap selectedImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private EditText yemekKategori;
    private Uri imageData;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button upload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kategori_ekle, container, false);

        yemekKategori = view.findViewById(R.id.yemekAdminKategoriText);
        upload = view.findViewById(R.id.UploadAdminKategoriButton);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadButton();
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        imageViewAdminKategoriResmi = view.findViewById(R.id.imageViewAdminKategoriResmi);
        imageViewAdminKategoriResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimKategoriEkle();
            }
        });

        return view;
    }

    public void resimKategoriEkle() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentToGalary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGalary,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGalary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGalary,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 20) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageViewAdminKategoriResmi.setImageBitmap(selectedImage);
                }

                selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageData);
                imageViewAdminKategoriResmi.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void UploadButton() {
        if (imageData != null) {
            UUID uuid = UUID.randomUUID();
            final String imageName = "images/" + uuid +".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReferance = FirebaseStorage.getInstance().getReference(imageName);
                    newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String useEmail = firebaseUser.getEmail();
                            String yemekKategorisi = yemekKategori.getText().toString();

                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("downloadUrl",downloadUrl);
                            postData.put("useEmail", useEmail);
                            postData.put("yemekKategorisi", yemekKategorisi);

                            firebaseFirestore.collection("Kategoriler").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Kaydedildi", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage().toString() + "Hata!!", Toast.LENGTH_SHORT).show();
                                    System.out.println(e.getMessage().toString());
                                }
                            });
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getActivity(), "Lütfen Resim Seçin", Toast.LENGTH_LONG).show();
        }
    }
}