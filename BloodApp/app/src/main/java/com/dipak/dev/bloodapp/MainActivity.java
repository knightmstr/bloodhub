package com.dipak.dev.bloodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView register;
    Button login;
    int UID;
    int PID;
    String email;
    EditText et_username, et_password;
    //Boolean variable to check user is either loggedIn or not
    private boolean logged_in = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.registerLink);
        login = findViewById(R.id.login);
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
//                Intent intent = new Intent(MainActivity.this, Dashboard.class);
  //              startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public void login() {
        final String email = this.et_username.getText().toString().trim();
        final String password = this.et_password.getText().toString().trim();
        // String BloodGroup = spinner.toString();

       /* String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(method, FullName, DOB);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Username or Password", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.url_login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //Adding Values to the editor
                        editor.putBoolean(Config.SHARED_PREFERENCE_LOGGED_IN, true);
                        editor.putString(Config.SHARED_PREFERENCE_EMAIL, email);

                        //Saving values to editor
                        editor.apply();
                            login2();
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Login Failure", Toast.LENGTH_LONG).show();
                            }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Login Failure", Toast.LENGTH_LONG).show();
                }
                }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_EMAIL, email);
                    params.put(Config.KEY_PASSWORD, password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
    private void login2(){

        //fetch value from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        //fetching the email value form shared preferences
        final String username = sharedPreferences.getString(Config.SHARED_PREFERENCE_EMAIL, email);
//        final String email1 = this.et_username.getText().toString().trim();
  //      final String password1 = this.et_password.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_FETCH_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    JSONArray objJSONArray = obj.getJSONArray("post");
                    for (int i = 0; i < objJSONArray.length(); i++) {
                        JSONObject jsonObject = objJSONArray.getJSONObject(i);
                        UID = jsonObject.getInt("id");
                        //PID = jsonObject.getInt("post_id");

                        //Adding values to the editor
                        editor.putBoolean(Config.SHARED_PREFERENCE_LOGGED_IN, true);
                      //  editor.putInt(Config.SHARED_PREFERENCE_PID,PID);
                        editor.putInt(Config.SHARED_PREFERENCE_UID, UID);
                        //saving values to editor
                        editor.apply();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (UID == 1) {
                    //Starting admin dashboard activity
                    Intent intent = new Intent(MainActivity.this, AdminDashboard.class);
                    startActivity(intent);
                } else {
                    //Starting students dashboard activity
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Login Failure", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_EMAIL, username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
/*
    @Override
    protected void onResume() {
        //fetch value from shared preference
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        //fetching the boolean value form shared preferences
        logged_in = sharedPreferences.getBoolean(Config.SHARED_PREFERENCE_LOGGED_IN, false);
        UID = sharedPreferences.getInt(Config.SHARED_PREFERENCE_UID, UID);
        //if we will get true
        if(logged_in)
        {
            if (UID == 1) {
                // we will start AdminDashboard Activity
                Intent i1 = new Intent(MainActivity.this, AdminDashboard.class);
                startActivity(i1);
            }else{
                    // we will start Dashboard Activity
                    Intent i3 = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(i3);
            }
        }
        super.onResume();
    }
    */

}