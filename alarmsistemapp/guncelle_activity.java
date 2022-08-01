package com.ozge.alarmsistemapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class guncelle_activity extends AppCompatActivity {

    TextView text_guncelle_ad, text_guncelle_no, text_guncelle_who;
    EditText edit_guncelle_ad, edit_guncelle_no, edit_guncelle_who;
    Button guncelle_btn, sil_btn;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guncelle);

        text_guncelle_ad = findViewById(R.id.guncelle_text_ad);
        text_guncelle_no = findViewById(R.id.guncelle_text_no);
        text_guncelle_who = findViewById(R.id.guncelle_text_who);
        edit_guncelle_ad = findViewById(R.id.edit_guncelle_ad);
        edit_guncelle_no = findViewById(R.id.edit_guncelle_no);
        edit_guncelle_who = findViewById(R.id.edit_guncelle_who);
        guncelle_btn = findViewById(R.id.guncelle_btn);
        sil_btn = findViewById(R.id.sil_btn);


        Intent intent = getIntent();
        String gelenAd = intent.getStringExtra("ad");
        String gelenNo = intent.getStringExtra("numara");
        String gelenWho = intent.getStringExtra("yakinlik");
        String gelenId = intent.getStringExtra("id");

        String id = gelenId;

        text_guncelle_ad.setText(gelenAd);
        text_guncelle_no.setText(gelenNo);
        text_guncelle_who.setText(gelenWho);

        // Aşağıda guncelle_btn adlı düğmeye setOnClickListener metodu kullanılarak düğmeye basıldığında harekete geçen bir eylem eklenmiştir.
        // Kullanıcı düğmeye bastığında alttaki metodlar harekete geçerek ve database'deki (google firebaseden) bilgiler yeni yazılan bilgiler ile güncellenecektir
        guncelle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Canli").child("Insan").child(id);

                String ad = edit_guncelle_ad.getText().toString().trim();
                String num = edit_guncelle_no.getText().toString().trim();
                String kim = edit_guncelle_who.getText().toString().trim();

                Canli insan = new Canli(id, ad, num, kim);
                databaseReference.setValue(insan);
                Toast.makeText(guncelle_activity.this, "Guncelleme islemi basarili", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(guncelle_activity.this, MainActivity.class);
                startActivity(intent1);
            }
        });


        // Aşağıda sil_btn adlı düğmeye setOnClickListener metodu kullanılarak düğmeye basıldığında harekete geçen bir eylem eklenmiştir.
        // Kullanıcı düğmeye bastığında alttaki metodlar harekete geçerek ve database'den (google firebaseden) bilgiler silinecektir.
        sil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ad = edit_guncelle_ad.getText().toString().trim();
                String num = edit_guncelle_no.getText().toString().trim();
                String kim = edit_guncelle_who.getText().toString().trim();

                databaseReference = FirebaseDatabase.getInstance().getReference("Canli").child("Insan").child(id);
                databaseReference.removeValue();
                Intent intent2 = new Intent(guncelle_activity.this, MainActivity.class);
                startActivity(intent2);
                Toast.makeText(guncelle_activity.this, "Kişi silindi ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}