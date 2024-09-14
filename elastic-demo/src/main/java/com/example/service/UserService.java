package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository<User> userRepository;

	@Transactional
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	@Transactional
	public List<User> findByName(String name) {
		return userRepository.findByFirstName(name);
	}

	@Transactional
	public Optional<User> getById(Long id) {
		return userRepository.findById(id);
	}

	@Transactional
	public void deleteUser(Long UserId) {
		userRepository.deleteById(UserId);
	}

	@Transactional
	public boolean addUser(User User) {
		return userRepository.save(User) != null;
	}

	@Transactional
	public boolean updateUser(User User) {
		return userRepository.save(User) != null;
	}

}
