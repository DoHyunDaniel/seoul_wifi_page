package com.seoul_wifi_page.service;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyOkHttp3 {
	private static final String API_KEY = ConfigUtil.getApiKey();
	private static OkHttpClient client;
	private static Request.Builder builder;

	// 생성자에서 초기화 진행
	public MyOkHttp3() {
		client = new OkHttpClient.Builder().build();
		builder = new Request.Builder();
	}

	public Request getRequest(int start, int end) throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /* URL */
		urlBuilder.append("/" + URLEncoder.encode(API_KEY, "UTF-8")); /* 인증키 (sample사용시에는 호출시 제한됩니다.) */
		urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /* 요청파일타입 (xml,xmlf,xls,json) */
		urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /* 서비스명 (대소문자 구분 필수입니다.) */
		urlBuilder.append("/" + URLEncoder.encode(String.valueOf(start), "UTF-8")); /* 요청시작위치 (sample인증키 사용시 5이내 숫자) */
		urlBuilder.append(
				"/" + URLEncoder.encode(String.valueOf(end), "UTF-8")); /* 요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨) */

//		// 서비스별 추가 요청 인자
//		urlBuilder.append("/" + URLEncoder.encode("서대문구", "UTF-8")); /* 서비스별 추가 요청인자들 */
		URL url = new URL(urlBuilder.toString());

		return builder.url(url).get().build();
	}

	public Call getOkClient(Request request) {
		return client.newCall(request);
	}

	public String getApiResult(int start, int end) throws IOException {
		Call call = getOkClient(getRequest(start, end));
		Response response = call.execute(); // 동기 요청
		if (response.isSuccessful() && response.body() != null) {
			return response.body().string();
		} else {
			throw new IOException("Unsuccessful response or null body");
		}
	}

//	// 비동기 통신을 수행
//	public CompletableFuture<Result> getApiResult(int start, int end) throws IOException {
//		CompletableFuture<Result> future = new CompletableFuture<>();
//
//		getOkClient(getRequest(start, end)).enqueue(new Callback() {
//			@Override
//			public void onFailure(@NonNull Call call, @NonNull IOException e) {
//				System.err.println("Error Occurred");
//			}
//
//			@Override
//			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//				ResponseBody body = response.body();
//				if (response.isSuccessful() && body != null) {
//					future.complete(new Gson().fromJson(body.string(), Result.class));
//				}
//			}
//		});
//
//		return future;
//	}
//
//	public int getPossibleCnt() {
//		return 10;
//	}

}
