package com.example.hackandroll2021;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.w3c.dom.Text;

public class ViewProfile extends AppCompatActivity {
    String name;
    String prefs;

    public void setDietaryPreference(String preference){
        TextView diet_view = (TextView) findViewById(R.id.profileDiet);
        String dietaryPreference = "Username :"+preference;
        diet_view.setText(dietaryPreference);
    }
    public void updateUserName(String preference){
        TextView username_view = (TextView) findViewById(R.id.profileUsername);
        String userName = "Username :" + preference;
        username_view.setText(userName);
    }
    public void updateEmail(String preference){
        TextView email_view = (TextView) findViewById(R.id.profileEmail);
        String userEmail = "Username" + preference;
        email_view.setText(userEmail);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Button edit = (Button) findViewById(R.id.profileSave);;
        Button chat = (Button) findViewById(R.id.profileToChat);
        final TextView username_view = (TextView) findViewById(R.id.profileUsername);
        final TextView diet_view = (TextView) findViewById(R.id.profileDiet);
        final TextView email_view = (TextView) findViewById(R.id.profileEmail);
          name = getIntent().getStringExtra("name");
          prefs = getIntent().getStringExtra("prefs");


        final int idd1 = getIntent().getIntExtra("idd" , 7);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.18.21:3306/updateDietPref?idd="+String.valueOf(idd1)+"&pref="+prefs;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String responseString = response.toString();
                        username_view.setText(name);
                        email_view.setText(responseString.split(",")[1]);
                        diet_view.setText(responseString.split(",")[2]);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                username_view.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("logg" , "at viewprofileclick "+String.valueOf(idd1));
                Intent intent = new Intent(getApplicationContext(), ChatHistory.class);
                intent.putExtra("name", name);
                intent.putExtra("idd", idd1);
                startActivity(intent);
            }
        });

//        startActivity(intent);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), profileEdit.class);
                // Code here executes on main thread after user presses button
                intent.putExtra("name", name);
                intent.putExtra("idd", idd1);
                startActivity(intent);
            }
        });
    }
}
