package com.SnackStack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SnackStack.dto.RestaurantDto;
import com.SnackStack.model.Address;
import com.SnackStack.model.Restaurant;
import com.SnackStack.model.User;
import com.SnackStack.repository.AddressRepository;
import com.SnackStack.repository.RestaurantRepository;
import com.SnackStack.repository.UserRepository;
import com.SnackStack.request.CreateRestaurantRequest;

@Service
public class RestaurantServiceImp implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) throws Exception {
        Restaurant exist = restaurantRepository.findByOwnerId(user.getId());
        if (exist != null) {
            throw new Exception("Restaurant already exists for this owner");
        }

        Address savedAddress = addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(savedAddress);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);
        restaurant.setOpen(true);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updateRestaurant) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (updateRestaurant.getCuisineType() != null) {
            restaurant.setCuisineType(updateRestaurant.getCuisineType());
        }
        if (updateRestaurant.getDescription() != null) {
            restaurant.setDescription(updateRestaurant.getDescription());
        }
        if (updateRestaurant.getName() != null) {
            restaurant.setName(updateRestaurant.getName());
        }
        if (updateRestaurant.getOpeningHours() != null) {
            restaurant.setOpeningHours(updateRestaurant.getOpeningHours());
        }
        if (updateRestaurant.getContactInformation() != null) {
            restaurant.setContactInformation(updateRestaurant.getContactInformation());
        }
        if (updateRestaurant.getImages() != null && !updateRestaurant.getImages().isEmpty()) {
            restaurant.setImages(updateRestaurant.getImages());
        }
        
        if (updateRestaurant.getAddress() != null) {
            Address address = restaurant.getAddress();
            if (address == null) {
                address = new Address();
                restaurant.setAddress(address);
            }
            Address updatedAddress = updateRestaurant.getAddress();
            if (updatedAddress.getStreetAddress() != null) {
                address.setStreetAddress(updatedAddress.getStreetAddress());
            }
            if (updatedAddress.getCity() != null) {
                address.setCity(updatedAddress.getCity());
            }
            if (updatedAddress.getState() != null) {
                address.setState(updatedAddress.getState());
            }
            if (updatedAddress.getPostalCode() != null) {
                address.setPostalCode(updatedAddress.getPostalCode());
            }
            if (updatedAddress.getCountry() != null) {
                address.setCountry(updatedAddress.getCountry());
            }
            addressRepository.save(address);
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> opt = restaurantRepository.findById(id);
        if (opt.isEmpty()) {
            throw new Exception("Restaurant not found with id " + id);
        }
        return opt.get();
    }

    @Override
    public List<Restaurant> getAllRestaurant() throws Exception {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found with owner id " + userId);
        }
        return restaurant;
    }

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        
        RestaurantDto dto = new RestaurantDto();
        dto.setDescription(restaurant.getDescription());
        dto.setImages(restaurant.getImages());
        dto.setTitle(restaurant.getName());
        dto.setId(restaurantId);

        boolean isFavorited = false;
        List<RestaurantDto> favorites = user.getFavourite();
        for (RestaurantDto favorite : favorites) {
            if (favorite.getId().equals(restaurantId)) {
                isFavorited = true;
                break;
            }
        }

        if (isFavorited) {
            favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        } else {
            favorites.add(dto);
        }

        userRepository.save(user);
        return dto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurantAddress(Long restaurantId, Address address) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        Address existingAddress = restaurant.getAddress();
        if (existingAddress == null) {
            existingAddress = new Address();
            restaurant.setAddress(existingAddress);
        }
        if (address.getStreetAddress() != null) {
            existingAddress.setStreetAddress(address.getStreetAddress());
        }
        if (address.getCity() != null) {
            existingAddress.setCity(address.getCity());
        }
        if (address.getState() != null) {
            existingAddress.setState(address.getState());
        }
        if (address.getPostalCode() != null) {
            existingAddress.setPostalCode(address.getPostalCode());
        }
        if (address.getCountry() != null) {
            existingAddress.setCountry(address.getCountry());
        }
        addressRepository.save(existingAddress);
        return restaurantRepository.save(restaurant);
    }

}
