package edu.app.bloodhub.dashboard.events.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.R;
import edu.app.bloodhub.model.Event;
import edu.app.bloodhub.utils.BloodHubPreference;

public class EventPostActivity extends BaseActivity {

    private static final String EVENT = "event";
    private static final String EVENTS = "events";
    private static final String POSITION = "Position";
    @BindView(R.id.et_name)
    AppCompatEditText etName;
    @BindView(R.id.et_description)
    AppCompatEditText etDescription;
    @BindView(R.id.et_address)
    AppCompatEditText etAddress;
    @BindView(R.id.et_date)
    AppCompatEditText etDate;
    @BindView(R.id.et_phone)
    AppCompatEditText etPhone;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;


    ProgressDialog dialog,progressDialog;
    BloodHubPreference preference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Event event;
    int position;
    List<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("Create Event");
        setToolBarVisible();
        setBackVisible();
        preference = new BloodHubPreference(this);
        event = getIntent().getParcelableExtra(EVENT);
        if (event != null) {
            events = getIntent().getParcelableArrayListExtra(EVENTS);
            position = getIntent().getIntExtra(POSITION,0);
            setToolBarTitle("Event Detail");
            btnSubmit.setVisibility(View.GONE);
            etName.setText(event.getName());
            etName.setEnabled(false);
            etDescription.setText(event.getDescription());
            etDescription.setEnabled(false);
            etAddress.setText("Address : "+event.getAddress());
            etAddress.setEnabled(false);
            etDate.setText("Date and Time : "+event.getDateAndTime());
            etDate.setEnabled(false);
            etPhone.setText("Phone : "+event.getPhone());
            etPhone.setEnabled(false);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Posting Event");
        dialog.setMessage("Please wait ...");
        dialog.setCancelable(false);


        if (preference.isAdmin()) {
            ivDelete.setVisibility(View.VISIBLE);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Deleting Event");
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventPostActivity.this);
                builder.setMessage("Are you sure to delete this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                progressDialog.show();
                                events.remove(position);
                                reference.child("events").setValue(events).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        finish();
                                        Toast.makeText(EventPostActivity.this, "The event is deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();


            }
        });

    }

    @Override
    public int getLayout() {
        return R.layout.activity_event_post;
    }

    public static void createEvent(Context context) {
        context.startActivity(new Intent(context, EventPostActivity.class));
    }

    public static void createEvent(Context context, Event event, List<Event> events,int position) {
        Intent intent = new Intent(context, EventPostActivity.class);
        intent.putExtra(EVENT,event);
        intent.putParcelableArrayListExtra(EVENTS,(ArrayList<Event>) events);
        intent.putExtra(POSITION,position);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (validate()) {
            dialog.show();
            reference.child("events").push().setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    Toast.makeText(EventPostActivity.this, "Event has been published", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private boolean validate() {
        event = new Event();
        event.setAddress(etAddress.getText().toString());
        event.setName(etName.getText().toString());
        event.setPhone(etPhone.getText().toString());
        event.setDescription(etDescription.getText().toString());
        event.setDateAndTime(etDate.getText().toString());
        if (event.getName().isEmpty() || event.getDescription().isEmpty()
                || event.getAddress().isEmpty() || event.getDateAndTime().isEmpty()
                || event.getPhone().isEmpty()) {
            Toast.makeText(this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
