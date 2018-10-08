package edu.app.bloodhub.login.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.dashboard.activity.MainActivity;
import edu.app.bloodhub.R;
import edu.app.bloodhub.dashboard.donorlist.activity.DonorListActivity;
import edu.app.bloodhub.login.readus.activity.ReadUsActivity;
import edu.app.bloodhub.model.User;
import edu.app.bloodhub.utils.BloodHubPreference;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_rad_us)
    AppCompatButton btnRadUs;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;
    @BindView(R.id.btn_donor_list)
    AppCompatButton btnDonorList;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.et_username)
    AppCompatEditText etUsername;
    @BindView(R.id.et_pass)
    AppCompatEditText etPass;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;

    ProgressDialog dialog;
    String username,password;

    FirebaseDatabase database;
    DatabaseReference reference;
    BloodHubPreference preference;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Signing In");
        dialog.setMessage("Please Wait ...");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        preference = new BloodHubPreference(this);
    }

    @OnClick({R.id.btn_rad_us, R.id.btn_register, R.id.btn_donor_list, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_rad_us:
                ReadUsActivity.openReadUsActivity(this);
                break;

            case R.id.btn_register:
                RegisterActivity.startRegisterActivity(this);
                break;
            case R.id.btn_donor_list:
                DonorListActivity.openDonorList(this);
                break;
            case R.id.btn_login:
                username = etUsername.getText().toString();
                password = etPass.getText().toString();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    if(!isAdmin()) {
                        dialog.show();
                        DatabaseReference userNameRef = reference.child("userslist").child(username);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    //create new user
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();

                                } else {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (user.getPassword().equals(password)) {
                                        dialog.dismiss();
                                        preference.setUser(user);
                                        preference.setLoggedIn(true);
                                        preference.setAdmin(false);
                                        MainActivity.startMainActivity(LoginActivity.this);
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Invalid Username or Password.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        userNameRef.addListenerForSingleValueEvent(eventListener);
                    }else{
                        dialog.dismiss();
                        preference.setLoggedIn(true);
                        preference.setAdmin(true);
                        MainActivity.startMainActivity(LoginActivity.this);
                    }
                }
                break;
        }
    }

    private boolean isAdmin() {
        if(username.equals("admin")&&password.equals("admin")){
            return true;
        }
        return false;
    }

    public static void startLoginActivity(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
        ((AppCompatActivity)context).finish();
    }
}
