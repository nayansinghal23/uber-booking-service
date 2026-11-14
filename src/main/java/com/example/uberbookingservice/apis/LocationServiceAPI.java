package com.example.uberbookingservice.apis;

import com.example.uberbookingservice.dto.DriverLocationDTO;
import com.example.uberbookingservice.dto.NearbyDriversRequestDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceAPI {
    @POST("/nearby/drivers")
    Call<DriverLocationDTO[]> getNearByDrivers(@Body NearbyDriversRequestDTO dto);
}
