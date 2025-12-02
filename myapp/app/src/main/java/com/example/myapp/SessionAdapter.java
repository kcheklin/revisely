package com.example.myapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private List<Session> sessions;
    private OnSessionClickListener listener;
    private OnChatClickListener chatListener;

    public interface OnSessionClickListener {
        void onSessionClick(Session session);
    }

    public interface OnChatClickListener {
        void onChatClick();
    }

    public SessionAdapter(List<Session> sessions, OnSessionClickListener listener, OnChatClickListener chatListener) {
        this.sessions = sessions;
        this.listener = listener;
        this.chatListener = chatListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;
        TextView tvTutor;
        TextView tvDate;
        TextView tvTime;
        TextView tvLocation;
        TextView tvMeetingLink;
        TextView tvStatus;
        Button btnJoin;
        Button btnChat;
        TextView tvWaiting;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            tvSubject = view.findViewById(R.id.tvSubject);
            tvTutor = view.findViewById(R.id.tvTutor);
            tvDate = view.findViewById(R.id.tvDate);
            tvTime = view.findViewById(R.id.tvTime);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvMeetingLink = view.findViewById(R.id.tvMeetingLink);
            tvStatus = view.findViewById(R.id.tvStatus);
            btnJoin = view.findViewById(R.id.btnJoin);
            btnChat = view.findViewById(R.id.btnChat);
            tvWaiting = view.findViewById(R.id.tvWaiting);
            cardView = view.findViewById(R.id.cardSession);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
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

        // Set status text and color based on session status
        switch (session.getStatus()) {
            case UPCOMING:
                holder.tvStatus.setText("UPCOMING");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#B8E6E1")); // Cyan
                holder.tvStatus.setTextColor(Color.parseColor("#4A9B8E"));
                break;
            case ACCEPTED:
                holder.tvStatus.setText("ACCEPTED");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#D4E7D4")); // Green
                holder.tvStatus.setTextColor(Color.parseColor("#5A9B5A"));
                break;
            case PENDING:
                holder.tvStatus.setText("PENDING");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF4C4")); // Yellow
                holder.tvStatus.setTextColor(Color.parseColor("#C4A000"));
                break;
        }

        if (session.getMeetingLink() != null) {
            holder.tvMeetingLink.setVisibility(View.VISIBLE);
            holder.tvMeetingLink.setText(session.getMeetingLink());
        } else {
            holder.tvMeetingLink.setVisibility(View.GONE);
        }

        if (session.isWaitingForConfirmation()) {
            holder.tvWaiting.setVisibility(View.VISIBLE);
            holder.btnJoin.setVisibility(View.GONE);
            holder.btnChat.setVisibility(View.GONE);
        } else {
            holder.tvWaiting.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.VISIBLE);
            holder.btnChat.setVisibility(View.VISIBLE);
        }

        holder.btnChat.setOnClickListener(v -> chatListener.onChatClick());
        holder.cardView.setOnClickListener(v -> listener.onSessionClick(session));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}