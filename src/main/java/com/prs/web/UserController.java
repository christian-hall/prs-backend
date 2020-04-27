package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.User;
import com.prs.db.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	// list method

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<User> users = userRepo.findAll();
		if (users.size() > 0) {
			jr = JsonResponse.getInstance(users);
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no users found.");
		}
		return jr;
	}
	
	// get method

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			jr = JsonResponse.getInstance(user.get());
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no user found for id: " + id);
		}
		return jr;
	}
	
	// create method

	@PostMapping("/")
	public JsonResponse createUser(@RequestBody User u) {
		JsonResponse jr = null;
		try {
			u = userRepo.save(u);
			jr = JsonResponse.getInstance(u);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace(); 
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error creating user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}
	
	
	// update method
	
	@PutMapping("/")
	public JsonResponse updateUser(@RequestBody User u) {
		JsonResponse jr = null;
		try {
			u = userRepo.save(u);
			jr = JsonResponse.getInstance(u);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error updating user: " +e.getMessage());
			e.printStackTrace();
		}
		
		return jr;
	}
	
	// delete method
	
	@DeleteMapping("/{id}")
	public JsonResponse deleteUser(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			userRepo.deleteById(id);
			jr = JsonResponse.getInstance("Deleted user with the id of " +id);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error deleting user " +id +" : " +e.getMessage());
			e.printStackTrace();
		}
		
		return jr;
		
	}
	
	//login method
	
	@PostMapping(path="/login") //you do post, not request, because
	public JsonResponse login(@RequestBody User u) { // a user is passed in on JSON
		JsonResponse jr = null;
		Optional<User> user = userRepo.findByUserNameAndPassword(u.getUserName(), u.getPassword());
		if (user.isPresent()) {
			jr = JsonResponse.getInstance(user.get());
		}
		else {
			jr = JsonResponse.getErrorInstance("Unable to comply: invalid username or password. Try again.");
		}
		
		return jr;
	}
	

}