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

public class profileEdit extends AppCompatActivity {
    /*
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        //...
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            Spinner spinner = (Spinner) findViewById(R.id.selectDietaryPreferences);
            spinner.setOnItemSelectedListener(this);

        }
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos)
            ViewProfile profile = new ViewProfile();
            profile.setDietaryPreference((String) parent.getItemAtPosition(pos));
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

     */
    String prefs= "NONE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        final String name = getIntent().getStringExtra("name");

        final int idd1 = getIntent().getIntExtra("idd" , 7);

        Spinner spinner = (Spinner) findViewById(R.id.selectDietaryPreferences);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] options = getResources().getStringArray(R.array.dietaryArray);
                prefs = options[position];
                Log.i("logg" , "prefs is " + prefs+ String.valueOf(idd1));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dietaryArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Button save_page = (Button) findViewById(R.id.profileSave);
        final Intent intent = new Intent(profileEdit.this, ViewProfile.class);

        save_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText username = (EditText) findViewById(R.id.selectUsername);
                ViewProfile prof = new ViewProfile();
//                if(!username.getText().toString().isEmpty()){
                Log.i("logg","ateditprof"+ String.valueOf(idd1));
                intent.putExtra("name", username.getText().toString());
                intent.putExtra("prefs", prefs);
                intent.putExtra("idd", idd1);
                //prof.setDietaryPreference(prefs);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://192.168.18.21:3306/updateDietPref?idd="+String.valueOf(idd1)+"&pref="+prefs;

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
//                }
                startActivity(intent);
            }
        });
    }
}
