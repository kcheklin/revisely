package com.example.myapp.api;

import com.example.myapp.Tutor;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/tutors")
    Call<List<Tutor>> getTutors();

    @GET("api/tutors/{id}")
    Call<Tutor> getTutorDetails(@Path("id") int id);
}
