package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WifiInfo {
	private String mgrNo;
	private String district;
	private String mainName;
	private String address1;
	private String address2;
	private String installType;
	private String serviceType;
	private String installBy;
	private String year;
	private String indoorOutdoor;
	private double latitude;
	private double longitude;

	// Constructor, Getters, and Setters
	public WifiInfo(String mgrNo, String district, String mainName, String address1, String address2,
			String installType, String serviceType, String installBy, String year, String indoorOutdoor,
			double latitude, double longitude) {
		this.mgrNo = mgrNo;
		this.district = district;
		this.mainName = mainName;
		this.address1 = address1;
		this.address2 = address2;
		this.installType = installType;
		this.serviceType = serviceType;
		this.installBy = installBy;
		this.year = year;
		this.indoorOutdoor = indoorOutdoor;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "WifiInfo{" + "mgrNo='" + mgrNo + '\'' + ", district='" + district + '\'' + ", mainName='" + mainName
				+ '\'' + ", address1='" + address1 + '\'' + ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}
}

public class App {
	public static void main(String[] args) throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /* URL */
		urlBuilder.append(
				"/" + URLEncoder.encode("5678486a467a697038364c416c4951", "UTF-8")); /* 인증키 (sample사용시에는 호출시 제한됩니다.) */
		urlBuilder.append("/" + URLEncoder.encode("xml", "UTF-8")); /* 요청파일타입 (xml,xmlf,xls,json) */
		urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /* 서비스명 (대소문자 구분 필수입니다.) */
		urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); /* 요청시작위치 (sample인증키 사용시 5이내 숫자) */
		urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8")); /* 요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨) */
		// 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

		// 서비스별 추가 요청 인자
		urlBuilder.append("/" + URLEncoder.encode("서대문구", "UTF-8")); /* 서비스별 추가 요청인자들 */

		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/xml");
		System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다. */
		BufferedReader rd;

		// 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		rd.close();
		conn.disconnect();
		System.out.println(sb.toString());
	}

}
