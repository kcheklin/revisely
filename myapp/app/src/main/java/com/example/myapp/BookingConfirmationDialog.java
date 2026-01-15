package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;

import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import java.util.*;

public class BookingConfirmationDialog extends DialogFragment {

    private int tutorId;
    private String tutorName;
    private String date;
    private String startTime;
    private double duration;
    private String subject;
    private String notes;

    public BookingConfirmationDialog(int tutorId, String tutorName, String date, String startTime, String subject, double duration, String notes) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.date = date;
        this.startTime = startTime;
        this.subject = subject;
        this.duration = duration;
        this.notes = notes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_booking_confirmation, container, false);

        TextView tvConfirm1 = view.findViewById(R.id.TVConfirm1);
        TextView tvConfirm2 = view.findViewById(R.id.TVConfirm2);
        Button btnBackHome = view.findViewById(R.id.BtnBackHome);

        String formattedDate = formatDate(date);
        String formattedTime = formatTime(startTime, duration);
        tvConfirm1.setText("Your request to book a session with tutor " + tutorName + " has been sent");
        tvConfirm2.setText("Subject: " + subject + "\nDate: " + formattedDate + "\nTime: " + formattedTime + "\nAdditional Notes: " + ((notes == null || notes.trim().isEmpty()) ? "-" : notes));

        // Create the booking via API
        createBooking();

        btnBackHome.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), StudentHomepageActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout(
                    (int)(350 * getResources().getDisplayMetrics().density),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private String formatDate(String date){
        try{
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMMM yyyy", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(date));
        }catch(Exception e){
            e.printStackTrace();
            return date;
        }
    }

    //Display time in exp: 2:00 PM - 3:00 PM
    private String formatTime(String startTime, double duration){
        try{
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            Date startDate = inputFormat.parse(startTime);
            long startMillis = startDate.getTime();
            long endMillis = startMillis + (long)(duration * 60 * 60 * 1000);
            Date endDate = new Date(endMillis);
            return outputFormat.format(startDate) + " - " + outputFormat.format(endDate);
        }catch(Exception e){
            e.printStackTrace();
            return startTime + " - ?";
        }
    }

    private void createBooking() {
        // Get user ID from session manager if available
        com.example.myapp.utils.SessionManager sessionManager = new com.example.myapp.utils.SessionManager(requireContext());
        Integer userId = null;
        String studentName = null;

        // Try to get user ID from session
        if (sessionManager.getToken() != null) {
            userId = sessionManager.getUserId();
            studentName = sessionManager.getUserName();
        }

        // Format time to HH:mm:ss format expected by backend
        String formattedTime = startTime + ":00";

        // Create booking request
        com.example.myapp.models.BookingRequest bookingRequest = 
            new com.example.myapp.models.BookingRequest(tutorId, userId, studentName, subject, date, formattedTime);
        bookingRequest.setNotes(notes);

        // Call the API
        com.example.myapp.api.RetrofitClient.getApiService().createBooking(bookingRequest)
            .enqueue(new retrofit2.Callback<com.example.myapp.models.BookingResponse>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.myapp.models.BookingResponse> call, 
                                     retrofit2.Response<com.example.myapp.models.BookingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        com.example.myapp.models.BookingResponse bookingResponse = response.body();
                        if (!bookingResponse.hasError()) {
                            // Booking successful
                            android.util.Log.d("BookingConfirmation", "Booking created successfully with ID: " + bookingResponse.getId());
                        } else {
                            // Handle error from backend
                            android.util.Log.e("BookingConfirmation", "Error creating booking: " + bookingResponse.getError());
                            Toast.makeText(getContext(), "Booking error: " + bookingResponse.getError(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        android.util.Log.e("BookingConfirmation", "Failed to create booking: " + response.code());
                        // Still show confirmation dialog even if API fails
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.myapp.models.BookingResponse> call, Throwable t) {
                    android.util.Log.e("BookingConfirmation", "API call failed: " + t.getMessage());
                    t.printStackTrace();
                    // Still show confirmation dialog even if API fails
                }
            });
    }

}

