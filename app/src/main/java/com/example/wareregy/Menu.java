package com.example.wareregy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    TextView nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setElevation(0);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Utilizador user = SharedPrefManager.getInstance(this).getUser();
            nome = findViewById(R.id.textNome);
            nome.setText(user.getNome());
        } else {
            Intent intent = new Intent(Menu.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

        public void launchRegistos (MenuItem item){
            Intent intent = new Intent(this, Registos.class);
            startActivity(intent);
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
}