package com.seoul_wifi_page.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WifiRow {
    @SerializedName("X_SWIFI_MGR_NO")
    private String managerNo;

    @SerializedName("X_SWIFI_WRDOFC")
    private String wardOffice;

    @SerializedName("X_SWIFI_MAIN_NM")
    private String mainName;

    @SerializedName("X_SWIFI_ADRES1")
    private String address1;

    @SerializedName("X_SWIFI_ADRES2")
    private String address2;

    @SerializedName("X_SWIFI_INSTL_FLOOR")
    private String instlFloor;

    @SerializedName("X_SWIFI_INSTL_TY")
    private String instlTy;

    @SerializedName("X_SWIFI_INSTL_MBY")
    private String instlMby;

    @SerializedName("X_SWIFI_SVC_SE")
    private String svcSe;

    @SerializedName("X_SWIFI_CMCWR")
    private String cmcwr;

    @SerializedName("X_SWIFI_CNSTC_YEAR")
    private String cnstcYear;

    @SerializedName("X_SWIFI_INOUT_DOOR")
    private String inoutDoor;

    @SerializedName("X_SWIFI_REMARS3")
    private String remars3;

    @SerializedName("LAT")
    private double latitude;

    @SerializedName("LNT")
    private double longitude;

    @SerializedName("WORK_DTTM")
    private String workDttm;
    
    @SerializedName("distance")
    private double distance;          // 거리 (추가 필드)
}
