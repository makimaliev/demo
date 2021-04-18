package com.example.demo.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {

    private Long id;
    private String username;
    private String name;
    private Double balance;

    public UserProfile(Long id, String username, String name, Double balance) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.balance = balance;
    }
}
