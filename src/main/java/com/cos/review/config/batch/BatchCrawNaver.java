package com.cos.review.config.batch;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cos.review.model.Product;
import com.cos.review.model.SearchKeyword;
import com.cos.review.repository.ProductRepository;
import com.cos.review.repository.SearchKeywordRepository;
import com.cos.review.util.CrawNaverBlog;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BatchCrawNaver {

	private final SearchKeywordRepository searchKeywordRepository;
	private final ProductRepository productRepository;

	// 초(0-59)   분(0-59)   시간(0-23)   일(1-31)   월(1-12)   요일(0-7) 
	@Scheduled(cron = "0 0 3 * * *") // 매일 새벽3시 크롤링 시작
	public void cronJob() {
		System.out.println("=============자동 크롤링 배치 시작=============");
		List<Product> products = null;
		List<SearchKeyword> searchKeywords = searchKeywordRepository.findAll();

		CrawNaverBlog crawNaverBlog = new CrawNaverBlog();
		for (SearchKeyword searchKeyword : searchKeywords) {
			products = crawNaverBlog.startDayCraw(searchKeyword.getKeyword());

			SearchKeyword searchKeywordEntity = searchKeywordRepository.findByKeyword(searchKeyword.getKeyword());

			for (Product product : products) {
				product.setKeyword(searchKeywordEntity);
			}

			productRepository.saveAll(products);
		}
		System.out.println("=============자동 크롤링 배치 종료===========");
	}
}