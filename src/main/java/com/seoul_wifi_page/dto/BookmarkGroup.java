package com.seoul_wifi_page.dto;

import lombok.Data;

@Data
public class BookmarkGroup {
	private Integer groupId;
	private String groupName;
	private Integer groupOrder;
	private String createdAt;
	private String editedAt;
}
