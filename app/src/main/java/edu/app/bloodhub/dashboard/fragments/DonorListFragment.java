package edu.app.bloodhub.dashboard.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edu.app.bloodhub.R;
import edu.app.bloodhub.dashboard.adapters.DonorListAdapter;
import edu.app.bloodhub.model.User;

public class DonorListFragment extends Fragment {
    private static final String UNSIGNED = "unsigned";
    @BindView(R.id.tv_label)
    AppCompatTextView tvLabel;
    @BindView(R.id.tv_blood_group)
    AppCompatTextView tvBloodGroup;
    @BindView(R.id.rv_donors_list)
    RecyclerView rvDonorsList;
    Unbinder unbinder;

    LinearLayoutManager manager;

    FirebaseDatabase database;
    DatabaseReference reference;
    List<User> users;


    ProgressDialog progressDialog;
    @BindView(R.id.tv_no_donor)
    AppCompatTextView tvNoDonor;

    boolean isUnsigned;
    String bloodGroup = "A+";


    public static DonorListFragment newInstance() {
        return new DonorListFragment();
    }

    public static DonorListFragment newInstance(boolean isUnSigned) {
        DonorListFragment fragment = new DonorListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(UNSIGNED, isUnSigned);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donors, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = new LinearLayoutManager(getActivity());
        rvDonorsList.setLayoutManager(manager);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        users = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait ...");
        progressDialog.setTitle("Fetching Donor List");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (getArguments() != null)
            isUnsigned = getArguments().getBoolean(UNSIGNED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_blood_group)
    public void onViewClicked() {
        new MaterialDialog.Builder(getActivity())
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
                        tvBloodGroup.setText(text);
                        progressDialog.show();
                        reference.child("donorList").child(text.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                progressDialog.dismiss();
                                users = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    users.add(snapshot.getValue(User.class));
                                }
                                if (users.size() > 0) {
                                    tvNoDonor.setVisibility(View.GONE);
                                    rvDonorsList.setVisibility(View.VISIBLE);
                                    DonorListAdapter adapter = new DonorListAdapter(users, isUnsigned);
                                    rvDonorsList.setAdapter(adapter);

                                } else {
                                    tvNoDonor.setVisibility(View.VISIBLE);
                                    rvDonorsList.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        reference.child("donorList").child(bloodGroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.size() > 0) {
                    tvNoDonor.setVisibility(View.GONE);
                    rvDonorsList.setVisibility(View.VISIBLE);
                    DonorListAdapter adapter = new DonorListAdapter(users, isUnsigned);
                    rvDonorsList.setAdapter(adapter);

                } else {
                    tvNoDonor.setVisibility(View.VISIBLE);
                    rvDonorsList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
