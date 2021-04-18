package com.example.demo.service;

import com.example.demo.exception.AppException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.UserProfile;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class MainService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;
    private final LogoutService logoutService;

    public MainService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtTokenProvider tokenProvider,
                       HttpServletRequest request,
                       LoginAttemptService loginAttemptService,
                       LogoutService logoutService){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.request = request;
        this.loginAttemptService = loginAttemptService;
        this.logoutService = logoutService;
    }

    public String authenticate(LoginRequest loginRequest) {

        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new BadRequestException("User is blocked!");
        }

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return tokenProvider.generateToken(authentication);
        }
        catch (BadCredentialsException e){
            throw new UnauthorizedException(e.getMessage());
        }
        catch (Exception e){
            throw new AppException(e.getMessage());
        }
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public UserProfile getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getBalance());
    }

    public void logoutUser(HttpServletRequest request) {
        String key;
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            key = request.getRemoteAddr();
        }
        else{
            key = xfHeader.split(",")[0];
        }

        this.logoutService.logoutSucceeded(key);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Transactional
    public UserProfile makePayment(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        user.setBalance(round(user.getBalance() - 1.1, 1));

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getBalance());
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
