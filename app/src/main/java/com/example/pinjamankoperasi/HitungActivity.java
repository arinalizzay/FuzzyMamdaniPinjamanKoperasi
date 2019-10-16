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
    private double gaji, g_rendah, g_tinggi, g_sedang;
    private double rule1, rule2, rule3, rule4, rule5, rule6;
    private double z1, z2, z3, z4, z5,z6;
    private double z, zmakspinjam, zminpinjam;

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
        }else if (Double.parseDouble(masakerjax)>=1 && Double.parseDouble(gajix)>0){
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
            alertDialog.setMessage("masa kerja minimal 1 tahun dan gaji tidak boleh 0 kebawah");
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
        if (gaji >=0 && gaji <= 2000000) {
            g_rendah = ((gaji-2000000)/(4000000-2000000));
            g_sedang = 0;
            g_tinggi = 0;
        }else if (gaji > 2000000 && gaji < 4000000){
            g_rendah = (-(gaji - 4000000))/(4000000-2000000);
            g_sedang = ((gaji-2000000)/(4000000-2000000));
            g_tinggi = 0;
        }else if (gaji == 4000000 ){
            g_rendah = 0;
            g_sedang = 1;
            g_tinggi = 0;
        }else if (gaji > 4000000 && gaji < 6000000){
            g_rendah = 0;
            g_sedang = (-(gaji - 6000000))/(6000000-4000000);
            g_tinggi = ((gaji-4000000)/(6000000-4000000));
        }else{
            g_rendah = 0;
            g_sedang = 0;
            g_tinggi=1;
        }
        Log.d("gaji_rendah", ""+g_rendah);
        Log.d("gaji_sedang", ""+g_sedang);
        Log.d("gaji_tinggi", ""+g_tinggi);
    }

    public void fuzzifikasiMASAKERJA(double masakerja){
        if(masakerja>1 && masakerja<= 3){
            masakerja_sebentar =1;
            masakerja_lama = 0;
        }else if(masakerja> 3 && masakerja< 5){
            masakerja_sebentar = (-(masakerja - 5))/(5-3);
            masakerja_lama = (masakerja - 3 )/(5-3);
        }else{
            masakerja_sebentar=0;
            masakerja_lama=1;
        }
        Log.d("masakerja_sebentar", ""+masakerja_sebentar);
        Log.d("masakerja_lama", ""+masakerja_lama);
    }

    public void Rules(){
        rule1   = Math.min(g_rendah, masakerja_lama);
        z1 = (rule1*4000000) + 1000000;//pinjaman sedikit
        //
        rule2   = Math.min(g_rendah, masakerja_sebentar);
        z2 = (rule2 * 4000000)+1000000;//           sedikit
        //
        rule3   = Math.min(g_sedang, masakerja_lama);
        z3 = 5000000 - (rule3 * 4000000);//            banyak
        //
        rule4   = Math.min(g_sedang, masakerja_sebentar);
        z4 = (rule4 * 4000000)+1000000 ;//            sedikit
        //
        rule5    = Math.min(g_tinggi, masakerja_lama);
        z5 = 5000000 - (rule5 * 4000000);//               banyak
        //
        rule6    = Math.min(g_tinggi, masakerja_sebentar);
        z6 = 5000000-(rule6 * 4000000);//              banyak
    }

    public double defuzzifikasi(){
        zmakspinjam = Math.max(rule3, Math.max(rule5, rule6));
        zminpinjam = Math.max(rule1, Math.max(rule2, rule4));

        if (zmakspinjam == rule3 && zminpinjam == rule1) {
            return z = ((zmakspinjam * z3) + (zminpinjam * z1)) / (rule3 + rule1);
        }
        else if (zmakspinjam == rule3 && zminpinjam == rule2) {
            return z = ((zmakspinjam * z3) + (zminpinjam * z2)) / (rule3 + rule2);
        }
        else if(zmakspinjam == rule3 && zminpinjam == rule4) {
            return z = ((zmakspinjam * z3) + (zminpinjam * z4)) / (rule3 + rule4);
        }
        else if (zmakspinjam == rule5 && zminpinjam == rule1) {
            return z = ((zmakspinjam * z5) + (zminpinjam * z1)) / (rule5 + rule1);
        }
        else if(zmakspinjam == rule5 && zminpinjam == rule2) {
            return z = ((zmakspinjam * z5) + (zminpinjam * z2)) / (rule5 + rule2);
        }
        else if (zmakspinjam == rule5 && zminpinjam == rule4) {
            return z = ((zmakspinjam * z5) + (zminpinjam * z4)) / (rule5 + rule4);
        }
        else if(zmakspinjam == rule6 && zminpinjam == rule1) {
            return z = ((zmakspinjam * z6) + (zminpinjam * z1)) / (rule6 + rule1);
        }
        else if (zmakspinjam == rule6 && zminpinjam == rule2) {
            return z = ((zmakspinjam * z6) + (zminpinjam * z2)) / (rule6 + rule2);
        }
        else
            return z = ((zmakspinjam * z6) + (zminpinjam * z4)) / (rule6 + rule4);



    }
}
