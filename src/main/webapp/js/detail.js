document.addEventListener("DOMContentLoaded", async () => {
	const tableBody = document.querySelector("#wifi-table tbody");
	const homeButton = document.getElementById("home");
	const bgresponse = await fetch(`/be1_java_web_study01/bookmark-group-get-results`);
	const bookMarkButton = document.getElementById("add-bookmark");
	const selectBmGroup = document.getElementById("bookmark-group-select");

	bookMarkButton.addEventListener("click", async () => {
		const v = selectBmGroup.value;
		if (v == "") {
			alert("북마크 그룹을 선택해주세요.");
			return;
		} else {
			const groupId = v;
			const wifiId = managerNo;
			addBookmark(wifiId, groupId);
			window.location.href = "/be1_java_web_study01/bookmark.jsp";
		}
	});
	// 홈 버튼 누르면
	homeButton.addEventListener("click", () => {
		window.location.href = "/be1_java_web_study01/index.jsp";
	})

	// 북마크그룹을 받아오면
	if (bgresponse.ok) {
		const bgdata = await bgresponse.json(); // JSON 데이터 파싱
		updateBookmarkGroupList(bgdata.bookmarkGroupList); // 받아온 데이터로 리스트 업데이트
	} else {
		console.error(`Failed to fetch bookmark groups. Status: ${bgresponse.status}`);
		alert("북마크 그룹 정보를 가져오는 데 실패했습니다.");
	}

	// `managerNo`를 쿼리 문자열에서 가져오기
	const urlParams = new URLSearchParams(window.location.search);
	const managerNo = urlParams.get("managerNo");
	if (!managerNo) {
		alert("관리번호가 없습니다.");
		return;
	}
	try {
		const response = await fetch(`/be1_java_web_study01/detail?managerNo=${managerNo}`);
		if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

		const data = await response.json(); // JSON 데이터 파싱
		updateTable([data]); // 테이블 업데이트 (배열 형태로 전달)
	} catch (error) {
		console.error("Failed to fetch detail data:", error);
	}

	// 테이블 업데이트 함수
	function updateTable(data) {
		tableBody.innerHTML = ""; // 기존 데이터 초기화

		data.forEach((wifi) => {
			const row = `
				<table id="wifi-table" border="1">
				<tbody id="wifi-table-body">
					<tr>
						<td>거리 (km)</td>
						<td>${(wifi.distance).toFixed(2)} km</td>
					</tr>
					<tr>
						<td>관리번호</td>
						<td>${wifi.X_SWIFI_MGR_NO}</td>
					</tr>
					<tr>
						<td>자치구</td>
						<td>${wifi.X_SWIFI_WRDOFC}</td>
					</tr>
					<tr>
						<td>와이파이명</td>
						<td>${wifi.X_SWIFI_MAIN_NM}</td>
					</tr>
					<tr>
						<td>도로명주소</td>
						<td>${wifi.X_SWIFI_ADRES1}</td>
					</tr>
					<tr>
						<td>상세주소</td>
						<td>${wifi.X_SWIFI_ADRES2}</td>
					</tr>
					<tr>
						<td>설치위치</td>
						<td>${wifi.X_SWIFI_INSTL_FLOOR}</td>
					</tr>
					<tr>
						<td>설치유형</td>
						<td>${wifi.X_SWIFI_INSTL_TY}</td>
					</tr>
					<tr>
						<td>설치기관</td>
						<td>${wifi.X_SWIFI_INSTL_MBY}</td>
					</tr>
					<tr>
						<td>서비스구분</td>
						<td>${wifi.X_SWIFI_SVC_SE}</td>
					</tr>
					<tr>
						<td>망종류</td>
						<td>${wifi.X_SWIFI_CMCWR}</td>
					</tr>
					<tr>
						<td>설치년도</td>
						<td>${wifi.X_SWIFI_CNSTC_YEAR}</td>
					</tr>
					<tr>
						<td>실내외구분</td>
						<td>${wifi.X_SWIFI_INOUT_DOOR}</td>
					</tr>
					<tr>
						<td>WIFI 접속환경</td>
						<td>${wifi.X_SWIFI_REMARS3}</td>
					</tr>
					<tr>
						<td>X좌표</td>
						<td>${wifi.LNT}</td>
					</tr>
					<tr>
						<td>Y좌표</td>
						<td>${wifi.LAT}</td>
					</tr>
					<tr>
						<td>작업일자</td>
						<td>${wifi.WORK_DTTM}</td>
					</tr>
				</tbody>
			</table>
            `;

			tableBody.innerHTML += row;
		});
	}

	function updateBookmarkGroupList(bookmarkGroupList) {
		const selectElement = document.getElementById("bookmark-group-select");
		if (!selectElement) {
			console.warn("bookmark-group-select 요소를 찾지 못했습니다.");
			return;
		}

		selectElement.innerHTML = "";

		const defaultOption = document.createElement("option");
		defaultOption.value = "";
		defaultOption.textContent = "북마크 그룹 선택";
		selectElement.appendChild(defaultOption);

		bookmarkGroupList.forEach(group => {
			const option = document.createElement("option");
			// 예: groupId, groupName 필드가 있다고 가정
			option.value = group.groupId;        // value 값으로 groupId
			option.textContent = group.groupName; // 보이는 텍스트는 groupName
			selectElement.appendChild(option);
		});
	}

	// 북마크 저장 함수
	async function addBookmark(wifiId, groupId) {
		// 입력값 유효성 검사
		if (!wifiId) {
			alert("북마크에 입력할 요소가 잘못되었습니다.");
			return;
		}

		if (!groupId) {
			alert("북마크 그룹을 선택해주세요.");
			return;
		}
		const bmResponse = await fetch('/be1_java_web_study01/saveBookmark', {
			method: "POST",
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			}, body: `wifiId=${encodeURIComponent(wifiId)}&groupId=${encodeURIComponent(groupId)}`,
		});
		if (bmResponse.ok) {
			console.log("북마크 그룹 저장 완료.");
		} else {
			throw new Error(`HTTP error! status: ${bmResponse.status}`);
		}
	}
});


