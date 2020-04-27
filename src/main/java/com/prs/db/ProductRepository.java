package com.prs.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
