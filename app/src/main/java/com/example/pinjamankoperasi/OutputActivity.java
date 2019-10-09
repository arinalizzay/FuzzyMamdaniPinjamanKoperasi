package com.example.pinjamankoperasi;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
//import android.icu.text.NumberFormat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.text.NumberFormat;

public class OutputActivity extends AppCompatActivity {

    TextView textViewPinjaman;
    TextView textViewMasaKerja;
    TextView textViewGaji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        textViewPinjaman = findViewById(R.id.textViewPinjaman);
        textViewMasaKerja = findViewById(R.id.textViewMasaKerja);
        textViewGaji = findViewById(R.id.textViewGaji);

        int pinjaman = (int) getIntent().getDoubleExtra("hasil", 0);
        int gaji = (int) getIntent().getDoubleExtra("gaji", 0);
        double masaKerja = (int) getIntent().getDoubleExtra("masa kerja", 0);

        String strPinjaman = numberFormat.format(pinjaman);
        textViewPinjaman.setText("Rp. "+strPinjaman);

        String strGaji = numberFormat.format(gaji);
        textViewGaji.setText("Gaji : Rp. "+strGaji);
        textViewMasaKerja.setText("Masa Kerja : "+masaKerja+"Tahun");
    }

    public void onClick (View v){
        Intent intent = new Intent(this, HitungActivity.class);
        startActivity(intent);
        finish();
    }
}
