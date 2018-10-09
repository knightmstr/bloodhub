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
import android.widget.ImageView;

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
import edu.app.bloodhub.dashboard.adapters.DonatePostsAdapter;
import edu.app.bloodhub.emergency.activity.EmergencyPostActivity;
import edu.app.bloodhub.model.Post;
import edu.app.bloodhub.utils.BloodHubPreference;

public class EmergencyFragment extends Fragment {

    @BindView(R.id.rv_in_need)
    RecyclerView rvInNeed;
    @BindView(R.id.iv_post)
    ImageView ivPost;
    Unbinder unbinder;

    LinearLayoutManager layoutManagerInNeed;
    List<Post> inNeedPosts = new ArrayList<>(), filteredPosts = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    ProgressDialog dialog;
    @BindView(R.id.tv_no_in_need)
    AppCompatTextView tvNoInNeed;

    BloodHubPreference preference;

    DonatePostsAdapter adapter;
    @BindView(R.id.tv_label)
    AppCompatTextView tvLabel;
    @BindView(R.id.tv_blood_group)
    AppCompatTextView tvBloodGroup;

    String filterBy ="A+";


    public static EmergencyFragment newInstance() {
        return new EmergencyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManagerInNeed = new LinearLayoutManager(getActivity());

        rvInNeed.setLayoutManager(layoutManagerInNeed);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading Posts");
        dialog.setMessage("Please wait ...");
        dialog.setCancelable(false);
        dialog.show();
        preference = new BloodHubPreference(getActivity());
        if (preference.isAdmin()) {
            ivPost.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();
        inNeedPosts = new ArrayList<>();
        reference.child("inNeed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getChildren() != null)
                        inNeedPosts.add(snapshot.getValue(Post.class));
                }
                dialog.dismiss();
                if (inNeedPosts.size() != 0) {
                    tvNoInNeed.setVisibility(View.GONE);
                    adapter = new DonatePostsAdapter(inNeedPosts);
                    rvInNeed.setAdapter(adapter);
                    filterByGroup(filterBy);

                } else {
                    tvNoInNeed.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void filterByGroup(String bloodGroup) {
        filteredPosts = new ArrayList<>();
        for (Post post : inNeedPosts) {
            if (post.getBloodGroup().equalsIgnoreCase(bloodGroup)) {
                filteredPosts.add(post);
            }
        }
        if (filteredPosts.size() > 0) {
            rvInNeed.setVisibility(View.VISIBLE);
            tvNoInNeed.setVisibility(View.GONE);
            adapter.updateAdapter(filteredPosts);
        } else {
            rvInNeed.setVisibility(View.GONE);
            tvNoInNeed.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_blood_group, R.id.iv_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_blood_group:
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
                                tvBloodGroup.setText(text);
                                filterBy = text.toString();
                                filterByGroup(text.toString());
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .show();
                break;
            case R.id.iv_post:
                EmergencyPostActivity.startEmergencyPostActivity(getActivity());
                break;
        }
    }
}

