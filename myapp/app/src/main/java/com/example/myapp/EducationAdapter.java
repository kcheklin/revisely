package com.example.myapp;

import android.graphics.Color;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.view.*;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {

    private List<Education> educationList;

    public EducationAdapter(List<Education> educationList) {
        this.educationList = educationList;
    }

    public static class EducationViewHolder extends RecyclerView.ViewHolder{
        CardView cvEducation;
        TextView tvEducation, tvInstitution, tvGraduationYear;

        public EducationViewHolder(@NonNull View itemView){
            super(itemView);
            cvEducation = itemView.findViewById(R.id.CVEducation);
            tvEducation = itemView.findViewById(R.id.TVEducation);
            tvInstitution = itemView.findViewById(R.id.TVInstitution);
            tvGraduationYear = itemView.findViewById(R.id.TVGraduationYear);
        }
    }

    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_education, parent, false);
        return new EducationViewHolder(view);
    }

    public void onBindViewHolder(@NonNull EducationViewHolder holder, int position){
        Education education = educationList.get(position);

        holder.tvEducation.setText(education.getEducation());
        holder.tvInstitution.setText(education.getInstitution());
        holder.tvGraduationYear.setText(String.valueOf(education.getGraduationYear()));

        if(position % 2 == 0){
            holder.cvEducation.setCardBackgroundColor(Color.parseColor("#EFF6FF"));
        }else{
            holder.cvEducation.setCardBackgroundColor(Color.parseColor("#F0FDF4"));
        }
    }

    public int getItemCount(){
        return educationList.size();
    }
}
