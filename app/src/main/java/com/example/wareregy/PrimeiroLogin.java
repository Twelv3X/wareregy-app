package com.example.wareregy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PrimeiroLogin extends AppCompatActivity {
    Button continuar;
    EditText password, password2;
    TextView bemvindo;
    Utilizador user = SharedPrefManager.getInstance(this).getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeiro_login);
        continuar = findViewById(R.id.continuar);
        bemvindo = findViewById(R.id.bemvindo);
        bemvindo.setText("Bem vindo "+user.getNome());
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = findViewById(R.id.password);
                password2 = findViewById(R.id.password2);

                mudarPassword(password.getText().toString(), password2.getText().toString(), user.getId());
            }
        });
    }


    public void mudarPassword(String password, String password2, int user_id) {
        if(password.equals(password2)){
            if(password.matches(".*\\d+.*")){
                if(password.toString().length() > 8){

                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://192.168.1.80:3000/mudarpassword";
                    StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            SharedPrefManager.getInstance(getApplicationContext()).setPrimeiroLogin(1);
                            finish();
                            startActivity(new Intent(getApplicationContext(), Menu.class));
                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("user_id", String.valueOf(user_id));
                            params.put("user_password", password);
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
                }else{
                    Toast.makeText(getApplicationContext(), "Palavra-passe tem que conter pelo menos 9 carateres", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Palavra-passe tem que conter pelo menos 1 número", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Palavra-passe não condiz", Toast.LENGTH_SHORT).show();
        }
    }
}