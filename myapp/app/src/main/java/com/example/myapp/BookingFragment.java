package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;

import android.widget.*;
import androidx.annotation.*;
import java.util.*;

public class BookingFragment extends Fragment {

    private Tutor tutor;

    public BookingFragment(){
        super(R.layout.fragment_booking);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String tutorId = bundle.getString("tutorId");
        String selectedDate = bundle.getString("selectedDate");
        String selectedTime = bundle.getString("selectedTime");

        TextView tvDate = view.findViewById(R.id.TVDate);
        TextView tvTime = view.findViewById(R.id.TVTime);
        Spinner spnSubject = view.findViewById(R.id.SpnSubject);
        Spinner spnDuration = view.findViewById(R.id.SpnDuration);
        EditText etNotes = view.findViewById(R.id.ETNotes);
        Button btnConfirmBooking = view.findViewById(R.id.BtnConfirmBooking);

        //Display date in exp: Thu, 12 December 2025
        if(selectedDate != null){
            try{
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, d MMMM yyyy", Locale.getDefault());
                tvDate.setText(outputDateFormat.format(inputDateFormat.parse(selectedDate)));
            }catch(Exception e){
                tvDate.setText(selectedDate);
            }
        }
        //Display time in 12-hour format
        if(selectedTime != null){
            try{
                SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                SimpleDateFormat outputTimeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                tvTime.setText(outputTimeFormat.format(inputTimeFormat.parse(selectedTime)));
            }catch(Exception e){
                tvTime.setText(selectedTime);
            }
        }

        tutor = findTutorByTutorId(tutorId);
        //Setup spinner for subject and duration
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tutor.getSubjects());
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSubject.setAdapter(subjectAdapter);

        List<Integer> durations = getDuration(selectedDate, selectedTime, tutor.getTimeSlots(), tutor.getTimeSlotsAvailability());
        ArrayAdapter<Integer> durationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, durations);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDuration.setAdapter(durationAdapter);

        //Navigate to dialog after user clicked confirm booking button
        btnConfirmBooking.setOnClickListener(v -> {
            String selectedSubject = spnSubject.getSelectedItem().toString();
            double selectedDuration = Double.parseDouble(spnDuration.getSelectedItem().toString());
            String notes = etNotes.getText().toString();

            // Convert tutorId String to int
            int tutorIdInt = Integer.parseInt(tutorId);
            BookingConfirmationDialog dialog = new BookingConfirmationDialog(tutorIdInt, tutor.getName(), selectedDate, selectedTime, selectedSubject, selectedDuration, notes);
            dialog.show(getParentFragmentManager(), "BookingConfirmation");
        });

        view.findViewById(R.id.IVBackBooking).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
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

    //Return available consecutive duration (hour)
    private List<Integer> getDuration(String selectedDate, String selectedTime, List<String> timeSlots, List<Boolean> availability) {
        List<Integer> duration = new ArrayList<>();
        duration.add(1);

        for(int i = 0; i < timeSlots.size(); i++){
            String[] parts = timeSlots.get(i).split("\\s+");

            if(parts[0].equals(selectedDate) && parts[1].equals(selectedTime) && availability.get(i)) {
                int consecutive = 1;
                for(int j = i + 1; j < timeSlots.size(); j++){
                    parts = timeSlots.get(j).split("\\s+");
                    if(parts[0].equals(selectedDate) && availability.get(j)){
                        consecutive++;
                        duration.add(consecutive);
                    }else{
                        break;
                    }
                }
                break;
            }
        }

        return duration;
    }

}
