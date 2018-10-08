package edu.app.bloodhub.profile.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.R;
import edu.app.bloodhub.model.User;
import edu.app.bloodhub.utils.BloodHubPreference;

public class ProfileActivity extends BaseActivity {

    private static final String USER = "user";
    User user;
    @BindView(R.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R.id.tv_address)
    AppCompatTextView tvAddress;
    @BindView(R.id.tv_bloodgroup)
    AppCompatTextView tvBloodgroup;
    @BindView(R.id.tv_phone)
    AppCompatTextView tvPhone;
    @BindView(R.id.tv_ldo)
    AppCompatTextView tvLdo;

    BloodHubPreference preference;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        user = getIntent().getParcelableExtra(USER);
        setToolBarTitle("Profile");
        setToolBarVisible();
        setBackVisible();
        tvAddress.setText("Address : " + user.getAddress());
        tvName.setText(user.getFullName());
        tvBloodgroup.setText("Blood Group : " + user.getBloodGroup());
        tvPhone.setText("Phone : " + user.getPhone());
        tvLdo.setText("Last Donated On : " + user.getLdo());
        preference = new BloodHubPreference(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        if (preference.isAdmin()) {
            ivDelete.setVisibility(View.VISIBLE);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Deleting Donor");
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Are you sure to delete this donor?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                progressDialog.show();
                                reference.child("donorList").child(user.getBloodGroup()).child(user.getUserName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.child("userslist").child(user.getUserName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        });
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
        return R.layout.activity_profile;
    }

    public static void openProfileActivity(Context context, User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USER, user);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_phone)
    public void onViewClicked() {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + user.getPhone()));
        startActivity(dialIntent);
    }
}
