package com.example.hackandroll2021;


import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ChatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        final TextView msgs = findViewById(R.id.msgs);
        final int idd1 = getIntent().getIntExtra("idd1",7);
        final int idd2 = getIntent().getIntExtra("idd2",6);
        final String user = getIntent().getStringExtra("user");
        final String name = getIntent().getStringExtra("name");
        final EditText input = findViewById(R.id.input);
        final Button go = findViewById(R.id.send);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.18.21:3306/getChat?idd1=" + String.valueOf(idd1) + "&idd2=" + String.valueOf(idd2);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        msgs.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("errr" , error.toString());
                go.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


        go.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = null;
                try {
                    url = "http://192.168.18.21:3306/updateChat?idd1=" + String.valueOf(idd1) + "&idd2=" + String.valueOf(idd2) + "&msg=" + URLEncoder.encode(msgs.getText().toString()+"\n",StandardCharsets.UTF_8.toString())+ URLEncoder.encode(input.getText().toString(), StandardCharsets.UTF_8.toString());
                    input.setText("");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ;

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                msgs.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        go.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });


    }
}
