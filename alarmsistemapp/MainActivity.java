package com.ozge.alarmsistemapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity<st> extends AppCompatActivity {
    EditText edt_Name, edt_phoneNo, edt_who;
    Button ekleBtn, call;
    DatabaseReference databaseReference, embeddedDbLR, embeddedDbMaster, dbNo;

    ListView listViewCanli;
    List<Canli> canliList;
    CanliList adapter;

    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // uygulama açıldığında çalıştırılan metod
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // setContentView metodu çalıştırılarak layout dosyasından ekran tasarımı yüklenir

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Canli").child("Insan");
        embeddedDbLR = FirebaseDatabase.getInstance().getReference("embeddedInformations").child("LRMaster");
        embeddedDbMaster = FirebaseDatabase.getInstance().getReference("embeddedInformations").child("Master");
        dbNo = FirebaseDatabase.getInstance().getReference("Canli").child("Insan").child("canliNo");


        // xml dosyasındaki (tasarımdakileri) bağlıyoruz
        // findViewById() ile XML'de android:id ile isim verdiğimiz öğelere erişiriz.
        edt_Name = findViewById(R.id.edt_Name);
        edt_phoneNo = findViewById(R.id.edt_phoneNo);
        edt_who = findViewById(R.id.edt_who);
        ekleBtn = findViewById(R.id.ekleBtn);
        listViewCanli = findViewById(R.id.listViewCanli);  //listView'i tasarımdakiyle bağlıyoruz
        canliList = new ArrayList<>();

        listViewCanli.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // ListView, verileri liste halinde ekranda göstermemizi sağlayan bir bileşendir.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Canli insan = canliList.get(position);
                Intent intent = new Intent(getApplicationContext(), guncelle_activity.class);
                intent.putExtra("id", insan.getCanliid());
                intent.putExtra("ad", insan.getCanliAd());
                intent.putExtra("numara", insan.getCanliNo());
                intent.putExtra("yakinlik", insan.getCanliWho());
                startActivity(intent);
            }
        });


        embeddedDbLR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot deneme : snapshot.getChildren()) {
                        String alarm = deneme.child("info").getValue().toString();
                        Toast.makeText(MainActivity.this, "" + alarm.toString() + " ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }

                if (snapshot.getValue() != null) { // eğer veri varsa ( null değilse )  // anlık olarak firebasede belirlediğimiz yerin fotoğrafını çekip kontrol ediyoruz (snapshot)
                    // gömülüden yangın bilgisi geldiğini kontrol ettik ve aşağıdaki işlemleri gerçekleştiriyoruz
                    createNotification(); // createNotification fonksiyonunu çağırıyoruz
                    Intent intent7 = new Intent(Intent.ACTION_CALL);
                    String phoneNum1 = adapter.getItem(0).getCanliNo().toString(); // anasayfadaki listeden numarayı çekiyoruz
                    intent7.setData(Uri.parse("tel:" + phoneNum1));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "Please grant permission ", Toast.LENGTH_SHORT).show();
                        requestPermission();
                    } else {
                        startActivity(intent7); // intent7'yi başlat
                    }
                   // Toast.makeText(MainActivity.this, "Firebasedeki veri silindi ", Toast.LENGTH_SHORT).show();
                    embeddedDbLR.removeValue(); // firebasedeki veeriyi siliyoruz.
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // yukarıdakinin aynısı, gömülüdeki diğer sistem için (uzak menzil vs yakın menzil)
        embeddedDbMaster.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot1) {
                if (snapshot1.getValue() != null) {
                    dbNo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            createNotification();
                            Intent intent8 = new Intent(Intent.ACTION_CALL);
                            String phoneNum = adapter.getItem(0).getCanliNo().toString();

                            intent8.setData(Uri.parse("tel:" + phoneNum));

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(MainActivity.this, "Please grant permission ", Toast.LENGTH_SHORT).show();
                                requestPermission();
                            } else {
                                startActivity(intent8);
                            }
                            Toast.makeText(MainActivity.this, "Firebasedeki veri silindi ", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    embeddedDbMaster.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        ekleBtn.setOnClickListener(new View.OnClickListener() { // ekleBtn id'li butonumuza basılıp basılmadığını kontrolediyoruz
            @Override
            public void onClick(View v) {
                kullaniciEkle();
            } // ekleBtn isimli butonumuza basıldıysa KullanıcıEkle isimli fonksiyonumuzu çalıştırıyoruz. Bu fonksiyon altta
        });
    }


    private void createNotification() {
        String channelId = "channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, "example channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Alarm Sistem App") // bildirimde yazmasını istediğin yazının başlığı
                .setContentText("!!!!!! Yangın Alarmı !!!!!!"); // bildirimde yazmasını istediğin yazı
        notificationManager.notify(1, builder.build());
    }
    @Override
    protected void onStart() {
        super.onStart(); // onStart: onCreate metodu çalıştırılıp görsel öğeler oluştuktan sonra çağırılır.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                canliList.clear();
                for (DataSnapshot canliSnapshot : snapshot.getChildren()) {
                    Canli insan = canliSnapshot.getValue(Canli.class);
                    canliList.add(insan);
                }
                adapter = new CanliList(MainActivity.this, canliList);
                listViewCanli.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void kullaniciEkle() { // edt_Name, edt_phoneNo vs. id'lerimizden isim numara bilgilerini çekip, String olarak oluşturduğumuz isim numara vs. değişkenlerine bu bilgileri atıyoruz.
        String isim = edt_Name.getText().toString();
        String numara = edt_phoneNo.getText().toString();
        String yakinlik = edt_who.getText().toString();
        String id = databaseReference.push().getKey();

        Canli insan = new Canli(id, isim, numara, yakinlik);
        databaseReference.child(id).setValue(insan); // database'e mobilde kullanıcı ekle butonuyla eklediğimiz bilgileri gönderiyoruz
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }
}