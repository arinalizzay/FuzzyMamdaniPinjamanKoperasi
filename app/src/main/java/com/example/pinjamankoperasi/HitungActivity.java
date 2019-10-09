package com.example.pinjamankoperasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HitungActivity extends AppCompatActivity {
    private double masakerja, masakerja_sebentar, masakerja_lama;
    private double gaji, g_rendah, g_tinggi;
    private double rule1, rule2, rule3, rule4;
    private double z1, z2, z3, z4;
    private double z;

    EditText editTextMasaKerja;
    EditText editTextGaji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitung);

        editTextMasaKerja = findViewById(R.id.editTextMasaKerja);
        editTextGaji = findViewById(R.id.editTextGaji);
    }

    public void onClick(View v){
        String masakerjax = editTextMasaKerja.getText().toString();
        String gajix = editTextGaji.getText().toString();
        if(masakerjax.isEmpty()){
            Toast.makeText(this,"Please fill in the form",Toast.LENGTH_SHORT).show();
        }else if(gajix.isEmpty()){
            Toast.makeText(this,"Please fill in the form",Toast.LENGTH_SHORT).show();
        }else if (Double.parseDouble(masakerjax)>=2){
            masakerja = Double.parseDouble(masakerjax);
            gaji = Double.parseDouble(gajix);
            double hasil = hitung(masakerja, gaji);
            Intent intent = new Intent(this, OutputActivity.class);
            intent.putExtra("hasil", hasil);
            intent.putExtra("masakerja", masakerja);
            intent.putExtra("gaji", gaji);
            startActivity(intent);
            finish();
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Syarat tidak memenuhi");
            alertDialog.setMessage("masa kerja minimal 2 tahun");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }

    public double hitung(double masakerja, double gaji){
        fuzzifikasiMASAKERJA(masakerja);
        fuzzifikasiPenghasilan(gaji);
        Rules();
        return defuzzifikasi();
    }

    public void fuzzifikasiPenghasilan (double gaji){
        if(gaji <= 2000000){
            g_rendah=1;
            g_tinggi=0;
        }else if (gaji >= 2000000 && gaji <= 6000000){
            g_rendah = (6000000-gaji) / (6000000-2000000);
            g_tinggi = (gaji-2000000/(6000000-2000000));
        }else{
            g_rendah =0;
            g_tinggi=1;
        }
        Log.d("gaji_rendah", ""+g_rendah);
        Log.d("gaji_tinggi", ""+g_tinggi);
    }

    public void fuzzifikasiMASAKERJA(double masakerja){
        if(masakerja <= 2){
            masakerja_sebentar=1;
            masakerja_lama=0;
        }else if(masakerja>=2 && masakerja<=4){
            masakerja_sebentar =(4-masakerja)/(4-2);
            masakerja_lama = (masakerja-2)/(4-2);
        }else{
            masakerja_sebentar=0;
            masakerja_lama=1;
        }
        Log.d("masakerja_sebentar", ""+masakerja_sebentar);
        Log.d("masakerja_lama", ""+masakerja_lama);
    }

    public void Rules(){
        // IF Gaji Tinggi dan Masa Kerja Lama then Besar Peminjaman Banyak
        rule1   = Math.min(g_tinggi, masakerja_lama);
        z1 = 4000000 - (rule1 * (4000000 - 2000000));
        // IF Gaji Rendah dan Masa Kerja Sebentar then Besar Peminjaman Sedikit
        rule2   = Math.min(g_rendah, masakerja_sebentar);
        z2 = 2000000 + (rule2 * 4000000);
        // IF Gaji Rendah dan Masa Kerja Lama then Besar Besar Peminjaman Sedikit
        rule3   = Math.min(g_rendah, masakerja_lama);
        z3 = 2000000 + (rule3 * 4000000);
        // IF Gaji Tinggi dan Masa Kerja Sebentar then Besar Besar Peminjaman Banyak
        rule4   = Math.min(g_tinggi, masakerja_sebentar);
        z4 = 4000000 - (rule4 * (4000000 - 2000000));
    }

    public double defuzzifikasi(){
        return z = ((rule1*z1)+(rule2*z2)+(rule3*z3)+(rule4*z4))/(rule1+rule2+rule3+rule4);
    }
}
