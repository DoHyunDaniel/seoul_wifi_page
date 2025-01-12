<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>위치 히스토리 목록</title>
    <link rel="stylesheet" href="css/style.css?v=1.0">
</head>
<body>
    <div class="container">
        <h1>위치 히스토리 목록</h1>
        <nav>
            <p>
                <a href="#" id="home">홈</a> |
                <a href="#" id="fetch-api">Open API 와이파이 정보 가져오기</a>
            </p>
        </nav>
        <section>
            <h2>저장된 위치 히스토리</h2>
            <p>총 <span id="total-count">0</span>개의 위치 기록이 저장되어 있습니다.</p>
            <table id="history-table" border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>X좌표</th>
                        <th>Y좌표</th>
                        <th>조회일자</th>
                        <th>비고</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- 데이터는 JavaScript로 추가 -->
                </tbody>
            </table>
        </section>
    </div>
    <script src="js/searchHistory.js?v=1.0"></script>
</body>
</html>
