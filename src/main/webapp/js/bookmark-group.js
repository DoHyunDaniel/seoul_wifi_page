document.addEventListener("DOMContentLoaded", async () => {
	const homeButton = document.getElementById("home");
	const response = await fetch(`/be1_java_web_study01/bookmark-group-get-results`);
	
	// 홈 버튼 누르면
	homeButton.addEventListener("click", () => {
		window.location.href = "/be1_java_web_study01/index.jsp";
	})
	
	if (response.ok) {
		const data = await response.json(); // JSON 데이터 파싱
		console.log(data);
		updateBookmarkGroupTable(data.bookmarkGroupList); // 받아온 데이터로 테이블 업데이트
	} else {
		console.error(`Failed to fetch bookmark groups. Status: ${response.status}`);
		alert("북마크 그룹 정보를 가져오는 데 실패했습니다.");
	}

	// 북마크 그룹 테이블 업데이트 함수
	function updateBookmarkGroupTable(bookmarkGroupList) {
		const bookmarkGroupTableBody = document.querySelector("#bookmark-group-table-tbody");
		bookmarkGroupTableBody.innerHTML = ""; // 기존 데이터 초기화

		if (bookmarkGroupList && bookmarkGroupList.length > 0) {
			bookmarkGroupList.forEach((group) => {
				const row = `
                    <tr>
                        <td>${group.groupOrder}</td>
                        <td>${group.groupName}</td>
                        <td>${group.createdAt}</td>
                        <td>${group.editedAt || "-"}</td>
                        <td>
                            <a href = "/be1_java_web_study01/bookmark-group-edit.jsp?groupId=${group.groupId}&groupOrder=${group.groupOrder}&groupName=${group.groupName}"><button class="edit-btn" data-id="${group.groupId}">수정</button></a>
                            <button class="delete-btn" data-id="${group.groupId}">삭제</button>
                        </td>
                    </tr>
                `;
				bookmarkGroupTableBody.innerHTML += row;

				addDeleteEventListeners();
			});
		} else {
			const noDataRow = `
                <tr>
                    <td colspan="6">등록된 북마크 그룹이 없습니다.</td>
                </tr>
            `;
			bookmarkGroupTableBody.innerHTML = noDataRow;
		}
	}


	// 삭제 이벤트 리스너 추가
	function addDeleteEventListeners() {
		document.querySelectorAll(".delete-btn").forEach((button) => {
			// 기존 이벤트 제거 후 추가
			button.removeEventListener("click", handleDeleteEvent);
			button.addEventListener("click", handleDeleteEvent);
		});
	}

	// 삭제 이벤트 처리 함수
	async function handleDeleteEvent(e) {
		const id = e.target.getAttribute("data-id");
		console.log(`Deleting ID: ${id}`);
		await deleteBookmarkGroup(id);
		location.reload(); // 페이지 새로고침
	}



	// 북마크그룹 삭제
	async function deleteBookmarkGroup(id) {
		try {
			const response = await fetch(`/be1_java_web_study01/deleteBookmarkGroup`, {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: `groupId=${id}`
			});
			if (!response.ok) throw new Error(`Failed to delete bookmark group with ID ${id}`);
			console.log(`Deleted bookmark group with ID ${id}`);
		} catch (error) {
			console.error("Failed to delete bookmark group:", error);
		}
	}

}
);


