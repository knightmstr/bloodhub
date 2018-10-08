package edu.app.bloodhub.dashboard.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.app.bloodhub.R;
import edu.app.bloodhub.model.User;
import edu.app.bloodhub.profile.activity.ProfileActivity;

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.UsersViewHolder> {
    Context context;
    List<User> users;
    boolean isUnsigned;

    public DonorListAdapter(List<User> users, boolean isUnsigned) {
        this.users = users;
        this.isUnsigned = isUnsigned;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_donor, parent, false));
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvName.setText("Name : " + user.getFullName());
        holder.tvAddress.setText("Address : " + user.getAddress());
        holder.tvPhone.setText("Phone : " + user.getPhone());
        holder.tvLdo.setText("Last Donated On : " + user.getLdo());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_address)
        AppCompatTextView tvAddress;
        @BindView(R.id.tv_phone)
        AppCompatTextView tvPhone;
        @BindView(R.id.tv_ldo)
        AppCompatTextView tvLdo;

        public UsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUnsigned)
                        Toast.makeText(context, "You must Sign In to open Profile", Toast.LENGTH_SHORT).show();
                    else
                        ProfileActivity.openProfileActivity(context, users.get(getAdapterPosition()));
                }
            });
        }
    }
}
