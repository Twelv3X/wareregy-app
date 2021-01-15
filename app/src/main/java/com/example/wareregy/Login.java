package com.example.wareregy;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button buttonLogin;
    EditText user_email, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Menu.class));
        }

        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = findViewById(R.id.user_email);
                user_password = findViewById(R.id.user_password);
                login(user_email.getText().toString(), user_password.getText().toString());
            }
        });
    }

    private Boolean checkBiometricSupport()
    {

        KeyguardManager keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        PackageManager packageManager = this.getPackageManager();

        if (!keyguardManager.isKeyguardSecure()) {
            //notifyUser("Lock screen security not enabled in Settings");
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {

            //notifyUser("Fingerprint authentication permission not enabled");
            return false;
        }

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
        {
            return true;
        }

        return true;
    }
    // Callback da autenticação
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback()
    {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                //notifyUser("Authentication error: " + errString);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode,
                                             CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed()
            {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),"Falhou", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result)
            {
                //notifyUser("Authentication Succeeded");
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Autenticação feita", Toast.LENGTH_LONG).show();

            }
        };
    }
    private CancellationSignal cancellationSignal;

    private CancellationSignal getCancellationSignal()
    {

        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener()
        {
            @Override
            public void onCancel()
            {
                //notifyUser("Cancelled via signal");
            }
        });
        return cancellationSignal;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void authenticateUser()
    {

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).setTitle("Biometric Demo").setSubtitle("Authentication is required to continue").setDescription("This app uses biometric authentication to protect your data.").setNegativeButton("Cancel", this.getMainExecutor(), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // notifyUser("Authentication cancelled");
            }
        }).build();
        biometricPrompt.authenticate(getCancellationSignal(),getMainExecutor(),getAuthenticationCallback());
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void authenticateUser(View view) {
        // Chama o prompt da autenticação
        authenticateUser();

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).setTitle("Biometric Demo").setSubtitle("Authentication is required to continue").setDescription("This app uses biometric authentication to protect your data.").setNegativeButton("Cancel", this.getMainExecutor(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // notifyUser("Authentication cancelled");
            }
        }).build();
        biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(), getAuthenticationCallback());
    }


    public void login(final String user_email, final String user_password) {
        if (user_email == "") {
            Toast.makeText(getApplicationContext(),
                    "Insira o Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user_password == "") {
            Toast.makeText(getApplicationContext(),
                    "Insira a password", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.118:3000/applogin";
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               try {

                   JSONObject obj = new JSONObject(response);
                   Log.d("login", response.toString());
                   if (!response.contains("Login Falhou")){
                   Utilizador user = new Utilizador(
                           obj.getInt("id"),
                           obj.getString("nome"),
                           obj.getString("email"),
                           obj.getInt("login"),
                           obj.getInt("exp"),
                           obj.getInt("nivel"),
                           obj.getInt("minxp"),
                           obj.getInt("maxxp")
                   );

                   //storing the user in shared preferences
                   SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                   finish();
                   startActivity(new Intent(getApplicationContext(), Menu.class));
               }else{
                       Toast.makeText(getApplicationContext(),
                               "Erro na Autenticação", Toast.LENGTH_SHORT).show();
                   }
                   } catch(Exception e){
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
                params.put("user_email", user_email);
                params.put("user_password", user_password);
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
}