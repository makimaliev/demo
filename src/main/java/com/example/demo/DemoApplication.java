package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	MainService service;

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User("test1 test1", "test1", "test123");
		user1.setPassword(passwordEncoder.encode(user1.getPassword()));
		service.register(user1);

		User user2 = new User("test2 test2", "test2", "test123");
		user2.setPassword(passwordEncoder.encode(user2.getPassword()));
		service.register(user2);
	}
}
