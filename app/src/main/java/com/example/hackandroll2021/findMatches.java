package com.example.hackandroll2021;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//Layers of Matching
//1. Time Range
//2. Price Range
//3. Dietary Preferences
//4. Cuisine
//5. Randomise
public class findMatches extends AppCompatActivity{
    public static String time_range;
    public static String cuisine_choice;
    public String max_price;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_matches);
        //Time Ranges
        final Spinner times = (Spinner) findViewById(R.id.timeSpinner);
        times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] options = getResources().getStringArray(R.array.timeRanges);
                time_range = options[position];
                Log.i("logg" , "times: " +time_range);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> time_adapter = ArrayAdapter.createFromResource(this,
                R.array.timeRanges, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        times.setAdapter(time_adapter);
        final EditText price = findViewById(R.id.priceLimit);

        //Cuisine Array
        final Spinner cuisines = (Spinner) findViewById(R.id.cuisineSpinner);
        cuisines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] options = getResources().getStringArray(R.array.cuisineArray);
                cuisine_choice = options[position];
                Log.i("logg" , "cusine: " +cuisine_choice);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> cuisine_adapter = ArrayAdapter.createFromResource(this,
                R.array.cuisineArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        cuisine_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cuisines.setAdapter(cuisine_adapter);
        Button exit = (Button) findViewById(R.id.backButton);
        Button next = (Button) findViewById(R.id.nextButton);
        final int idd1 = getIntent().getIntExtra("idd" , 7);
//        final Intent leave = new Intent(findMatches.this, ViewProfile.class);
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(leave);
//            }
//        });
//        final Intent next_page = new Intent(findMatches.this, FindMatchActivity.class);//Next Process goes here);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                max_price = price.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://192.168.18.21:3306/findmatch?idd="+String.valueOf(idd1)+"&cuisine="+cuisine_choice+"&timerange="+time_range+"&pricelim="+max_price;

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                    Log.i("logg" , "resp "+response.toString());
                                Intent i  =  new Intent(getApplicationContext(), ChatHistory.class);
                                i.putExtra("match" , response.toString());
                                i.putExtra("sendmsg" , 1);
                                startActivity(i);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent i  =  new Intent(getApplicationContext(), ChatHistory.class);
                        i.putExtra("match" , "keep waiting");
                        i.putExtra("sendmsg" , 0);
                        startActivity(i);
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });
    }
}
