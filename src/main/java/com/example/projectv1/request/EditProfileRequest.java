package com.example.projectv1.request;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String country;
    private String state;
    private String city;
    private String address;
    private String gender;
}
