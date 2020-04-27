package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.*;
import com.prs.db.*;

@RestController
@RequestMapping("/line-items")
public class LineitemController {

	@Autowired
	private LineitemRepository lineitemRepo;
	@Autowired
	private RequestRepository requestRepo;
	@Autowired

	// list method

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<Lineitem> lineitems = lineitemRepo.findAll();
		if (lineitems.size() > 0) {
			jr = JsonResponse.getInstance(lineitems);
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no lineitems found.");
		}
		return jr;
	}

	// get method

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Lineitem> lineitem = lineitemRepo.findById(id);
		if (lineitem.isPresent()) {
			jr = JsonResponse.getInstance(lineitem.get());
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no lineitem found for id: " + id);
		}
		return jr;
	}

	// create method

	@PostMapping("/")
	public JsonResponse createLineitem(@RequestBody Lineitem l) {
		JsonResponse jr = null;
		try {
			l = lineitemRepo.save(l);
			jr = JsonResponse.getInstance(l);
			recalculateRequestTotal(l.getRequest());
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error creating lineitem: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	// update method

	@PutMapping("/")
	public JsonResponse updateLineitem(@RequestBody Lineitem l) {
		JsonResponse jr = null;
		try {
			l = lineitemRepo.save(l);
			jr = JsonResponse.getInstance(l);
			recalculateRequestTotal(l.getRequest());
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error updating lineitem: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	// delete method

	@DeleteMapping("/{id}")
	public JsonResponse deleteLineitem(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Lineitem l = lineitemRepo.getOne(id);
			lineitemRepo.deleteById(id);
			jr = JsonResponse.getInstance("Deleted lineitem with the id of " + id);
			recalculateRequestTotal(l.getRequest());
		} catch (Exception e) {
			jr = JsonResponse
					.getErrorInstance("Unable to comply: error deleting lineitem " + id + " : " + e.getMessage());
			e.printStackTrace();
		}

		return jr;

	}

	//get a list of line-items for a specific request
	@GetMapping("/lines-for-pr/{id}")
	public JsonResponse getRequestLineItems(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Request> request = requestRepo.findById(id);
		if (request.isPresent()) {
			jr = JsonResponse.getInstance(request);
			List<Lineitem> lineitems = lineitemRepo.findAllByRequest(request);
			if (lineitems.size() > 0) {
				jr = JsonResponse.getInstance(lineitems);
			}
			else {
				jr = JsonResponse.getInstance("Unable to comply: no lineitems under request id " +id);
			}
		}
		else {
			jr = JsonResponse.getInstance("Unable to comply: Request id " + id + " is unavailable.");
		}
		return jr;
	}
	//method that will calculate the total of a lineitem quantity and product price
	private double calculateItemTotal(Product product, int quantity) {
		double total = 0.0;
		double price = product.getPrice();
		total = (quantity * price);
		return total;
	}
	
	//method will recalculate the total and save it to a specific request
	private void recalculateRequestTotal(Request request) {
		//get a list of lineitems, for a specific request
		List<Lineitem> lineitems = lineitemRepo.findAllByRequest(request);
		double newTotal = 0.0;
		double productTotal = 0.0;
		Product product = null;
		int quanitity = 0;
		//loop through list to get the total
		for (Lineitem li: lineitems) {
			product = li.getProduct();
			quanitity = li.getQuantity();
			productTotal = calculateItemTotal(product, quanitity);
			newTotal += productTotal;
		}
		//save that total in the instance Request
		request.setTotal(newTotal);
		try {
			requestRepo.save(request);	
		}
		catch (Exception e) {
			throw e;
		}
		
	}
}
