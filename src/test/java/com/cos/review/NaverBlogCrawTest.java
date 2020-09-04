package com.cos.review;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import com.cos.review.model.Product;

// http://hare.kr/222065140009
// https://search.naver.com/search.naver?date_from=&date_option=0&date_to=&dup_remove=1&nso=&post_blogurl=&post_blogurl_without=&query=%EA%B0%A4%EB%9F%AD%EC%8B%9C20&sm=tab_pge&srchby=all&st=sim&where=post&start=41
// 섬네일, 블로그주소, 제목, 날짜
public class NaverBlogCrawTest {

	@Test
	public void 날짜_파싱() {

		String today = LocalDate.now().toString();
		System.out.println(today);

		LocalDate todayLocalDate = LocalDate.now();
		System.out.println(todayLocalDate.minusDays(1));

		String day = todayLocalDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

		String date1 = "1일 전";

		if (date1.contains("일 전")) {
			char minusDay = date1.charAt(0);
			System.out.println(minusDay);
			String date1Temp = LocalDate.now().minusDays(Integer.parseInt(minusDay + ""))
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			System.out.println("date1Temp : " + date1Temp);
		}

		String date2 = "2시간 전";

		if (date2.contains("시간 전")) {
			String date2Temp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			System.out.println("date2Temp : " + date2Temp);
		}

		String date3 = "어제";

		if (date3.contains("어제")) {
			String date3Temp = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			System.out.println("date3Temp : " + date3Temp);
		}

		String date4 = "2020.08.01.";
		char date4End = date4.charAt(date4.length() - 1);
		if (date4End == '.') {
			String date4Temp = date4.substring(0, date4.length() - 1);
			date4Temp = date4Temp.replace(".", "-");
			System.out.println("date4Temp : " + date4Temp);
		}

	}

	@Test
	public void 제품리뷰_블로그_크롤링() {
		int start = 1; //10씩 증가하면 됨.
		List<Product> products = new ArrayList<>();
		while(products.size() < 1) {
			String url = "https://search.naver.com/search.naver?date_from=&date_option=0&date_to=&dup_remove=1&nso=&post_blogurl=&post_blogurl_without=&query=%EB%A7%A5%EB%B6%81%ED%94%84%EB%A1%9C&sm=tab_pge&srchby=all&st=sim&where=post&start="+start;
			
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
					if(els3.size() <= i) {
						product.setThumnail("사진없음");
					}else {
						product.setThumnail(els3.get(i).attr("src"));	
					}
					products.add(product);
				}
				System.out.println("start : "+start);
				start = start + 10;
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		for (Product product : products) {
			System.out.println("제목 : "+product.getTitle());
		}
	}
//	}

	// @Test
//	public void 제품리뷰_크롤링() {
//		String keyword = "갤럭시20";
//		String url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query="+keyword+"&oquery=%EA%B0%A4%EB%9F%AD%EC%8B%9C+%EB%85%B8%ED%8A%B8+20&tqi=Uz88Lsp0YihsssC770RssssstG8-057521";
//		try {
//			Document doc = Jsoup.connect(url).get();
//			Elements els = doc.select(".blog .sh_blog_top a");
//			for (Element el : els) {
//				System.out.println(el.attr("title"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(url);
//	}
}
