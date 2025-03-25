package com.seoul_wifi_page.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seoul_wifi_page.dto.TbPublicWifiInfo;
import com.seoul_wifi_page.dto.TbPublicWifiInfoWrapper;
import com.seoul_wifi_page.dto.WifiRow;
import com.seoul_wifi_page.repository.IndexRepo;

/**
 * 와이파이 정보 처리와 관련된 비즈니스 로직을 담당하는 서비스 클래스입니다.
 * API 호출, 거리 계산, DB 저장 및 조회 기능을 제공합니다.
 */
public class IndexService {
	private final IndexRepo dbHelper;

	public IndexService(ServletContext servletContext) {
		this.dbHelper = new IndexRepo(servletContext);
	}

	/**
	 * 서울시 공공 와이파이 API를 호출하여 데이터를 파싱하고,
	 * DB에 삽입합니다. 사용자 위치를 기준으로 각 와이파이와의 거리를 계산하여 저장합니다.
	 *
	 * @param userLat 사용자의 위도
	 * @param userLon 사용자의 경도
	 * @throws IOException API 호출 실패 시 발생
	 */
	public void fetchAPI(double userLat, double userLon) throws IOException {
		// DB 초기화
		dbHelper.resetWifiInfo();
		dbHelper.createTable();

		MyOkHttp3 ok = new MyOkHttp3();

		int start = 1;
		int end = 1000;
		boolean hasMoreData = true;

		while (hasMoreData) {
			try {
				System.out.println("Fetching data from " + start + " to " + end);
				String result = ok.getApiResult(start, end);

				if (result == null || result.isEmpty()) {
					System.err.println("API result is null or empty.");
					break;
				}

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
						row.setDistance(distance);
						dbHelper.insertWifiInfo(row);
					}
				}

				start = end + 1;
				end += 1000;
			} catch (Exception e) {
				System.err.println("Failed to fetch data from API for range " + start + " to " + end);
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * 전체 와이파이 정보를 DB에서 조회합니다.
	 *
	 * @return 전체 와이파이 정보 리스트
	 */
	public List<WifiRow> getAllWifiInfo() {
		return dbHelper.getAllWifiInfo();
	}

	/**
	 * 가까운 순으로 상위 N개의 와이파이 정보를 조회합니다.
	 *
	 * @param limit 상위 개수 제한
	 * @return 가까운 순 와이파이 정보 리스트
	 */
	public List<WifiRow> getTopWifiInfo(int limit) {
		return dbHelper.getTopWifiInfo(limit);
	}

	/**
	 * 두 좌표 간의 거리를 계산합니다. (Haversine 공식 사용)
	 *
	 * @param lat1 위도1
	 * @param lon1 경도1
	 * @param lat2 위도2
	 * @param lon2 경도2
	 * @return 거리 (단위: km)
	 */
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // 지구 반지름 (km)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	/**
	 * 저장된 전체 와이파이 개수를 반환합니다.
	 *
	 * @return 와이파이 총 개수
	 */
	public int getWifiTotalCount() {
		return dbHelper.getWifiCount();
	}

	/**
	 * 관리자 번호를 기준으로 특정 와이파이 상세 정보를 조회합니다.
	 *
	 * @param managerNo 관리자 번호
	 * @return 해당 와이파이의 상세 정보
	 */
	public WifiRow getWifiDetailByManagerNo(String managerNo) {
	    return dbHelper.getWifiByManagerNo(managerNo);
	}
}
