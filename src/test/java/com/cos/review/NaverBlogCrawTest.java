package com.cos.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import com.cos.review.model.Product;

// http://hare.kr/222065140009
// https://search.naver.com/search.naver?date_from=&date_option=0&date_to=&dup_remove=1&nso=&post_blogurl=&post_blogurl_without=&query=%EA%B0%A4%EB%9F%AD%EC%8B%9C20&sm=tab_pge&srchby=all&st=sim&where=post&start=41
// 섬네일, 블로그주소, 제목, 날짜
public class NaverBlogCrawTest {

	@Test
	public void 제품리뷰_블로그_크롤링() {
		
		int start = 1; //10씩 증가하면 됨. 1, 11, 21, 31, 41 ... 
		String url = "https://search.naver.com/search.naver?date_from=&date_option=0&date_to=&dup_remove=1&nso=&post_blogurl=&post_blogurl_without=&query=%EA%B0%A4%EB%9F%AD%EC%8B%9C20&sm=tab_pge&srchby=all&st=sim&where=post&start="+start;

		List<Product> products = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements els1 = doc.select(".blog .sh_blog_top .sh_blog_title");
			Elements els2 = doc.select(".blog .sh_blog_top .txt_inline");
			Elements els3 = doc.select(".blog .sh_blog_top .sp_thmb img");
			for (int i=0; i<els1.size(); i++) {
				Product product = new Product();
				product.setTitle(els1.get(i).attr("title"));
				product.setBlogUrl(els1.get(i).attr("href"));
				product.setDay(els2.get(i).text());
				product.setThumnail(els3.get(i).attr("src"));
				products.add(product);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Product product : products) {
			System.out.println("===========================");
			System.out.println("제목 : "+product.getTitle());
			System.out.println("주소 : "+product.getBlogUrl());
			System.out.println("섬네일 : "+product.getThumnail());
			System.out.println("날짜 : "+product.getDay());
			System.out.println();
		}
		System.out.println(url);
	}
	
	//@Test
	public void 제품리뷰_크롤링() {
		String keyword = "갤럭시20";
		String url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query="+keyword+"&oquery=%EA%B0%A4%EB%9F%AD%EC%8B%9C+%EB%85%B8%ED%8A%B8+20&tqi=Uz88Lsp0YihsssC770RssssstG8-057521";
		try {
			Document doc = Jsoup.connect(url).get();
			Elements els = doc.select(".blog .sh_blog_top a");
			for (Element el : els) {
				System.out.println(el.attr("title"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(url);
	}
}
