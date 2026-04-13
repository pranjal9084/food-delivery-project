package com.SnackStack.controller;

import com.SnackStack.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SnackStack.model.Restaurant;
import com.SnackStack.request.CreateRestaurantRequest;
import com.SnackStack.response.MessageResponse;
import com.SnackStack.service.RestaurantService;
import com.SnackStack.service.UserService;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(
        @RequestBody CreateRestaurantRequest req,
        @RequestHeader("Authorization") String jwt
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.createRestaurant(req, user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }
        
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
        @RequestBody CreateRestaurantRequest req,
        @RequestHeader("Authorization") String jwt,
        @PathVariable Long id
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(
        @RequestHeader("Authorization") String jwt,
        @PathVariable Long id
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        
        restaurantService.deleteRestaurant(id);

        MessageResponse res = new MessageResponse();
        res.setMessage("Restaurant deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    
    // update restaurant status
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
        @RequestHeader("Authorization") String jwt,
        @PathVariable Long id
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }   

    // find restaurant by user id
    @GetMapping("/user")
    public ResponseEntity<Restaurant> getRestaurantByUserId(
        @RequestHeader("Authorization") String jwt
    )throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    
}
