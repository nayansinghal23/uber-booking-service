package com.example.uberbookingservice.dto;

import com.example.uberentityservice.enums.UserType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllBookingsRequestDTO {
    private Long userId;
    private UserType userType;
}
