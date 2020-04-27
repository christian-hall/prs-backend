package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.Request;
import com.prs.business.User;

public interface RequestRepository extends JpaRepository<Request, Integer> {
	List<Request> findAllByStatusAndUserNot(String status, Optional<User> user);
	

}
