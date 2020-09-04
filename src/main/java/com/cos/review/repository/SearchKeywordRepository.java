package com.cos.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.review.model.SearchKeyword;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Integer>{

	SearchKeyword findByKeyword(String keyword);

}
