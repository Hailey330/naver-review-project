package com.cos.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.review.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query(value = "SELECT * FROM product WHERE keywordId = ?1 ORDER BY day DESC", nativeQuery = true)
	List<Product> mFindProductAll(int keywordId);

	Page<Product> findByKeywordId(int keywordId, Pageable pageable);
}
