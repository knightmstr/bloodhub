package edu.app.bloodhub.dashboard.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.app.bloodhub.R;
import edu.app.bloodhub.emergency.activity.EmergencyPostActivity;
import edu.app.bloodhub.model.Post;

public class DonatePostsAdapter extends RecyclerView.Adapter<DonatePostsAdapter.PostsViewHolder> {

    Context context;
    List<Post> posts;
    List<Post> originalPost;


    public DonatePostsAdapter(List<Post> posts) {
        this.posts = posts;
        this.originalPost = posts;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PostsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvName.setText(post.getUser().getFullName());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        public PostsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmergencyPostActivity.startEmergencyPostActivity(context,posts.get(getAdapterPosition()),originalPost,getAdapterPosition());
                }
            });
        }
    }

    public void updateAdapter(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }
}
