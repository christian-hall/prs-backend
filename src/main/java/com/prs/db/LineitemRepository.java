package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.Lineitem;
import com.prs.business.Request;

public interface LineitemRepository extends JpaRepository<Lineitem, Integer> {
	List<Lineitem> findAllByRequest(Optional<Request> request);
	List<Lineitem> findAllByRequest(Request request);

}
