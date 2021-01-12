package com.example.wareregy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registos extends AppCompatActivity {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    //a list to store all the products
    List<Registo> registoList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registos);
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.registoView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//creating adapter object and setting it to recyclerview

        //initializing the productlist
        registoList = new ArrayList<>();
        //this method will fetch and parse json
        //to display it in recyclerview


        //Datepicker
        mDisplayDate = (TextView) findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Registos.this,
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
                mDisplayDate.setText(day + "/" + month + "/" + year);
                int user_id = 1;
                loadRegistos(user_id, date);
            }
        };

    }

    private void loadRegistos(int user_id, String registo_data) {
        registoList.clear();
         String URL_REGISTOS = "http://192.168.1.80:3000/registos" ;
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("json", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject registo = array.getJSONObject(i);
                                //adding the product to product list
                                registoList.add(new Registo(
                                        registo.getInt("produto_id"),
                                        registo.getString("produto_nome"),
                                        registo.getDouble("produto_peso"),
                                        registo.getString("registo_hora")
                                ));

                            }

                            RegistoAdapter adapter = new RegistoAdapter(Registos.this, registoList);
                            recyclerView.setAdapter(adapter);
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

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                Log.d("RL", String.valueOf(user_id));
                Log.d("RL", String.valueOf(registo_data));
                params.put("user_id", String.valueOf(user_id));
                params.put("registo_data", registo_data);
                return params;
            }
            
        }
        ;

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

}