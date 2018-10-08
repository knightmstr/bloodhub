package edu.app.bloodhub;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import edu.app.bloodhub.utils.BloodHubPreference;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    BloodHubPreference preference;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        preference = new BloodHubPreference(this);
        preference.setToken(refreshedToken);
    }
}
