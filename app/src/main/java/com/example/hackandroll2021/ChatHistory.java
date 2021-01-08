package com.example.hackandroll2021;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.List;

public class ChatHistory extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String[] listItem;
    String [] contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        listView=findViewById(R.id.listView);
        final String name = getIntent().getStringExtra("name");
        textView=findViewById(R.id.textView);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final int idd1 = getIntent().getIntExtra("idd" , 5);
        String url ="http://192.168.18.21:3306/contacts?idd1="+String.valueOf(idd1);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                       listItem  = response.toString().split("#");
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                // TODO Auto-generated method stub
                                String value=adapter.getItem(position);
                                Intent intent = new Intent(getApplicationContext() , ChatScreen.class);
                                intent.putExtra("idd1" , idd1);
                                String[] values = value.split(",");
                                intent.putExtra("idd2", Math.round(Float.valueOf(values[2])));
                                intent.putExtra("user" , values[0]);
                                intent.putExtra("name" , name);

                                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
                contacts= new String[]{"6" };
            }
        });
        //listItem = new String[contacts.length];

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}