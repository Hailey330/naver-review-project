package com.cos.review.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SearchKeyword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String keyword;
	
	@OneToMany(mappedBy = "keyword", orphanRemoval = true)
	private List<Product> products;
}
