package com.dipak.dev.bloodapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
//    private Context context;

    //List to store all users
    private ArrayList<User> userArrayList = new ArrayList<>();

    //Constructor of this class
    public CardAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setUserList(ArrayList<User> users) {
        this.userArrayList = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.myprofile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //Getting the particular item from the list
        User user = userArrayList.get(position);

        //Showing data on the views
        holder.et_username.setText(user.getUsername());
        holder.et_address.setText(user.getAddress());
        holder.et_dob.setText(user.getDob());
        holder.et_mobile.setText(user.getMobile());
        holder.et_bloodgroup.setText(user.getBlood_group());
        holder.et_donationDate.setText(user.getDonation_Date());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Views
        EditText et_username, et_dob, et_address, et_mobile, et_bloodgroup, et_donationDate;
        Button update_btn, cancel_btn;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            et_username = itemView.findViewById(R.id.p_username);
            //et_password = itemView.findViewById(R.id.);
            et_dob = itemView.findViewById(R.id.p_dob);
            et_address = itemView.findViewById(R.id.p_address);
            et_mobile = itemView.findViewById(R.id.p_mobile);
            et_bloodgroup = itemView.findViewById(R.id.p_bloodgroup);
            et_donationDate = itemView.findViewById(R.id.p_donationDate);
            update_btn = itemView.findViewById(R.id.update_btn);
            cancel_btn = itemView.findViewById(R.id.p_cancel_button);
        }
    }
}
