package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapp.models.StudySession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudySessionAdapter extends RecyclerView.Adapter<StudySessionAdapter.ViewHolder> {

    private List<StudySession> studySessions;

    public StudySessionAdapter(List<StudySession> studySessions) {
        this.studySessions = studySessions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_study_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudySession session = studySessions.get(position);
        
        holder.tvSubject.setText(session.getSubject());
        holder.tvDuration.setText(session.getDurationInMinutes() + " minutes");
        
        // Format the date
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US);
            Date date = inputFormat.parse(session.getStartTime());
            if (date != null) {
                holder.tvDate.setText(outputFormat.format(date));
            } else {
                holder.tvDate.setText(session.getStartTime());
            }
        } catch (ParseException e) {
            holder.tvDate.setText(session.getStartTime());
        }
    }

    @Override
    public int getItemCount() {
        return studySessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvDuration, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}

