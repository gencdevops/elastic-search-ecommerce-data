package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.model.User;

public interface UserRepository<P> extends CrudRepository<User, Long> {
	List<User> findByFirstName(String firstName);
}
