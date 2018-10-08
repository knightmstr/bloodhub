package edu.app.bloodhub.dashboard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.R;
import edu.app.bloodhub.dashboard.adapters.DashboardPagerAdapter;
import edu.app.bloodhub.login.activity.LoginActivity;
import edu.app.bloodhub.utils.BloodHubPreference;

public class MainActivity extends BaseActivity {

    BloodHubPreference preference;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        preference = new BloodHubPreference(this);
        if (!preference.isLoggedIN()) {
            LoginActivity.startLoginActivity(this);
            return;
        }
        setToolBarTitle("Blood Hub");
        setToolBarVisible();
        showMenu();
        viewPager.setAdapter(new DashboardPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        tabs.setShouldExpand(true);
        viewPager.setOffscreenPageLimit(3);
    }

    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
        ((AppCompatActivity) context).finish();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }
}
