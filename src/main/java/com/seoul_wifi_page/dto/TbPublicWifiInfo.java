package com.seoul_wifi_page.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class TbPublicWifiInfo {
	@SerializedName("list_total_count")
	private int listTotalCount;

	@SerializedName("RESULT")
	private Result result;

	@SerializedName("row")
	private List<WifiRow> row;

	@Data
	public static class Result {
		@SerializedName("CODE")
		private String code;

		@SerializedName("MESSAGE")
		private String message;
	}
}