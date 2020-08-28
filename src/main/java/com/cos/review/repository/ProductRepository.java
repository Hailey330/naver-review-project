package com.cos.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.review.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
