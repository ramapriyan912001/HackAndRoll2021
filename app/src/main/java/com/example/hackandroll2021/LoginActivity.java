package com.example.hackandroll2021;

import android.app.Activity;
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
import com.example.hackandroll2021.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button signUpButton = findViewById(R.id.Signup);
        final Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = ((EditText) findViewById(R.id.username)).getText().toString();//.getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();;//.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url ="http://192.168.18.21:3306/authenticate?username="+username+"&password="+password;
                final TextView textView = findViewById(R.id.textView);

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.toString()!="false"){
                                // Display the first 500 characters of the response string.
                                textView.setText(response.toString());
                                Intent i = new Intent(getApplicationContext() , ViewProfile.class);
                                i.putExtra("name", username);
                                    i.putExtra("prefs", "NONE");
                                i.putExtra("idd" , Math.round(Float.valueOf(response.toString().split(",")[2])));

                                startActivity(i);}

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VolleyError", error.toString());
                        textView.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = ((EditText) findViewById(R.id.username)).getText().toString();//.getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();;//.getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();;//.getText().toString();


                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url ="http://192.168.18.21:3306/insertProfile?username="+username+"&password="+password+"&email="+email;
                final TextView textView = findViewById(R.id.textView);

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                textView.setText("Works!");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VolleyError", error.toString());
                        textView.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });
    }
}