package edu.app.bloodhub.emergency.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import edu.app.bloodhub.model.Post;
import edu.app.bloodhub.model.User;
import edu.app.bloodhub.utils.BloodHubPreference;

public class EmergencyPostActivity extends BaseActivity {
    private static final String POST = "post";
    private static final String POSTS = "posts";
    private static final String POSITION = "position";
    @BindView(R.id.btn_need)
    AppCompatButton btnNeed;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_bloodgroup)
    AppCompatTextView tvBloodgroup;
    @BindView(R.id.et_amount)
    AppCompatEditText etAmount;
    @BindView(R.id.et_phone)
    AppCompatEditText etPhone;
    @BindView(R.id.et_address)
    AppCompatEditText etAddress;
    @BindView(R.id.et_req_within)
    AppCompatEditText etReqWithin;

    FirebaseDatabase database;
    DatabaseReference reference;

    String bloodGroup, amount, phone, address, reqWithin;
    ProgressDialog dialog;
    BloodHubPreference preference;
    User user;
    Post post;
    List<Post> posts;
    int position;
    ProgressDialog progressDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_emergency_post;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("Emergnecy Post");
        setToolBarVisible();
        setBackVisible();

        post = getIntent().getParcelableExtra(POST);
        if (post != null) {
            posts = getIntent().getParcelableArrayListExtra(POSTS);
            position = getIntent().getIntExtra(POSITION,0);
            llBottom.setVisibility(View.GONE);
            tvBloodgroup.setText("Blood Group : " + post.getBloodGroup());
            tvBloodgroup.setEnabled(false);
            etAmount.setText("Amount : " + post.getAmount());
            etAmount.setEnabled(false);
            etPhone.setText("Phone : " + post.getMobile());
            etPhone.setEnabled(false);
            etAddress.setText("Address : " + post.getAddress());
            etAddress.setEnabled(false);
            etReqWithin.setText("Required Within : " + post.getRequiredWithin());
            etReqWithin.setEnabled(false);


        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait ...");
        dialog.setTitle("Posting");
        dialog.setCancelable(false);
        preference = new BloodHubPreference(this);
        user = preference.getUser();
        post = new Post();
        post.setUser(user);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

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

                AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyPostActivity.this);
                builder.setMessage("Are you sure to delete this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                progressDialog.show();
                                posts.remove(position);
                                reference.child("inNeed").setValue(posts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        finish();
                                        Toast.makeText(EmergencyPostActivity.this, "The post is deleted.", Toast.LENGTH_SHORT).show();
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


    @OnClick({R.id.btn_need, R.id.tv_bloodgroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_need:
                if (validateFields()) {
                    dialog.show();
                    reference.child("inNeed").push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Toast.makeText(EmergencyPostActivity.this, "Your post has been published", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                break;
            case R.id.tv_bloodgroup:
                new MaterialDialog.Builder(this)
                        .title(R.string.blood_group)
                        .items(R.array.blood_groups)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                bloodGroup = text.toString();
                                tvBloodgroup.setText(text);
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .show();
                break;
        }
    }

    private boolean validateFields() {
        bloodGroup = tvBloodgroup.getText().toString();
        amount = etAmount.getText().toString();
        phone = etPhone.getText().toString();
        address = etAddress.getText().toString();
        reqWithin = etReqWithin.getText().toString();
        if (bloodGroup.equalsIgnoreCase("Blood Group")) {
            Toast.makeText(this, "Blood Group can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        post.setBloodGroup(bloodGroup);
        post.setAmount(amount);
        post.setMobile(phone);
        post.setAddress(address);
        post.setRequiredWithin(reqWithin);
        return true;
    }


    public static void startEmergencyPostActivity(Context context) {
        context.startActivity(new Intent(context, EmergencyPostActivity.class));
    }

    public static void startEmergencyPostActivity(Context context, Post post, List<Post>posts,int position) {
        Intent intent = new Intent(context, EmergencyPostActivity.class);
        intent.putExtra(POST, post);
        intent.putParcelableArrayListExtra(POSTS, (ArrayList<Post>) posts);
        intent.putExtra(POSITION, position);
        context.startActivity(intent);
    }
}
