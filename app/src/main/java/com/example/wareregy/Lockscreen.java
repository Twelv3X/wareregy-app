package com.example.wareregy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Toast;

public class Lockscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);
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
                Toast.makeText(getApplicationContext(),"Sucesso", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Lockscreen.this, Menu.class);
                Lockscreen.this.startActivity(intent);
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

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).setTitle("Impressão Digital requerida").setSubtitle("Coloque o seu dedo para continuar").setDescription("Esta aplicação usa uma autenticação biométrica para proteger os seus dados.").setNegativeButton("Cancelar", this.getMainExecutor(), new DialogInterface.OnClickListener()
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

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).setTitle("Impressão Digital requerida").setSubtitle("Coloque o seu dedo para continuar").setDescription("Esta aplicação usa uma autenticação biométrica para proteger os seus dados.").setNegativeButton("Cancelar", this.getMainExecutor(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // notifyUser("Authentication cancelled");
            }
        }).build();
        biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(), getAuthenticationCallback());
    }
}