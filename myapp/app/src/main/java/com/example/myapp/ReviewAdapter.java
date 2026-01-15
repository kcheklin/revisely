package com.example.myapp;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.view.*;
import android.widget.*;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cvReview;
        TextView tvName, tvRating, tvLikes;
        ImageView ivAvatar, ivStar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cvReview = itemView.findViewById(R.id.CVReview);
            tvName = itemView.findViewById(R.id.TVStudentNameReview);
            tvRating = itemView.findViewById(R.id.TVRatingStudentReview);
            tvLikes = itemView.findViewById(R.id.TVLikes);
            ivAvatar = itemView.findViewById(R.id.IVAvatarStudentReview);
            ivStar = itemView.findViewById(R.id.IVRatingReview2);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Review review = reviewList.get(position);

        // Map backend avatar ID to drawable resource
        int drawableResourceId = AvatarMapper.getStudentAvatarResource(review.getAvatarId());
        holder.ivAvatar.setImageResource(drawableResourceId);
        
        holder.tvName.setText(review.getName());
        holder.tvRating.setText(review.getContent());
        holder.tvLikes.setText("Helpful (" + review.getLikes() + ")");

        int star = review.getStar();
        switch (star){
            case 5:
                holder.ivStar.setImageResource(R.drawable.ic_rating_5);
                break;
            case 4:
                holder.ivStar.setImageResource(R.drawable.ic_rating_4);
                break;
            case 3:
                holder.ivStar.setImageResource(R.drawable.ic_rating_3);
                break;
            case 2:
                holder.ivStar.setImageResource(R.drawable.ic_rating_2);
                break;
            case 1:
                holder.ivStar.setImageResource(R.drawable.ic_rating_1);
                break;
        }

        if(position % 2 == 0){
            holder.cvReview.setCardBackgroundColor(Color.parseColor("#EFF6FF"));
        }else{
            holder.cvReview.setCardBackgroundColor(Color.parseColor("#F0FDF4"));
        }
    }

    @Override
    public int getItemCount(){
        return reviewList.size();
    }

    public void updateList(List<Review> filteredReviews){
        reviewList.clear();
        reviewList.addAll(filteredReviews);
        notifyDataSetChanged();
    }
}
