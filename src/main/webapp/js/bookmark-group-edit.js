document.addEventListener("DOMContentLoaded", async () => {
	const homeButton = document.getElementById("home");
	const updateBookmarkGroupButton = document.getElementById("update-bookmark-group");
	const urlParams = new URLSearchParams(window.location.search);
	const groupId = urlParams.get("groupId");
	const groupOrder = urlParams.get("groupOrder");
	const groupName = urlParams.get("groupName");


	// 폼에 미리 세팅
	document.getElementById("group-id").value = groupId || "";
	document.getElementById("bookmark-group-name-input").value = groupName || "";
	document.getElementById("order-input").value = groupOrder || "";

	// 홈 버튼 누르면
	homeButton.addEventListener("click", () => {
		window.location.href = "/be1_java_web_study01/index.jsp";
	})

	updateBookmarkGroupButton.addEventListener("click", () => {
		handleUpdateEvent();
	});
});


// 업데이트 이벤트 처리 함수
async function handleUpdateEvent(e) {
	const groupId = document.getElementById("group-id").value;
	const bookmarkGroupName = document.getElementById("bookmark-group-name-input").value;
	const bookmarkGroupOrder = document.getElementById("order-input").value;
	console.log(`Updating ID: ${groupId}`);
	await updateBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder, groupId);
}
// 북마크그룹 업데이트
async function updateBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder, groupId) {
	// 입력값 유효성 검사
	if (!bookmarkGroupName) {
		alert("북마크 이름을 입력해주세요.");
		return;
	}

	if (!bookmarkGroupOrder || bookmarkGroupOrder <= 0) {
		alert("순서가 없거나 잘못되었습니다. 순서는 1 이상의 숫자여야 합니다.");
		return;
	}
	const response = await fetch('/be1_java_web_study01/updateBookmarkGroup', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: `groupId=${encodeURIComponent(groupId)}&bookmarkGroupName=${encodeURIComponent(bookmarkGroupName)}&bookmarkGroupOrder=${encodeURIComponent(bookmarkGroupOrder)}`,
	});
	if (response.ok) {
		console.log("북마크 그룹 저장 완료.");
	} else {
		throw new Error(`HTTP error! status: ${response.status}`);
	}
	window.location.href = "/be1_java_web_study01/bookmark-group.jsp";
}