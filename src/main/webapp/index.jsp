<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
<link rel="stylesheet" href="css/style.css?v=1.0">
</head>
<body>
	<div class="container">
		<h1>와이파이 정보 구하기</h1>
		<nav>
			<p>
				<a href="#" id="home">홈</a> | <a
					href="/be1_java_web_study01/searchHistory.jsp" id="history">위치
					히스토리 목록</a> | <a href="#" id="fetch-api">Open API 와이파이 정보 가져오기</a> | <a
					href="/be1_java_web_study01/bookmark.jsp" id="bookmark">북마크 보기</a>
				| <a href="/be1_java_web_study01/bookmark-group.jsp" id="bookmark-group">북마크 그룹 관리</a> |
			</p>
		</nav>

		<section class="location">
			<p>
				LAT: <input type="text" id="latitude" placeholder="위도 입력">
				LNT: <input type="text" id="longitude" placeholder="경도 입력">
				<button id="get-location">내 위치 가져오기</button>
				<button id="view-nearby">근처 WiFi 정보 보기</button>
			</p>
		</section>

		<section class="wifi-info">
			<h2>저장된 WiFi 정보</h2>
			<!-- 데이터 개수 표시 -->
			<p>
				총 <span id="wifi-count">0</span>개의 WiFi 정보가 저장되어 있습니다.
			</p>
			<!-- WiFi 테이블 -->
			<table id="wifi-table" border="1">
				<thead>
					<tr>
						<th>거리 (km)</th>
						<th>관리번호</th>
						<th>자치구</th>
						<th>와이파이명</th>
						<th>도로명주소</th>
						<%-- <th>상세주소</th>
						<th>설치위치</th>
						<th>설치유형</th>
						<th>설치기관</th>
						<th>서비스구분</th>
						<th>망종류</th>
						<th>설치년도</th>
						<th>실내외구분</th>
						<th>WIFI 접속환경</th>
						<th>X좌표</th>
						<th>Y좌표</th>
						<th>작업일자</th> --%>

					</tr>
				</thead>
				<tbody id="wifi-table-body"></tbody>

			</table>


		</section>
	</div>
	<!-- JavaScript 파일 -->
	<script src="js/index.js?v=1.02"></script>
</body>
</html>