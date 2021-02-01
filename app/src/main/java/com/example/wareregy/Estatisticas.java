package com.example.wareregy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

public class Estatisticas extends AppCompatActivity {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Utilizador user = SharedPrefManager.getInstance(this).getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        //Atribuição de uma variável ao datepicker
        //onCLick: Abre o calendário
        //onDateSet: Gurda numa variável a data e envia a data e o id do utilziador para o loadEstatisticas()
        mDisplayDate = (TextView) findViewById(R.id.tvDate2);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Estatisticas.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = year + "-" + month + "-" + day;
                mDisplayDate.setText(day + "/" + month + "/" + year); //Mostrar a data selecionada no ecrã
                int user_id = user.getId(); // id do utilizador que fez o login
                loadEstatisticas(user_id, date);
            }
        };

    }

    //Este método faz uma request á API com o id do utilizador e a data como parâmetros.
    //A API responde com os registos desse utilizador para essa data e é construido um gráfico de barras com os registos para cada hora desse dia.
    private void loadEstatisticas(int user_id, String registo_data) {

        String URL_REGISTOS = "http://192.168.1.80:3000/getestatisticas" ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("json", response);
                            JSONArray array = new JSONArray(response); //Array com os registos devolvidos pela API

                            BarChart barChart = (BarChart) findViewById(R.id.barchart); //View do gráfico de barras
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            BarDataSet bardataset = new BarDataSet(entries, "Número de Registos");
                            ArrayList<String> labels = new ArrayList<String>();

                            //A API devolve a hora (x) e o número de registos efetuados nessa hora (y)
                            //Este loop vai passar por todos os registos do array e atribuir o x(labels) e o y(entries) ao gráfiico.
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject registo = array.getJSONObject(i);
                                entries.add(new BarEntry(registo.getInt("total"), i));
                                labels.add(String.valueOf(registo.getInt("hora"))+"h");

                            }

                            //Customização do gráfico
                            BarData data = new BarData(labels, bardataset);
                            barChart.setData(data);
                            barChart.setDescription("");
                            barChart.getLegend().setEnabled(false);
                            bardataset.setColors(Collections.singletonList(Color.parseColor("#6200EE")));
                            bardataset.setValueFormatter(new DefaultValueFormatter(0));
                            barChart.animateY(5000);
                            bardataset.setValueTextSize(18);
                            YAxis yaxis = barChart.getAxisLeft();
                            YAxis yaxis2 = barChart.getAxisRight();
                            XAxis xaxis = barChart.getXAxis();
                            yaxis2.setEnabled(false);
                            yaxis.setTextSize(18);
                            xaxis.setTextSize(18);
                            xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }){
            //Parâmetros da Request do Volley
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                Log.d("RL", String.valueOf(user_id));
                Log.d("RL", String.valueOf(registo_data));
                params.put("user_id", String.valueOf(user_id));
                params.put("registo_data", registo_data);
                return params;
            }

        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}