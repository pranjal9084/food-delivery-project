package com.SnackStack.request;

import java.time.LocalDateTime;
import java.util.List;

import com.SnackStack.model.Address;
import com.SnackStack.model.ContactInformation;

import lombok.Data;

@Data
public class  CreateRestaurantRequest {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
    // private LocalDateTime registrationDate;
}
