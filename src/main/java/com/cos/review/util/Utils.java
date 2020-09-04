package com.cos.review.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

	public static String dayParse(String day) {
		try {
			if (day.contains("일 전")) {
				return LocalDate.now().minusDays(Integer.parseInt(day.charAt(0) + "")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} else if (day.contains("시간 전")) {
				return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} else if (day.contains("어제")) {
				return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} else {
				return day.substring(0, day.length() - 1).replace(".", "-");
			}
		} catch (Exception e) {
			return day;
		}
	}
}
