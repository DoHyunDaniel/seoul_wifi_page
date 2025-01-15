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
					히스토리 목록</a> | <a href="/be1_java_web_study01/bookmark.jsp"
					id="bookmark">북마크 보기</a> | <a
					href="/be1_java_web_study01/bookmark-group.jsp" id="bookmark-group">북마크
					그룹 관리</a> |
			</p>
		</nav>

		<section class="wifi-info">
			<h2>북마크 그룹</h2>

			<!-- WiFi 테이블 -->
			<table id="wifi-table" border="1">
				<thead>
					<tr>
						<td>북마크 그룹 이름</td>
						<td><input type="text" id="bookmark-group-name-input"
							placeholder=""></td>
					</tr>
					<tr>
						<td>순서</td>
						<td><input type="number" id="order-input"
							placeholder="1 이상의 숫자만 입력해주세요" min="1"></td>
					</tr>
				</thead>
				<tbody id="wifi-table-body"></tbody>
			</table>

			<div class="button-container">
				<button id="save-bookmark-group">저장</button>
			</div>
		</section>
	</div>
	<!-- JavaScript 파일 -->
	<script src="js/bookmark-group-add.js?v=1.1"></script>
</body>
</html>
