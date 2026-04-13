 package com.SnackStack.service;

import java.util.List;

import com.SnackStack.dto.RestaurantDto;
import com.SnackStack.model.Address;
import com.SnackStack.model.Restaurant;
import com.SnackStack.model.User;
import com.SnackStack.request.CreateRestaurantRequest;

public interface RestaurantService {
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) throws Exception;

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updateRestaurant) throws Exception;

    public void deleteRestaurant(Long restaurantId) throws Exception;

    public Restaurant findRestaurantById(Long id) throws Exception; 

    public List<Restaurant> getAllRestaurant() throws Exception;

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant getRestaurantByUserId(Long userId) throws Exception;

    // for updating address
    public Restaurant updateRestaurantAddress(Long restaurantId, Address address) throws Exception;

    // addtoFavorites
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

    public Restaurant updateRestaurantStatus(Long id) throws Exception;

    
    
}
