package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PastSessionAdapter extends RecyclerView.Adapter<PastSessionAdapter.ViewHolder> {

    private List<Session> sessions;
    private OnRateClickListener listener;

    public interface OnRateClickListener {
        void onRateClick(Session session);
    }

    public PastSessionAdapter(List<Session> sessions, OnRateClickListener listener) {
        this.sessions = sessions;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;
        TextView tvTutor;
        TextView tvDate;
        TextView tvTime;
        TextView tvLocation;
        TextView tvMeetingLink;
        Button btnRate;

        public ViewHolder(View view) {
            super(view);
            tvSubject = view.findViewById(R.id.tvSubject);
            tvTutor = view.findViewById(R.id.tvTutor);
            tvDate = view.findViewById(R.id.tvDate);
            tvTime = view.findViewById(R.id.tvTime);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvMeetingLink = view.findViewById(R.id.tvMeetingLink);
            btnRate = view.findViewById(R.id.btnRate);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_past_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessions.get(position);

        holder.tvSubject.setText(session.getSubject());
        holder.tvTutor.setText(session.getTutor());
        holder.tvDate.setText(session.getDate());
        holder.tvTime.setText(session.getTime());
        holder.tvLocation.setText(session.getLocation());

        if (session.getMeetingLink() != null) {
            holder.tvMeetingLink.setVisibility(View.VISIBLE);
            holder.tvMeetingLink.setText(session.getMeetingLink());
        } else {
            holder.tvMeetingLink.setVisibility(View.GONE);
        }

        holder.btnRate.setOnClickListener(v -> listener.onRateClick(session));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
