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
				| <a href="/be1_java_web_study01/bookmark-group.jsp"
					id="bookmark-group">북마크 그룹 관리</a> |
			</p>
		</nav>


		<select id="bookmark-group-select" name="bookmarkGroupId">
			<option value="">북마크 그룹</option>
		</select><button
				id="add-bookmark">북마크 추가</button>
		<section class="wifi-info">
			<!-- WiFi 테이블 -->
			<table id="wifi-table" border="1">
				<thead>
					<tr>
						<th>항목</th>
						<th>내용</th>
					</tr>
				</thead>
				<tbody id="wifi-table-body">
					<tr>
						<td>거리 (km)</td>
						<td></td>
					</tr>
					<tr>
						<td>관리번호</td>
						<td></td>
					</tr>
					<tr>
						<td>자치구</td>
						<td></td>
					</tr>
					<tr>
						<td>와이파이명</td>
						<td></td>
					</tr>
					<tr>
						<td>도로명주소</td>
						<td></td>
					</tr>
					<tr>
						<td>상세주소</td>
						<td></td>
					</tr>
					<tr>
						<td>설치위치</td>
						<td></td>
					</tr>
					<tr>
						<td>설치유형</td>
						<td></td>
					</tr>
					<tr>
						<td>설치기관</td>
						<td></td>
					</tr>
					<tr>
						<td>서비스구분</td>
						<td></td>
					</tr>
					<tr>
						<td>망종류</td>
						<td></td>
					</tr>
					<tr>
						<td>설치년도</td>
						<td></td>
					</tr>
					<tr>
						<td>실내외구분</td>
						<td></td>
					</tr>
					<tr>
						<td>WIFI 접속환경</td>
						<td></td>
					</tr>
					<tr>
						<td>X좌표</td>
						<td></td>
					</tr>
					<tr>
						<td>Y좌표</td>
						<td></td>
					</tr>
					<tr>
						<td>작업일자</td>
						<td></td>
					</tr>
				</tbody>
			</table>
		</section>
	</div>
	<!-- JavaScript 파일 -->
	<script src="js/detail.js?v=1.0.5"></script>
</body>
</html>