document.addEventListener("DOMContentLoaded", async () => {
	const response = await fetch(`/be1_java_web_study01/get-results`);
	const data = await response.json(); // JSON 데이터 파싱
	document.getElementById("wifi-count").textContent = data.totalCount; // 최신 WiFi 개수 업데이트
}
);
document.addEventListener("DOMContentLoaded", () => {
	const fetchApiButton = document.getElementById("fetch-api");
	const latitudeInput = document.getElementById("latitude");
	const longitudeInput = document.getElementById("longitude");
	const tableBody = document.querySelector("#wifi-table tbody");
	const getLocationButton = document.getElementById("get-location");
	const wifiCountSpan = document.getElementById("wifi-count");
	const getResultButton = document.getElementById("view-nearby");
	const homeButton = document.getElementById("home");

	// 홈 버튼 누르면
	homeButton.addEventListener("click", () => {
		window.location.href = "/be1_java_web_study01/index.jsp";
	})

	// 현재 위치 불러오기 버튼 이벤트 리스너
	getLocationButton.addEventListener("click", () => {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(
				(position) => {
					const latitude = position.coords.latitude.toFixed(6);
					const longitude = position.coords.longitude.toFixed(6);
					console.log(`위도: ${latitude}, 경도: ${longitude}`);
					latitudeInput.value = latitude;
					longitudeInput.value = longitude;

					// 현재 날짜 가져오기
					const searchDate = new Date().toISOString();

					// 검색 기록 저장 요청
					saveSearchHistory(latitude, longitude, searchDate);
				},
				async (error) => {
					console.error("Geolocation error, falling back to IP:", error);
					await getLocationFromIP();
				}
			);
		} else {
			alert("Geolocation이 지원되지 않는 브라우저입니다. 위치를 수동으로 입력해주세요.");
		}
	});

	// 검색 기록 저장 요청 함수
	async function saveSearchHistory(latitude, longitude, searchDate) {
		try {
			const response = await fetch('/be1_java_web_study01/saveSearchHistory', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: `latitude=${latitude}&longitude=${longitude}&searchDate=${encodeURIComponent(searchDate)}`,
			});

			if (response.ok) {
				console.log("검색 기록 저장 완료.");
			} else {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
		} catch (error) {
			console.error("검색 기록 저장 중 오류:", error);
		}
	}


	// API 호출 및 데이터 테이블 업데이트
	fetchApiButton.addEventListener("click", async (e) => {
		e.preventDefault();

		const latitude = latitudeInput.value;
		const longitude = longitudeInput.value;

		if (!latitude || !longitude) {
			alert("위도와 경도를 입력해주세요.");
			return;
		}

		try {
			const response = await fetch(`/be1_java_web_study01/fetch-api?latitude=${latitude}&longitude=${longitude}`);
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const data = await response.json(); // 서버 응답을 JSON으로 파싱
			if (data.message) {
				alert(data.message); // 성공 메시지 표시
			} else if (data.error) {
				alert(data.error); // 오류 메시지 표시
			}
		} catch (error) {
			console.error("데이터 저장 중 오류:", error);
			alert("데이터를 가져오는 중 문제가 발생했습니다.");
		}
	});

	getResultButton.addEventListener("click", async (e) => {
		e.preventDefault();
		try {
			const response = await fetch(`/be1_java_web_study01/get-results`);
			const data = await response.json(); // JSON 데이터 파싱
			updateTable(data.wifiData);
			document.getElementById("wifi-count").textContent = data.totalCount; // 최신 WiFi 개수 업데이트
		} catch (error) {
			console.error("데이터 처리 중 오류:", error);
			alert("데이터를 불러오는 중 문제가 발생했습니다.");
		}
	})

	// 테이블 업데이트 함수
	function updateTable(data) {
		tableBody.innerHTML = ""; // 기존 데이터 초기화

		data.forEach((wifi, index) => {
			const row = `
                <tr>
                    <td>${(wifi.distance).toFixed(2)} km</td>
                    <td>${wifi.X_SWIFI_MGR_NO}</td>
                    <td>${wifi.X_SWIFI_WRDOFC}</td>
                    <td><a href="/be1_java_web_study01/detail.jsp?managerNo=${wifi.X_SWIFI_MGR_NO}">
                        ${wifi.X_SWIFI_MAIN_NM}
                    </a></td>
                    <td>${wifi.X_SWIFI_ADRES1}</td>
                </tr>
            `;

			tableBody.innerHTML += row;
		});
	}

});
function updateWifiCount(totalCount) {
	wifiCountSpan.textContent = totalCount; // 개수 업데이트
}

