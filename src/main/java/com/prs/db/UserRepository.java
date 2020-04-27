package com.prs.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	//login method
	Optional<User> findByUserNameAndPassword(String userName, String password);
	//SQL: select * from user where username = __________ and password = ________________.
	

}
