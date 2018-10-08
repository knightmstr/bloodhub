package edu.app.bloodhub;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import edu.app.bloodhub.login.activity.LoginActivity;
import edu.app.bloodhub.login.activity.RegisterActivity;
import edu.app.bloodhub.utils.BloodHubPreference;

public abstract class BaseActivity extends AppCompatActivity {

    AppCompatImageView ivBack;
    AppCompatTextView tvToolbarTitle;
    RelativeLayout toolbar;
    FrameLayout container;
    public AppCompatImageView ivMenu,ivDelete;
    AppCompatTextView tvProfile, tvLogout;
    LinearLayout llMenu;

    BloodHubPreference preference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        preference = new BloodHubPreference(this);
        ivBack = findViewById(R.id.iv_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbar = findViewById(R.id.rl_toolbar);
        container = findViewById(R.id.container);
        tvProfile = findViewById(R.id.tv_profile);
        tvLogout = findViewById(R.id.tv_logout);
        llMenu = findViewById(R.id.ll_menu);
        ivMenu = findViewById(R.id.iv_menu);
        ivDelete = findViewById(R.id.iv_delete);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llMenu.getVisibility() == View.GONE) {
                    llMenu.setVisibility(View.VISIBLE);
                } else {
                    llMenu.setVisibility(View.GONE);
                }
            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llMenu.setVisibility(View.GONE);
                RegisterActivity.startRegisterActivity(BaseActivity.this, preference.getUser());
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llMenu.setVisibility(View.GONE);
                preference.clear();
                LoginActivity.startLoginActivity(BaseActivity.this);
            }
        });
        if (preference.isAdmin()) {
            tvProfile.setVisibility(View.GONE);
        }
        container.addView(LayoutInflater.from(this).inflate(getLayout(), container, false));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void showMenu() {
        ivMenu.setVisibility(View.VISIBLE);
    }

    public abstract int getLayout();

    public void setBackVisible() {
        ivBack.setVisibility(View.VISIBLE);
    }

    public void setToolBarVisible() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void setToolBarTitle(String title) {

        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(title);
    }


}
