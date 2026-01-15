package com.example.myapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;

import android.widget.*;
import android.view.*;
import androidx.annotation.*;
import androidx.recyclerview.widget.*;
import java.util.*;
import com.google.android.material.chip.*;

public class TutorProfileFragment extends Fragment {

    private Tutor tutor;

    public TutorProfileFragment(){
        super(R.layout.fragment_tutor_profile);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        String tutorId = getArguments().getString("tutorId");
        tutor = findTutorByTutorId(tutorId);

        ImageView ivAvatar = view.findViewById(R.id.IVAvatar2);
        TextView tvName = view.findViewById(R.id.TVTutorName2);
        TextView tvRole = view.findViewById(R.id.TVTutorRole);
        TextView tvAbout = view.findViewById(R.id.TVAbout);
        TextView tvNoStudents = view.findViewById(R.id.TVNoStudents);
        TextView tvNoSessions = view.findViewById(R.id.TVNoSessions);
        TextView tvExperienceYears = view.findViewById(R.id.TVExperienceYears);
        TextView tvRating = view.findViewById(R.id.TVRating2);
        TextView tvReviews = view.findViewById(R.id.TVReviews);
        ChipGroup cgDate = view.findViewById(R.id.CGDate);
        ChipGroup cgTime = view.findViewById(R.id.CGTime);
        ProgressBar[] pbStars = {view.findViewById(R.id.PB5Star), view.findViewById(R.id.PB4Star), view.findViewById(R.id.PB3Star), view.findViewById(R.id.PB2Star), view.findViewById(R.id.PB1Star)};
        TextView[] tvStarsCount = {view.findViewById(R.id.TV5Star), view.findViewById(R.id.TV4Star), view.findViewById(R.id.TV3Star), view.findViewById(R.id.TV2Star), view.findViewById(R.id.TV1Star)};

        // Map backend avatar ID to drawable resource
        int drawableResourceId = AvatarMapper.getTutorAvatarResource(tutor.getAvatarId());
        ivAvatar.setImageResource(drawableResourceId);
        tvName.setText(tutor.getName());
        tvRole.setText(tutor.getRole());
        tvAbout.setText(tutor.getAbout());
        tvNoStudents.setText(String.valueOf(tutor.getNoStudents()));
        tvNoSessions.setText(String.valueOf(tutor.getNoSessions()));
        tvExperienceYears.setText(tutor.getYearsExperience() + " yrs");
        tvRating.setText(String.format("%.1f", tutor.getAverageRating()));
        tvReviews.setText(tutor.getTotalRatings() + " reviews");

        for(int i = 0; i < 5; i++){
            int percentage = (tutor.getTotalRatings() == 0) ? 0 : (tutor.getStarRatings().get(i) * 100 / tutor.getTotalRatings());
            pbStars[i].setProgress(percentage);
            tvStarsCount[i].setText(String.valueOf(tutor.getStarRatings().get(i)));
        }

        //Navigate to review page when card is clicked
        CardView cvRating = view.findViewById(R.id.CVRating);
        cvRating.setOnClickListener(v -> {
            TutorReviewFragment fragment = new TutorReviewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tutorId", tutor.getTutorId());
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.discoveryFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        RecyclerView rvSubject = view.findViewById(R.id.RVSubject);
        rvSubject.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Subject> subjectList = new ArrayList<>();
        for(int i = 0; i < tutor.getSubjects().size(); i++){
            subjectList.add(new Subject(tutor.getSubjects().get(i), tutor.getSubjectMinLevel().get(i), tutor.getSubjectMaxLevel().get(i)));
        }
        SubjectAdapter subjectAdapter = new SubjectAdapter(new ArrayList<>(subjectList));
        rvSubject.setAdapter(subjectAdapter);

        RecyclerView rvEducation = view.findViewById(R.id.RVEducation);
        rvEducation.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Education> educationList = new ArrayList<>();
        for(int i = 0; i < tutor.getEducation().size(); i++){
            educationList.add(new Education(tutor.getEducation().get(i), tutor.getInstitutions().get(i), tutor.getGraduationYears().get(i)));
        }
        EducationAdapter educationAdapter = new EducationAdapter(new ArrayList<>(educationList));
        rvEducation.setAdapter(educationAdapter);

        cgDate.removeAllViews();
        cgTime.removeAllViews();
        //Prepare date format: date in Day, Date (Thu, 12); complete date to use for tag (2025-12-12); Time in
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.getDefault());
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        List<String> timeSlots = tutor.getTimeSlots(); //store in Date Time format (2025-12-12 20:00)
        List<Boolean> availability = tutor.getTimeSlotsAvailability();

        //Display date chip for the next 7 days based on current local date
        for (int i = 0; i < 7; i++){
            String dayName = dayFormat.format(calendar.getTime());
            String dayNumber = dateFormat.format(calendar.getTime());
            String dateCurrent = fullDateFormat.format(calendar.getTime());

            Chip chip = new Chip(getContext());
            chip.setText(dayName + ", " + dayNumber);
            chip.setTag(dateCurrent); //Store full date in tag
            chip.setBackgroundColor(Color.parseColor("#F3E8FF"));
            chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#E9D4FF")));
            chip.setChipCornerRadius(30);

            //Check if any time slot available for this date
            boolean hasAvailableSlot = false;
            for(int j = 0; j < timeSlots.size(); j++){
                String slotDate = timeSlots.get(j).split("\\s+")[0];
                if(slotDate.equals(dateCurrent) && availability.get(j)){
                    hasAvailableSlot = true;
                    break;
                }
            }
            chip.setEnabled(hasAvailableSlot);
            chip.setCheckable(hasAvailableSlot);
            chip.setTextColor(hasAvailableSlot ? Color.parseColor("#364153") : Color.parseColor("#D1D5DC"));

            cgDate.addView(chip);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //Display available time chips when user click on data chip
        cgDate.setOnCheckedStateChangeListener((group, checkedIds) -> {
            cgTime.removeAllViews();
            if(!checkedIds.isEmpty()){
                int checkedId = checkedIds.get(0);
                Chip selectedChip = group.findViewById(checkedId);
                String selectedDate = (String)selectedChip.getTag();

                for(int i = 0; i < timeSlots.size(); i++){
                    String[] parts = timeSlots.get(i).split("\\s+");
                    if(parts[0].equals(selectedDate)){
                        Chip timeChip = new Chip(getContext());
                        timeChip.setText(parts[1]);
                        timeChip.setEnabled(availability.get(i));
                        timeChip.setCheckable(availability.get(i));
                        timeChip.setBackgroundColor(Color.parseColor("#F3E8FF"));
                        timeChip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#E9D4FF")));
                        timeChip.setChipCornerRadius(30);
                        timeChip.setTextColor(availability.get(i) ? Color.parseColor("#364153") : Color.parseColor("#D1D5DC"));
                        cgTime.addView(timeChip);
                    }
                }
            }
        });

        //Auto select the first available date chip
        for(int i = 0; i < cgDate.getChildCount(); i++){
            Chip chip = (Chip) cgDate.getChildAt(i);
            if (chip.isEnabled()) {
                chip.setChecked(true);
                break;
            }
        }

        //Book session button
        LinearLayout llBookSession = view.findViewById(R.id.LLBookSession);
        llBookSession.setOnClickListener(v -> {
            int selectedDateId = cgDate.getCheckedChipId();
            if (selectedDateId == -1) {
                Toast.makeText(getContext(), "Select a date!", Toast.LENGTH_SHORT).show();
                return;
            }
            Chip selectedDateChip = cgDate.findViewById(selectedDateId);

            int selectedTimeId = cgTime.getCheckedChipId();
            if (selectedTimeId == -1) {
                Toast.makeText(getContext(), "Select a time!", Toast.LENGTH_SHORT).show();
                return;
            }
            Chip selectedTimeChip = cgTime.findViewById(selectedTimeId);

            BookingFragment fragment = new BookingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tutorId", tutorId);
            bundle.putString("selectedDate", selectedDateChip.getTag().toString());
            bundle.putString("selectedTime", selectedTimeChip.getText().toString());
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.discoveryFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.IVBackProfile).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private Tutor findTutorByTutorId(String tutorId){
        if(TutorDiscoveryFragment.tutorList != null){
            for(Tutor tutor : TutorDiscoveryFragment.tutorList){
                if(tutor.getTutorId().equals(tutorId)){
                    return tutor;
                }
            }
        }
        return null;
    }
}
