document.addEventListener("DOMContentLoaded", () => {
	const homeButton = document.getElementById("home");
	const saveBookmarkGroupButton = document.getElementById("save-bookmark-group");

	// 홈 버튼 누르면
	homeButton.addEventListener("click", () => {
		window.location.href = "/be1_java_web_study01/index.jsp";
	})

	saveBookmarkGroupButton.addEventListener("click", () => {
		const bookmarkGroupName = document.getElementById("bookmark-group-name-input").value;
		const bookmarkGroupOrder = document.getElementById("order-input").value;
		addBookmark(bookmarkGroupName, bookmarkGroupOrder);
		window.location.href = "/be1_java_web_study01/bookmark-group.jsp";
	});

	async function addBookmark(bookmarkGroupName, bookmarkGroupOrder) {
		// 입력값 유효성 검사
		if (!bookmarkGroupName) {
			alert("북마크 이름을 입력해주세요.");
			return;
		}

		if (!bookmarkGroupOrder || bookmarkGroupOrder <= 0) {
			alert("순서가 없거나 잘못되었습니다. 순서는 1 이상의 숫자여야 합니다.");
			return;
		}

		const response = await fetch('/be1_java_web_study01/saveBookmarkGroup', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
			body: `bookmarkGroupName=${encodeURIComponent(bookmarkGroupName)}&bookmarkGroupOrder=${encodeURIComponent(bookmarkGroupOrder)}`,
		});
		if (response.ok) {
			console.log("북마크 그룹 저장 완료.");
		} else {
			throw new Error(`HTTP error! status: ${response.status}`);
		}
	}

});
