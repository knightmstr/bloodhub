package edu.app.bloodhub.login.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.R;
import edu.app.bloodhub.model.User;
import edu.app.bloodhub.utils.BloodHubPreference;

public class RegisterActivity extends BaseActivity {

    private static String USER = "user";

    @BindView(R.id.et_fullname)
    AppCompatEditText etFullname;
    @BindView(R.id.et_username)
    AppCompatEditText etUsername;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.et_phone)
    AppCompatEditText etPhone;
    @BindView(R.id.tv_sex)
    AppCompatTextView tvSex;
    @BindView(R.id.tv_bloodgroup)
    AppCompatTextView tvBloodgroup;
    @BindView(R.id.et_address)
    AppCompatEditText etAddress;
    @BindView(R.id.tv_last_donated)
    AppCompatTextView tvLastDonated;
    @BindView(R.id.tv_dob)
    AppCompatTextView tvDob;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;

    ProgressDialog dialog;
    User user;

    BloodHubPreference preference;

    String fullName, dob, bloodGroup, phone, ldo, username, pass, address, sex;
    FirebaseDatabase database;
    DatabaseReference reference;
    @BindView(R.id.cb_notification)
    CheckBox cbNotification;

    @Override
    public int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("Register");
        setToolBarVisible();
        setBackVisible();
        preference = new BloodHubPreference(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait ...");
        dialog.setTitle("Registering");
        dialog.setCancelable(false);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        user = getIntent().getParcelableExtra(USER);
        if (user != null) {
            user = preference.getUser();
            btnRegister.setText("UPDATE");
            etFullname.setText("Name : " + user.getFullName());
            etFullname.setEnabled(false);
            etUsername.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            tvDob.setText("Date of Birth : " + user.getDob());
            tvDob.setEnabled(false);
            etPhone.setText("Phone : " + user.getPhone());
            tvSex.setText(user.getSex());
            tvSex.setEnabled(false);
            tvBloodgroup.setText("Blood Group : " + user.getBloodGroup());
            tvBloodgroup.setEnabled(false);
            etAddress.setText("Address : " + user.getAddress());
            tvLastDonated.setText("Last Donated On : " + user.getLdo());

        }
    }

    @OnClick({R.id.tv_sex, R.id.tv_bloodgroup, R.id.tv_last_donated, R.id.btn_register, R.id.tv_dob})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sex:
                new MaterialDialog.Builder(this)
                        .title(R.string.sex)
                        .items(R.array.sexes)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                sex = text.toString();
                                tvSex.setText(text);
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .show();
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
            case R.id.tv_last_donated:
                Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int m1 = month + 1;

                        ldo = year + "/" + m1 + "/" + dayOfMonth;
                        tvLastDonated.setText(ldo);
                    }
                };
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.tv_dob:
                Calendar myCalendar1 = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateOfBirth = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int m = month + 1;
                        dob = year + "/" + m + "/" + dayOfMonth;
                        tvDob.setText(dob);
                    }
                };
                new DatePickerDialog(RegisterActivity.this, dateOfBirth, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_register:

                if (validateEditFields()) {
                    dialog.show();
                    if (btnRegister.getText().toString().equalsIgnoreCase("Update")) {
                        update();
                    } else {
                        reference.child("usersList").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildren() == null) {
                                    register();
                                    return;
                                }
                                DatabaseReference userNameRef = reference.child("userslist").child(username);
                                ValueEventListener eventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            //create new user
                                            register();
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                };
                                userNameRef.addListenerForSingleValueEvent(eventListener);
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Some error occured. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                break;
        }
    }

    private void register() {
        user = new User();
        user.setAddress(address);
        user.setUserName(username);
        user.setPassword(pass);
        user.setBloodGroup(bloodGroup);
        user.setPhone(phone);
        user.setDob(dob);
        user.setLdo(ldo);
        user.setFullName(fullName);
        user.setSex(sex);
        reference.child("userslist").child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveInDonorList();

            }
        });
    }

    private void update() {
        dialog.setTitle("Updating");
        user = new User();
        user.setAddress(address);
        user.setUserName(username);
        user.setPassword(pass);
        user.setBloodGroup(bloodGroup);
        user.setPhone(phone);
        user.setDob(dob);
        user.setLdo(ldo);
        user.setFullName(fullName);
        user.setSex(sex);
        reference.child("userslist").child(user.getUserName()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveInDonorList();

            }
        });
    }

    private void saveInDonorList() {
        reference.child("donorList").child(bloodGroup).child(user.getUserName()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (cbNotification.isChecked()) {
                    reference.child("tokens").push().setValue(preference.getToken());
                }
                dialog.dismiss();
                if (btnRegister.getText().toString().equalsIgnoreCase("Register"))
                    LoginActivity.startLoginActivity(RegisterActivity.this);
                else {
                    Toast.makeText(RegisterActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    preference.setUser(user);
                }
            }
        });
    }


    private boolean validateEditFields() {
        fullName = etFullname.getText().toString().replace("Name : ", "");
        if (btnRegister.getText().toString().equalsIgnoreCase("Register")) {
            username = etUsername.getText().toString();
            pass = password.getText().toString();
        } else {
            username = user.getUserName();
            pass = user.getPassword();
        }
        phone = etPhone.getText().toString().replace("Phone : ", "");
        ;
        dob = tvDob.getText().toString().replace("Date of Birth : ", "");
        bloodGroup = tvBloodgroup.getText().toString().replace("Blood Group : ", "");

        ldo = tvLastDonated.getText().toString().replace("Last Donated On : ", "");
        address = etAddress.getText().toString().replace("Address : ", "");
        sex = tvSex.getText().toString();
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Fullname Cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (btnRegister.getText().toString().equalsIgnoreCase("Register")) {
            if (username.isEmpty()) {
                Toast.makeText(this, "Username Cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (pass.isEmpty()) {
                Toast.makeText(this, "Password Cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (username.length() <= 5) {
                Toast.makeText(this, "Username should at least be of 6 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (pass.length() <= 5) {
                Toast.makeText(this, "Password should at least be of 6 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (dob.equalsIgnoreCase("Date of Birth")) {
            Toast.makeText(this, "Date of Birth Cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone Cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bloodGroup.equalsIgnoreCase("Blood group")) {
            Toast.makeText(this, "Blood Group Cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static void startRegisterActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    public static void startRegisterActivity(Context context, User user) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(USER, user);
        context.startActivity(intent);
    }
}
