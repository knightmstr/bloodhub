package com.dipak.dev.bloodapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static EditText fullName, dob, mobile, address, last_donation_date;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.fullName);
        dob = findViewById(R.id.dateOfBirth);
        mobile = findViewById(R.id.mobileNo);
        address = findViewById(R.id.address);
        last_donation_date = findViewById(R.id.dateOfDonation);


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

         spinner = findViewById(R.id.bloodGroup);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodGroupList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public void register(View view) {
        String FullName = fullName.getText().toString();
        String DOB = address.getText().toString();
       // String BloodGroup = spinner.toString();

        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(method, FullName, DOB);
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
