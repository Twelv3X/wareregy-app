package com.example.wareregy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Scanner extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView timer;
    Utilizador user = SharedPrefManager.getInstance(this).getUser();
    Context ctx;
    TextView multiplicador;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannView = findViewById(R.id.scannerView);
        multiplicador = findViewById(R.id.multiplicador2);
         intent = getIntent();
        long value = Long.parseLong(intent.getStringExtra("timervalue"));
        String multi = intent.getStringExtra("multiplicador");

        multiplicador.setText(multi);
        codeScanner = new CodeScanner(this, scannView);
        timer = findViewById(R.id.timer2);

        new CountDownTimer(value*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timer.setText("0s");
                multiplicador.setText("1.0");
            }
        }.start();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Dividir QR em Strings
                        //Id do Produto
                        int indexNome = result.getText().indexOf("Nome");
                        String idString = result.getText().substring(0,indexNome);
                        idString = idString.trim().replace("Id: ", "");
                        int id = Integer.parseInt(idString);
                        //Nome do Produto
                        int indexPeso = result.getText().indexOf("Peso");
                        String nome = result.getText().substring(indexNome,indexPeso);
                        nome = nome.replace("Nome: ", "");
                        //Peso do Produto
                        int indexLoc = result.getText().indexOf("Localização");
                        String pesoString = result.getText().substring(indexPeso,indexLoc);
                        pesoString = pesoString.trim().replace("Peso: ", "");
                        Double peso = Double.parseDouble(pesoString);
                        //Localização
                        int indexFim = result.getText().length();
                        String loc = result.getText().substring(indexLoc,indexFim);
                        loc = loc.trim().replace("Localização: ", "");
                        //

                        //Data de hoje
                        DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.now();
                        //Hora de hoje em segundos
                        LocalDateTime tempo = LocalDateTime.now();
                        long segundos = Duration.between(tempo.withSecond(0).withMinute(0).withHour(0), tempo).getSeconds();

                        Registo registo = new Registo();
                        registo.setUserId(user.getId());
                        registo.setProdutoId(id);
                        registo.setProdutoNome(nome);
                        registo.setProdutoPeso(peso);
                        registo.setRegistoData(date.format(dFormatter).toString());
                        registo.setRegistoHora(Math.toIntExact(segundos));

                        Log.d("QR",registo.toJSON());

                        int contexto = Integer.parseInt(intent.getStringExtra("contexto"));
                        Log.d("ctx", String.valueOf(contexto));
                        if (contexto == 0){
                            enviarRegisto(registo);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), Incidencias.class);
                            intent.putExtra("registo", registo.toJSON());
                            startActivity(intent);
                        }

                    }
                });

            }
        });
        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Scanner.this, "Camera Permission is not Required", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }

    public void enviarRegisto(Registo registo) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.80:3000/enviarregisto";
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            Log.d("Res", response);
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject obj = array.getJSONObject(0);

                    user.setExp(obj.getInt("user_xp"));
                    user.setNivel(obj.getInt("nivel"));
                    user.setMinXp(obj.getInt("min_xp"));
                    user.setMaxXp(obj.getInt("max_xp"));
                    user.setNrReg(obj.getInt("nRegistos"));
                    Log.d("Res", user.toString());
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                    incrementarMultiplicador();
                    Intent intent = new Intent();
                    intent.putExtra("multiplicador", multiplicador.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(registo.getUserId()));
                params.put("produto_id", String.valueOf(registo.getProdutoId()));
                params.put("registo_data", registo.getRegistoData());
                params.put("registo_hora", String.valueOf(registo.getRegistoHora()));
                params.put("exp", String.valueOf((int)calcularExp(registo.getProdutoPeso(), user.getNrReg())));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    private static double arredondar (double valor, int casas) {
        int tamanho = (int) Math.pow(10, casas);
        return (double) Math.round(valor * tamanho) / tamanho;
    }

    public void incrementarMultiplicador (){
        double mult = Double.parseDouble(multiplicador.getText().toString());
            if(mult <=2.9){
                mult += 0.1;
                multiplicador.setText(String.valueOf(arredondar(mult,1)));
            }


    }

    public double calcularExp(Double peso, int nRegistos){
        double mult = Double.parseDouble(multiplicador.getText().toString());
        int exp = 0;

        if (peso <=5){
            exp = 10;
        }else if (peso <=15){
            exp = 20;
        }else if (peso >15){
            exp = 30;
        }

        if (nRegistos >= 650){
            mult+=1.0;
        }

        return exp*mult;
    }

    public int getTempo(){
        return Integer.parseInt(timer.getText().toString().replace("s",""));
    }

}
