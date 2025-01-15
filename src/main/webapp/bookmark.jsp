<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 정보</title>
<link rel="stylesheet" href="css/style.css?v=1.0">
</head>
<body>
	<div class="container">
		<h1>북마크 정보</h1>
		<nav>
			<p>
				<a href="#" id="home">홈</a> | <a
					href="/be1_java_web_study01/searchHistory.jsp" id="history">위치
					히스토리 목록</a> | <a href="/be1_java_web_study01/bookmark.jsp"
					id="bookmark">북마크 보기</a> | <a
					href="/be1_java_web_study01/bookmark-group.jsp" id="bookmark-group">북마크
					그룹 관리</a> |
			</p>
		</nav>

		<section class="wifi-info">
			<select id="bookmark-group-select" name="bookmarkGroupId">
			</select> 
			<!-- WiFi 테이블 -->
			<table id="bookmark-table" border="1">
				<thead>
					<tr>
						<th>ID</th>
						<th>북마크 그룹 이름</th>
						<th>와이파이명</th>
						<th>등록일자</th>
						<th>비고</th>
					</tr>
				</thead>
				<tbody id="bookmark-table-tbody"></tbody>

			</table>


		</section>
	</div>
	<!-- JavaScript 파일 -->
	<script src="js/bookmark.js?v=1.03"></script>
</body>
</html>