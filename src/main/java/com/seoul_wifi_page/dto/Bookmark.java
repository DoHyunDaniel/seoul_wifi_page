package com.seoul_wifi_page.dto;

import lombok.Data;

@Data
public class Bookmark {
	// wifiInfo
    private String managerNo;
    private String wardOffice;
    private String mainName;
    private String address1;
    private String address2;
    private String instlFloor;
    private String instlTy;
    private String instlMby;
    private String svcSe;
    private String cmcwr;
    private String cnstcYear;
    private String inoutDoor;
    private String remars3;
    private double latitude;
    private double longitude;
    private String workDttm;
    private double distance;
    
    // bookmark
    private int bookmarkId;
    private String createdAt;
    private String editedAt;
    private int groupId;
    
    // bookmarkGroup
	private String groupName;
}
