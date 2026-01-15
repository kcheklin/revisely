package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.widget.*;
import android.view.*;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder> {

    private Context context;
    private List<Tutor> tutorList;

    public TutorAdapter(Context context, List<Tutor> tutorList){
        this.context = context;
        this.tutorList = tutorList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cvTutor;
        ImageView ivAvatar;
        TextView tvTutorName, tvTutorFaculty, tvTutorRating, tvTotalRatings;
        Button btnViewProfile;
        LinearLayout llSubject;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cvTutor = itemView.findViewById(R.id.CVTutor);
            ivAvatar = itemView.findViewById(R.id.IVAvatar2);
            tvTutorName = itemView.findViewById(R.id.TVTutorName);
            tvTutorFaculty = itemView.findViewById(R.id.TVTutorFaculty);
            tvTutorRating = itemView.findViewById(R.id.TVTutorRating);
            tvTotalRatings = itemView.findViewById(R.id.TVTotalRatings);
            btnViewProfile = itemView.findViewById(R.id.BtnViewProfile);
            llSubject = itemView.findViewById(R.id.LLSubjects);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_tutor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Tutor tutor = tutorList.get(position);

        // Map backend avatar ID to drawable resource
        int drawableResourceId = AvatarMapper.getTutorAvatarResource(tutor.getAvatarId());
        holder.ivAvatar.setImageResource(drawableResourceId);
        
        holder.tvTutorName.setText(tutor.getName());
        holder.tvTutorFaculty.setText("Faculty of " + tutor.getFaculty());
        holder.tvTutorRating.setText(String.format("%.1f", tutor.getAverageRating()));
        holder.tvTotalRatings.setText("(" + tutor.getTotalRatings() + ")");

        if(position % 2 == 0){
            holder.cvTutor.setCardBackgroundColor(Color.parseColor("#EFF6FF"));
        }else{
            holder.cvTutor.setCardBackgroundColor(Color.parseColor("#F0FDF4"));
        }

        //dynamic subject chips for each tutor
        holder.llSubject.removeAllViews();
        if (tutor.getSubjects() != null) {
            for (String subject : tutor.getSubjects()) {
                TextView tv = new TextView(context);

                tv.setText(subject);
                tv.setTextColor(Color.parseColor("#4A5565"));
                tv.setPadding(40, 8, 40, 8);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

                GradientDrawable shape = new GradientDrawable();
                shape.setCornerRadius(25f);
                shape.setColor(Color.parseColor("#FFFFFF"));
                shape.setStroke(2, Color.parseColor("#E5E7EB"));
                tv.setBackground(shape);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                tv.setLayoutParams(params);

                holder.llSubject.addView(tv);
            }
        }

        holder.btnViewProfile.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("tutorId", tutor.getTutorId());
            TutorProfileFragment fragment = new TutorProfileFragment();
            fragment.setArguments(bundle);

            ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.discoveryFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }
}