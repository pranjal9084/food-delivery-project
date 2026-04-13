package com.SnackStack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SnackStack.dto.RestaurantDto;
import com.SnackStack.model.Restaurant;
import com.SnackStack.model.User;
import com.SnackStack.service.RestaurantService;
import com.SnackStack.service.UserService;

@RestController
@RequestMapping("/api/restaurants") 
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurant(
        @RequestHeader("Authorization") String jwt
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.getAllRestaurant();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(
        @RequestHeader("Authorization") String jwt,
        @PathVariable Long id
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
        @RequestHeader("Authorization") String jwt,
        @PathVariable String keyword
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.searchRestaurant(keyword);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }


    @PutMapping("/{id}/add-favourites")
    public ResponseEntity<RestaurantDto> addToFavorite(
        @RequestHeader ("Authorization") String jwt,
        @PathVariable Long id
    )throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        RestaurantDto restaurant = restaurantService.addToFavorites(id, user);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
        
    
}
