package com.example.wareregy;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Duration;
import java.util.Date;

public class Menu extends AppCompatActivity {
    private TextView nome;
    private TextView exp;
    private TextView nivel;
    private Button scanBtn;
    private Button scanBtn2;
    private ProgressBar pBar;
    private TextView timer;
    private ProgressBar objDiario;
    private TextView diarioTxt;
    private TextView regPercent;
    private TextView tRestante;
    private TextView multiplicador;
    CountDownTimer mCountDownTimer = new CountDownTimer(30000, 1000) {

        public void onTick(long millisUntilFinished) {
            timer.setText(millisUntilFinished / 1000 + "s");
        }

        public void onFinish() {
            timer.setText("0s");
            multiplicador.setText("1.0");
        }
    };

    public static final int TEXT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setElevation(0);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Utilizador user = SharedPrefManager.getInstance(this).getUser();
            nome = findViewById(R.id.textNome);
            exp = findViewById(R.id.exp);
            nivel = findViewById(R.id.nivel);
            pBar = findViewById(R.id.progressBar);
            nome.setText(user.getNome());
            objDiario = findViewById(R.id.objDiario_bar);
            diarioTxt = findViewById(R.id.objDiarioTxt);
            regPercent = findViewById(R.id.regPercent);
            tRestante = findViewById(R.id.tRestante);
            multiplicador = findViewById(R.id.multiplicador);
            mCountDownTimer.start();
            atualizarUser(user);

            if(user.getPrimeiroLogin() == 0){
                Intent intent = new Intent(Menu.this, PrimeiroLogin.class);
                startActivity(intent);
                finish();
            }

        } else {
            Intent intent = new Intent(Menu.this, Login.class);
            startActivity(intent);
            finish();
        }

        timer = findViewById(R.id.timer);
        scanBtn = (Button)findViewById(R.id.scanBtn);
        scanBtn2 = (Button)findViewById(R.id.scanBtn2);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner(0);
            }
        });

        scanBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner(1);
            }
        });

    }

        public void launchRegistos (MenuItem item){
            Intent intent = new Intent(this, Registos.class);
            startActivity(intent);
        }

        public String getTempo(){
        return timer.getText().toString().replace("s","");
        }

    private void openScanner(int contexto) {
        Intent intent = new Intent(this, Scanner.class);
        String tempo = getTempo();
        intent.putExtra("timervalue", tempo);
        intent.putExtra("multiplicador", multiplicador.getText().toString());
        intent.putExtra("contexto", String.valueOf(contexto));
        startActivityForResult(intent,TEXT_REQUEST);
    }

    private void atualizarUser(Utilizador user){
        String expLabel = user.getExp() + "/" + user.getMaxXp();
        exp.setText(expLabel);

        String nivelLabel = "Lv. " + String.valueOf(user.getNivel());
        nivel.setText(nivelLabel);

        pBar.setMin(user.getMinXp());
        pBar.setMax(user.getMaxXp());
        pBar.setProgress(user.getExp());

        diarioTxt.setText("Registado "+ user.getNrReg() + " produtos de 650");

        double percent = ((double)user.getNrReg()/650.0)*100;
        regPercent.setText(String.valueOf((int)percent) + "%");
        objDiario.setProgress(user.getNrReg());

        ZoneId z = ZoneId.of( "Europe/Lisbon" );
        ZonedDateTime now = ZonedDateTime.now( z );
        LocalDate tomorrow = now.toLocalDate().plusDays(1);
        ZonedDateTime tomorrowStart = tomorrow.atStartOfDay( z );
        Duration duration = Duration.between( now , tomorrowStart );

        tRestante.setText(String.valueOf(duration.toHours()) + "h restantes");
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String multi = data.getStringExtra("multiplicador");
                multiplicador.setText(multi);
                mCountDownTimer.cancel();
                mCountDownTimer.start();
                Utilizador user = SharedPrefManager.getInstance(this).getUser();
                atualizarUser(user);
            }

        }
    }

}