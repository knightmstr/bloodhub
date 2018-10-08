package edu.app.bloodhub.dashboard.donorlist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.R;
import edu.app.bloodhub.dashboard.fragments.DonorListFragment;

public class DonorListActivity extends BaseActivity {
    @BindView(R.id.container)
    FrameLayout container;

    @Override
    public int getLayout() {
        return R.layout.activity_donor_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarVisible();
        setToolBarTitle("Donors List");
        setBackVisible();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, DonorListFragment.newInstance(true));
        transaction.commit();
    }

    public static void openDonorList(Context context) {
        context.startActivity(new Intent(context, DonorListActivity.class));

    }
}
