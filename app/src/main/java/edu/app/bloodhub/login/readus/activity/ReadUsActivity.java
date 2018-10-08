package edu.app.bloodhub.login.readus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import edu.app.bloodhub.BaseActivity;
import edu.app.bloodhub.R;

public class ReadUsActivity extends BaseActivity {
    @Override
    public int getLayout() {
        return R.layout.activity_read_us;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarVisible();
        setBackVisible();
        setToolBarTitle("Read Us");

    }

    public static void openReadUsActivity(Context context){
        context.startActivity(new Intent(context,ReadUsActivity.class));
    }
}
