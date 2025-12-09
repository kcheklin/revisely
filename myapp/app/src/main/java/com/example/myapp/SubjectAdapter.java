package com.example.myapp;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.view.*;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;

    public SubjectAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubject, tvLevel;

        public SubjectViewHolder(@NonNull View itemView){
            super(itemView);
            tvSubject = itemView.findViewById(R.id.TVSubject2);
            tvLevel = itemView.findViewById(R.id.TVLevel);
        }
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position){
        Subject subject = subjectList.get(position);

        holder.tvSubject.setText(subject.getSubject());

        String level;
        if(subject.getMinLevel().equalsIgnoreCase(subject.getMaxLevel())){
            level = subject.getMinLevel();
        }else if(subject.getMinLevel().equalsIgnoreCase("Beginner") && subject.getMaxLevel().equalsIgnoreCase("Advanced")){
            level = "All levels";
        }else{
            level = subject.getMinLevel() + " - " + subject.getMaxLevel();
        }
        holder.tvLevel.setText(level);
    }

    public int getItemCount(){
        return subjectList.size();
    }
}
