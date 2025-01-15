package com.seoul_wifi_page.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seoul_wifi_page.dto.TbPublicWifiInfo;
import com.seoul_wifi_page.dto.TbPublicWifiInfoWrapper;
import com.seoul_wifi_page.dto.WifiRow;
import com.seoul_wifi_page.repository.IndexRepo;

public class IndexService {
	private final IndexRepo dbHelper;

	public IndexService(ServletContext servletContext) {
		this.dbHelper = new IndexRepo(servletContext);
	}

	/**
	 * API 호출 및 데이터베이스 저장
	 */
	public void fetchAPI(double userLat, double userLon) throws IOException {

		// 테이블 초기화 및 데이터 삽입
		dbHelper.resetWifiInfo();
		dbHelper.createTable();

		// API 호출
		MyOkHttp3 ok = new MyOkHttp3();

//		// 소수의 데이터만
//		String result;
//		try {
//			result = ok.getApiResult(1, 1000); // API 데이터 범위 확장
//		} catch (IOException e) {
//			System.err.println("Failed to fetch data from API.");
//			e.printStackTrace();
//			return;
//		}
//
//		if (result == null || result.isEmpty()) {
//			System.err.println("API result is null or empty.");
//			return;
//		}
//
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		TbPublicWifiInfoWrapper wifiInfoWrapper = gson.fromJson(result, TbPublicWifiInfoWrapper.class);
//		TbPublicWifiInfo wifiInfo = wifiInfoWrapper.getTbPublicWifiInfo();
//
//		if (wifiInfo == null) {
//			System.err.println("Failed to parse JSON into TbPublicWifiInfo.");
//			return;
//		}
//
//		dbHelper.resetWifiInfo();
//		dbHelper.createTable();
//
//		List<WifiRow> rows = wifiInfo.getRow();
//		if (rows != null && !rows.isEmpty()) {
//			for (WifiRow row : rows) {
//				double distance = calculateDistance(userLat, userLon, row.getLatitude(), row.getLongitude());
//				row.setDistance(distance); // 거리 계산 후 저장
//				dbHelper.insertWifiInfo(row);
//			}
//		} else {
//			System.err.println("No rows found in the data.");
//		}

		// 반복문으로 공공데이터 db에 삽입
		int start = 1;
		int end = 1000;
		boolean hasMoreData = true;
		while (hasMoreData) {
			try {
				System.out.println("Fetching data from " + start + " to " + end);
				String result = ok.getApiResult(start, end);

				// API 응답이 null이거나 비어 있는지 확인
				if (result == null || result.isEmpty()) {
					System.err.println("API result is null or empty.");
					break;
				}
				// JSON 파싱
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				TbPublicWifiInfoWrapper wifiInfoWrapper = gson.fromJson(result, TbPublicWifiInfoWrapper.class);
				TbPublicWifiInfo wifiInfo = wifiInfoWrapper.getTbPublicWifiInfo();

				if (wifiInfo == null || wifiInfo.getRow() == null || wifiInfo.getRow().isEmpty()) {
					System.out.println("No more data available.");
					hasMoreData = false;
				} else {
					List<WifiRow> rows = wifiInfo.getRow();
					for (WifiRow row : rows) {
						double distance = calculateDistance(userLat, userLon, row.getLatitude(), row.getLongitude());
						row.setDistance(distance); // 거리 계산 후 저장
						dbHelper.insertWifiInfo(row);
					}
				}

				// 다음 요청 범위 설정
				start = end + 1;
				end += 1000;
			} catch (Exception e) {
				System.err.println("Failed to fetch data from API for range " + start + " to " + end);
				e.printStackTrace();
				break;
			}
		}

	}

	// SQLite 데이터베이스에서 WiFi 정보 가져오기

	public List<WifiRow> getAllWifiInfo() {
		return dbHelper.getAllWifiInfo();
	}

	// SQLite 데이터베이스에서 상위 N개의 WiFi 정보 가져오기

	public List<WifiRow> getTopWifiInfo(int limit) {
		return dbHelper.getTopWifiInfo(limit);
	}

	// Haversine 공식을 사용하여 거리 계산

	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // 지구 반경 (km)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c; // 거리 반환
	}

	// 와이파이 총 갯수 세기
	public int getWifiTotalCount() {
		return dbHelper.getWifiCount();
	}
	
	// 상세정보 구하기
	public WifiRow getWifiDetailByManagerNo(String managerNo) {
	    return dbHelper.getWifiByManagerNo(managerNo);
	}
}
