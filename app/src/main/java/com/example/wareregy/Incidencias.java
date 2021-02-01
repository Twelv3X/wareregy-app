package com.example.wareregy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Incidencias extends AppCompatActivity {
    ImageView imageView;
    EditText comentario;
    Button enviar;
    Bitmap bitmap;
    ApiService apiService;
    String dadosRegisto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);
        //Buscar as informações do produto em que foi feito o scan
        Intent registo = getIntent();
        dadosRegisto = registo.getStringExtra("registo");
        Log.d("msg", dadosRegisto);

        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        imageView = (ImageView)findViewById(R.id.imageView);
        comentario = findViewById(R.id.comentario);
        enviar = findViewById(R.id.enviar);

        //Aqui é usado Retrofit em vez de Volley pois volley não supporta multipart requests
        initRetrofitClient();
        //Abrir câmera
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        //Enviar para a API a foto e o comentário
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmtr = comentario.getText().toString();
                if(bitmap != null && !cmtr.equals("")){
                    multipartImageUpload();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                }else{
                    Toast.makeText(getApplicationContext(),  "Tire uma foto e descreva a incidência primeiro", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Guardar a foto numa variável bitmap e mostrar a fota na imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);

    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        apiService = new Retrofit.Builder().baseUrl("http://192.168.1.80:3000").client(client).build().create(ApiService.class);
    }


    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            //Retrofit request com a imagem, a informação do produto e o comentário
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody registo = RequestBody.create(MediaType.parse("text/plain"), dadosRegisto);
            RequestBody incidMsg = RequestBody.create(MediaType.parse("text/plain"), comentario.getText().toString());
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            Call<ResponseBody> req = apiService.postImage(body, name, registo, incidMsg);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {

                    }

                    Toast.makeText(getApplicationContext(),  "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
