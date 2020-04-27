package com.prs.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.business.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

}
