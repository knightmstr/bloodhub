package com.dipak.dev.bloodapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static EditText email, dob, mobile, address, last_donation_date, password;
    Spinner bloodgroup;
    Button register_button, cancel_button;
     private static String url_register ="http://10.0.2.2/BloodHub/register.php";
    //private static String url_register ="http://decapodous-pace.000webhostapp.com/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.fullName);
        dob = findViewById(R.id.dateOfBirth);
       // bloodgroup = findViewById(R.id.bloodGroup);
        mobile = findViewById(R.id.mobileNo);
        address = findViewById(R.id.address);
        last_donation_date = findViewById(R.id.dateOfDonation);
        password = findViewById(R.id.password);
        register_button = findViewById(R.id.registerBtn);
        cancel_button = findViewById(R.id.cancel_btn);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager().beginTransaction(), "datePicker");
            }
        });
        last_donation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.DialogFragment newFragment = new DatePickerFragment1();
                newFragment.show(getSupportFragmentManager().beginTransaction(), "datePicker");
            }
        });

        bloodgroup = findViewById(R.id.bloodGroup);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodGroupList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        bloodgroup.setAdapter(adapter);
        bloodgroup.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void register() {
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String dob = this.dob.getText().toString().trim();
        final String address = this.address.getText().toString().trim();
        final String mobile = this.mobile.getText().toString().trim();
        final String donation_date = this.last_donation_date.getText().toString().trim();
        final String BloodGroup = this.bloodgroup.toString();

        if (email.isEmpty() || password.isEmpty() || address.isEmpty() || mobile.isEmpty() || BloodGroup.isEmpty()) {
            Toast.makeText(Register.this, "Enter All The Fields", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_register, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            Toast.makeText(Register.this, "Register Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Register.this, "Register Failure", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "Register Error! " + e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Register.this, "Register Failure", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("dob", dob);
                    params.put("address", address);
                    params.put("donation_date", donation_date);
                    params.put("mobile", mobile);
                    params.put("BloodGroup", BloodGroup);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    //Date Picker
    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // SetDate(year, month, day);
            dob.setText(year + "-" + (month + 1) + "-" + day);
          //  last_donation_date.setText(year + "-" + (month + 1) + "-" + day);
        }
    }
    //end of date  picker

    //Date Picker
    public static class DatePickerFragment1 extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // SetDate(year, month, day);
            last_donation_date.setText(year + "-" + (month + 1) + "-" + day);
        }
    }
    //end of date  picker

}
