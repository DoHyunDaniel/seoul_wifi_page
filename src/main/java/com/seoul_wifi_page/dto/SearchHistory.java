package com.seoul_wifi_page.dto;

import lombok.Data;

@Data
public class SearchHistory {
	private int id;
	private double latitude;
	private double longitude;
	private String searchDate;
}
