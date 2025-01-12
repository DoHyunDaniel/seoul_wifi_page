package com.seoul_wifi_page.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class TbPublicWifiInfoWrapper {
    @SerializedName("TbPublicWifiInfo")
	private TbPublicWifiInfo tbPublicWifiInfo;
}
