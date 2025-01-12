document.addEventListener("DOMContentLoaded", async () => {
    const tableBody = document.querySelector("#history-table tbody");
    const totalCountSpan = document.getElementById("total-count");
	const homeButton = document.getElementById("home");

	// 홈 버튼 누르면
	homeButton.addEventListener("click", () => {
		window.location.href = "/be1_java_web_study01/index.jsp";
	})
    // 데이터 로드 함수
    async function loadSearchHistory() {
        try {
            const response = await fetch("/be1_java_web_study01/searchHistoryData");
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const { historyList, totalCount } = await response.json();

            // 테이블 내용 초기화
            tableBody.innerHTML = "";

            // 데이터 추가
            historyList.forEach((history) => {
                const row = `
                    <tr>
                        <td>${history.id}</td>
                        <td>${history.longitude}</td>
                        <td>${history.latitude}</td>
                        <td>${history.searchDate}</td>
                        <td>
                            <button class="delete-btn" data-id="${history.id}">삭제</button>
                        </td>
                    </tr>`;
                tableBody.innerHTML += row;
            });

            // 총 개수 업데이트
            totalCountSpan.textContent = totalCount;

            // 삭제 버튼 이벤트 리스너 추가
            addDeleteEventListeners();
        } catch (error) {
            console.error("Failed to load search history:", error);
        }
    }

    // 삭제 이벤트 리스너 추가
    function addDeleteEventListeners() {
        const deleteButtons = document.querySelectorAll(".delete-btn");
        deleteButtons.forEach((button) => {
            button.addEventListener("click", async (e) => {
                const id = e.target.getAttribute("data-id");
                console.log(`Deleting ID: ${id}`);
                await deleteSearchHistory(id);
                await loadSearchHistory(); // 데이터 새로고침
            });
        });
    }

    // 삭제 요청 함수
    async function deleteSearchHistory(id) {
        try {
            const response = await fetch(`/be1_java_web_study01/deleteSearchHistory`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id })
            });
            if (!response.ok) throw new Error(`Failed to delete search history with ID ${id}`);
            console.log(`Deleted search history with ID ${id}`);
        } catch (error) {
            console.error("Failed to delete search history:", error);
        }
    }

    // 페이지 로딩 시 데이터 가져오기
    await loadSearchHistory();
});
