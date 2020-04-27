package com.prs.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.*;
import com.prs.db.RequestRepository;
import com.prs.db.UserRepository;

@RestController
@RequestMapping("/requests")
public class RequestController {

	@Autowired
	private RequestRepository requestRepo;
	@Autowired
	private UserRepository userRepo;

	// list method

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<Request> requests = requestRepo.findAll();
		if (requests.size() > 0) {
			jr = JsonResponse.getInstance(requests);
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no requests found.");
		}
		return jr;
	}

	// get method

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Request> request = requestRepo.findById(id);
		if (request.isPresent()) {
			jr = JsonResponse.getInstance(request.get());
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no request found for id: " + id);
		}
		return jr;
	}

	// create method

	@PostMapping("/")
	public JsonResponse createRequest(@RequestBody Request r) {
		JsonResponse jr = null;
		r.setStatus("New");
		r.setSubmittedDate(LocalDateTime.now());
		try {
			r = requestRepo.save(r);
			jr = JsonResponse.getInstance(r);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error creating request: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	// update method

	@PutMapping("/")
	public JsonResponse updateRequest(@RequestBody Request r) {
		JsonResponse jr = null;
		r.setStatus("New");
		r.setSubmittedDate(LocalDateTime.now());
		try {
			r = requestRepo.save(r);
			jr = JsonResponse.getInstance(r);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error updating request: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	// delete method

	@DeleteMapping("/{id}")
	public JsonResponse deleteRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			requestRepo.deleteById(id);
			jr = JsonResponse.getInstance("Deleted request with the id of " + id);
		} catch (Exception e) {
			jr = JsonResponse
					.getErrorInstance("Unable to comply: error deleting request " + id + " : " + e.getMessage());
			e.printStackTrace();
		}

		return jr;

	}

	// submit request (PUT statement that only adjusts status and LocalDateTime
	@PutMapping("/submit-review")
	public JsonResponse submitRequestForReview(@RequestBody Request r) {
		JsonResponse jr = null;
		double total = r.getTotal();
		if (total >= 50) {
			r.setStatus("Review");
		} else {
			r.setStatus("Approved");
		}
		r.setSubmittedDate(LocalDateTime.now());
		try {
			r = requestRepo.save(r);
			jr = JsonResponse.getInstance(r);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error submitting request: " + e.getMessage());
			e.printStackTrace();
		}
		return jr;
	}

	// get a list of requests where user is not the user

	@GetMapping("/list-review/{id}")
	public JsonResponse getUser(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			List<Request> requests = requestRepo.findAllByStatusAndUserNot("Review", user);
			if (requests.size() > 0) {
				jr = JsonResponse.getInstance(requests);
			} else {
				jr = JsonResponse.getErrorInstance("No requests found for user id" + id + ".");
			}
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no user found for id " + id + ".");
		}

		return jr;
	}

	// set a specific requests for accept (put request

	@PutMapping("/approve")
	public JsonResponse approveSubmittedRequest(@RequestBody Request r) {

		// need to maintain LocalDateTime and change status to accept
		JsonResponse jr = null;
		r.setStatus("Approved");
		try {
			r = requestRepo.save(r);
			jr = JsonResponse.getInstance(r);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error submitting request: " + e.getMessage());
			e.printStackTrace();
		}
		return jr;
	}

	// set specific requests for reject (put request)

	@PutMapping("/reject")
	public JsonResponse rejectSubmittedRequest(@RequestBody Request r) {

		// need to maintain LocalDateTime and change status to accept
		JsonResponse jr = null;
		r.setStatus("Rejected");
		try {
			r = requestRepo.save(r);
			jr = JsonResponse.getInstance(r);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error submitting request: " + e.getMessage());
			e.printStackTrace();
		}
		return jr;
	}
}
