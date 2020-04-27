package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.Product;
import com.prs.db.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepo;

	// list method

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<Product> products = productRepo.findAll();
		if (products.size() > 0) {
			jr = JsonResponse.getInstance(products);
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no vendors found.");
		}
		return jr;
	}

	// get method

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Product> product = productRepo.findById(id);
		if (product.isPresent()) {
			jr = JsonResponse.getInstance(product.get());
		} else {
			jr = JsonResponse.getErrorInstance("Unable to comply: no product found for id: " + id);
		}
		return jr;
	}

	// create method

	@PostMapping("/")
	public JsonResponse createProduct(@RequestBody Product p) {
		JsonResponse jr = null;
		try {
			p = productRepo.save(p);
			jr = JsonResponse.getInstance(p);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error creating product: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	// update method

	@PutMapping("/")
	public JsonResponse updateCredit(@RequestBody Product p) {
		JsonResponse jr = null;
		try {
			p = productRepo.save(p);
			jr = JsonResponse.getInstance(p);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Unable to comply: error updating product: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}
	// delete method

	@DeleteMapping("/{id}")
	public JsonResponse deleteProduct(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			productRepo.deleteById(id);
			jr = JsonResponse.getInstance("Deleted product with the id of " + id);
		} catch (Exception e) {
			jr = JsonResponse
					.getErrorInstance("Unable to comply: error deleting product " + id + " : " + e.getMessage());
			e.printStackTrace();
		}

		return jr;

	}

}
