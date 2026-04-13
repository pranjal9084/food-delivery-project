package com.SnackStack.controller;

import com.SnackStack.config.JwtProvider;
import com.SnackStack.model.Cart;
import com.SnackStack.model.USER_ROLE;
import com.SnackStack.model.User;
import com.SnackStack.repository.CartRepository;
import com.SnackStack.repository.UserRepository;
import com.SnackStack.request.LoginRequest;
import com.SnackStack.response.AuthResponse;
import com.SnackStack.service.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailService customerUserDetailService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null){
            throw new Exception("Email already used by another account. Please enter another email.");
        }
        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());

        User savedUser = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        java.util.List<GrantedAuthority> authorities = new java.util.ArrayList<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(savedUser.getRole().toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Registration successfull");
        authResponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req){

        String userName = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate(userName, password);
        Collection<? extends GrantedAuthority> authority = authentication.getAuthorities();
        String role = authority.isEmpty()?null:authority.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Signed In");
        authResponse.setRole(USER_ROLE.valueOf(role));
        return  new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @GetMapping("/hii")
    public String sayhii(){
        return "HIIIiiii";
    }
    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customerUserDetailService.loadUserByUsername(userName);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid Username and password.");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        return null;
    }
}
