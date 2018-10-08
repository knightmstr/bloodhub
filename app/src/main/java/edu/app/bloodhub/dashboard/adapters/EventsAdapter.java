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
import edu.app.bloodhub.dashboard.events.activity.EventPostActivity;
import edu.app.bloodhub.model.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    Context context;
    List<Event> events;

    public EventsAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.tvEventName.setText(events.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_event_name)
        AppCompatTextView tvEventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventPostActivity.createEvent(context,events.get(getAdapterPosition()),events,getAdapterPosition());
                }
            });
        }
    }
}
