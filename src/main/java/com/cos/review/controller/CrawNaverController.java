package com.cos.review.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.review.model.Product;
import com.cos.review.model.SearchKeyword;
import com.cos.review.repository.ProductRepository;
import com.cos.review.repository.SearchKeywordRepository;
import com.cos.review.util.CrawNaverBlog;

@Controller
public class CrawNaverController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SearchKeywordRepository searchKeywordRepository;

	@GetMapping("/product")
	public @ResponseBody List<Product> product() {
		int keywordId = searchKeywordRepository.findAll().get(0).getId();
		return productRepository.mFindProductAll(keywordId);
	}

	@GetMapping("/product/{keywordId}")
	public @ResponseBody List<Product> productKeyword(@PathVariable int keywordId) {
		return productRepository.mFindProductAll(keywordId);
	}

	@GetMapping("/searchKeyword")
	public @ResponseBody List<SearchKeyword> searchKeyword() {
		System.out.println("searchkeyword 호출됨");
		return searchKeywordRepository.findAll();
	}

	@GetMapping({"/", "/craw/naver"})
	public String crawNaver(Model model) {
		model.addAttribute("keywords", searchKeywordRepository.findAll());
		return "craw_naver";
	}

	@GetMapping("/craw/list")
	public String crawList(Model model) {
		model.addAttribute("keywords", searchKeywordRepository.findAll());
		return "craw_list";
	}

	@GetMapping("/craw/clear")
	public String crawClear(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

		int keywordId = searchKeywordRepository.findAll().get(0).getId();
		Page<Product> products = productRepository.findByKeywordId(keywordId, pageable);
		model.addAttribute("products", products.getContent());
		model.addAttribute("prev", products.getPageable().getPageNumber()-1);
		model.addAttribute("next", products.getPageable().getPageNumber()+1);
		model.addAttribute("keywordId", keywordId);
		model.addAttribute("allKeyword", searchKeywordRepository.findAll());
		return "craw_clear";
	}
	
	@GetMapping("/craw/clear/{keywordId}")
	public String crawClearKeyword(@PathVariable int keywordId, Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<Product> products = productRepository.findByKeywordId(keywordId, pageable);
		model.addAttribute("products", products.getContent());
		model.addAttribute("prev", products.getPageable().getPageNumber()-1);
		model.addAttribute("next", products.getPageable().getPageNumber()+1);
		model.addAttribute("keywordId", keywordId);
		model.addAttribute("allKeyword", searchKeywordRepository.findAll());
		return "craw_clear";
	}

	@PostMapping("/craw/naver/proc")
	public @ResponseBody String crawNaverProc(String keyword) {
		System.out.println(keyword);
		List<Product> products = new CrawNaverBlog().startAllCraw(keyword);
		SearchKeyword searchKeywordEntity = searchKeywordRepository.findByKeyword(keyword);

		for (Product product : products) {
			product.setKeyword(searchKeywordEntity);
		}

		productRepository.saveAll(products);
		return "크롤링 데이터 저장 성공";
	}

	@PostMapping("/craw/keyword/proc")
	public String crawKeywordProc(String keyword) {
		SearchKeyword entity = SearchKeyword.builder().keyword(keyword).build();
		searchKeywordRepository.save(entity);
		return "redirect:/craw/list";
	}

	@DeleteMapping("/craw/keyword/delete/{id}")
	public ResponseEntity<?> crawKeywordDelete(@PathVariable int id) {
		searchKeywordRepository.deleteById(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	// 스프링 스케줄!!
	public List<Product> startDayCraw(String keyword) {
		int start = 1; //10씩 증가하면 됨.
		List<Product> products = new ArrayList<>();

		// 개수 제한
		while (products.size() < 100) {
			String url = "https://search.naver.com/search.naver?&date_option=0&date_to=&dup_remove=1&nso=&query=" + keyword
					+ "&sm=tab_pge&srchby=all&st=date&where=post&start=" + start;

			try {
				Document doc = Jsoup.connect(url).get();
				Elements els1 = doc.select(".blog .sh_blog_top .sh_blog_title");
				Elements els2 = doc.select(".blog .sh_blog_top .txt_inline");
				Elements els3 = doc.select(".blog .sh_blog_top .sp_thmb img");
				for (int i = 0; i < els1.size(); i++) {
					Product product = new Product();
					product.setTitle(els1.get(i).attr("title"));
					product.setBlogUrl(els1.get(i).attr("href"));
					product.setDay(els2.get(i).text());
					if (els3.size() <= i) {
						product.setThumnail("사진없음");
					} else {
						product.setThumnail(els3.get(i).attr("src"));
					}
					// 오늘 날짜 것만 크롤링 데이터에서 걸러내기
					if (product.getDay().equals(LocalDate.now().toString())) {
						products.add(product);
					}

				}
				System.out.println("start : " + start);
				start = start + 10;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return products;
	}

}
