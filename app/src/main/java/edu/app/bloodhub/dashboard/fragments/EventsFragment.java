package edu.app.bloodhub.dashboard.fragments;

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
import edu.app.bloodhub.dashboard.adapters.EventsAdapter;
import edu.app.bloodhub.dashboard.events.activity.EventPostActivity;
import edu.app.bloodhub.model.Event;
import edu.app.bloodhub.utils.BloodHubPreference;

public class EventsFragment extends Fragment {

    @BindView(R.id.rv_events)
    RecyclerView rvEvents;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_no_events)
    AppCompatTextView tvNoEvents;
    Unbinder unbinder;

    LinearLayoutManager manager;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<Event> events;

    BloodHubPreference preference;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        manager = new LinearLayoutManager(getActivity());
        rvEvents.setLayoutManager(manager);
        preference = new BloodHubPreference(getActivity());
        if(preference.isAdmin()){
            ivAdd.setVisibility(View.VISIBLE);
        }else {
            ivAdd.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked() {
        EventPostActivity.createEvent(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        reference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    events.add(snapshot.getValue(Event.class));
                }
                if(events.size()>0){
                    rvEvents.setVisibility(View.VISIBLE);
                    tvNoEvents.setVisibility(View.GONE);
                    rvEvents.setAdapter(new EventsAdapter(events));
                }else {
                    rvEvents.setVisibility(View.GONE);
                    tvNoEvents.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
