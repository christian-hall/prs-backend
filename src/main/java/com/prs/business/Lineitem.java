package com.prs.business;

import javax.persistence.*;

@Entity
public class Lineitem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "requestID")
	private Request request;
	//need many-to-one
	@ManyToOne
	@JoinColumn(name = "productID")
	private Product product;
	private int quantity;
	
	// fully loaded constructor
	public Lineitem(int id, Request request, Product product, int quanitity) {
		super();
		this.id = id;
		this.request = request;
		this.product = product;
		this.quantity = quantity;
	}

	//empty constructor
	public Lineitem() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuanitity(int quantity) {
		this.quantity = quantity;
	}

	//toString
	@Override
	public String toString() {
		return "Lineitem [id=" + id + ", request=" + request + ", product=" + product + ", quanitity="
				+ quantity + "]";
	}
	
	
	
	

}
