document.addEventListener("DOMContentLoaded", async () => {
    const homeButton = document.getElementById("home");
    const bmGSelect = document.getElementById("bookmark-group-select");

    // 홈 버튼 누르면 이동
    homeButton.addEventListener("click", () => {
        window.location.href = "/be1_java_web_study01/index.jsp";
    });

    // 북마크 그룹 가져오기
    try {
        const bgresponse = await fetch(`/be1_java_web_study01/bookmark-group-get-results`);
        
        if (bgresponse.ok) {
            const bgdata = await bgresponse.json();
            updateBookmarkGroupList(bgdata.bookmarkGroupList);

            if (bgdata.bookmarkGroupList && bgdata.bookmarkGroupList.length > 0) {
                // 첫 번째 그룹 ID 가져오기
                const firstGroupId = bgdata.bookmarkGroupList[0].groupId;
                
                // select 요소의 값을 첫 번째 그룹으로 설정
                bmGSelect.value = firstGroupId;

                // 첫 번째 그룹의 북마크 정보 가져오기
                fetchBookmarksByGroupId(firstGroupId);
            } else {
                // 북마크 그룹이 없을 경우
                const bookmarkTableBody = document.querySelector("#bookmark-table-tbody");
                const noDataRow = `
                    <tr>
                      <td colspan="5">등록된 북마크 그룹이 없습니다.</td>
                    </tr>
                `;
                bookmarkTableBody.innerHTML = noDataRow;
            }
        } else {
            console.error(`Failed to fetch bookmark groups. Status: ${bgresponse.status}`);
            alert("북마크 그룹 정보를 가져오는 데 실패했습니다.");
        }
    } catch (error) {
        console.error("Error fetching bookmark groups:", error);
        alert("북마크 그룹 정보를 가져오는 중 오류가 발생했습니다.");
    }

    // 북마크 그룹 변경 시 북마크 정보 업데이트
    bmGSelect.addEventListener("change", () => {
        const selectedValue = bmGSelect.value; // 드롭다운에서 선택된 groupId
        if (selectedValue) {
            fetchBookmarksByGroupId(selectedValue);
        } else {
            // 선택된 값이 없을 경우 (예: 기본 옵션)
            const bookmarkTableBody = document.querySelector("#bookmark-table-tbody");
            bookmarkTableBody.innerHTML = `
                <tr>
                  <td colspan="5">북마크 그룹을 선택해주세요.</td>
                </tr>
            `;
        }
    });

    // 북마크 그룹 리스트 업데이트 함수
    function updateBookmarkGroupList(bookmarkGroupList) {
        const selectElement = document.getElementById("bookmark-group-select");
        if (!selectElement) {
            console.warn("bookmark-group-select 요소를 찾지 못했습니다.");
            return;
        }

        selectElement.innerHTML = ""; // 기존 옵션 제거

        bookmarkGroupList.forEach(group => {
            const option = document.createElement("option");
            option.value = group.groupId;        // value 값으로 groupId
            option.textContent = group.groupName; // 보이는 텍스트는 groupName
            selectElement.appendChild(option);
        });
    }

    // 북마크 테이블 업데이트 함수
    async function fetchBookmarksByGroupId(groupId) {
        try {
            const bmResponse = await fetch(`/be1_java_web_study01/bookmark-get-results?groupId=${groupId}`);
            
            if (bmResponse.ok) {
                const data = await bmResponse.json();
                updateBookmarkTable(data.bookmarkList);
            } else {
                console.error(`Failed to fetch bookmarks. Status: ${bmResponse.status}`);
                alert("북마크 정보를 가져오는 데 실패했습니다.");
            }
        } catch (error) {
            console.error("Error fetching bookmarks:", error);
            alert("북마크 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    function updateBookmarkTable(bookmarkList) {
        const bookmarkTableBody = document.querySelector("#bookmark-table-tbody");
        bookmarkTableBody.innerHTML = ""; // 기존 데이터 초기화

        if (bookmarkList && bookmarkList.length > 0) {
            bookmarkList.forEach((bm) => {
                console.log("bookmark item:", bm);

                const row = `
                  <tr>
                    <td>${bm.bookmarkId}</td>
                    <td>${bm.groupName}</td>
                    <td><a href="/be1_java_web_study01/detail.jsp?managerNo=${bm.managerNo}">${bm.mainName}</a></td>
                    <td>${bm.createdAt}</td>
                    <td>
                      <button class="delete-btn" data-id="${bm.bookmarkId}">삭제</button>
                    </td>
                  </tr>
                `;
                bookmarkTableBody.innerHTML += row;
            });
            addDeleteEventListeners();
        } else {
            const noDataRow = `
                <tr>
                  <td colspan="5">등록된 북마크가 없습니다.</td>
                </tr>
              `;
            bookmarkTableBody.innerHTML = noDataRow;
        }
    }

    // 삭제 이벤트 리스너 추가
    function addDeleteEventListeners() {
        document.querySelectorAll(".delete-btn").forEach((button) => {
            button.removeEventListener("click", handleDeleteEvent);
            button.addEventListener("click", handleDeleteEvent);
        });
    }

    // 삭제 이벤트 처리 함수
    async function handleDeleteEvent(e) {
        const id = e.target.getAttribute("data-id");
        console.log(`Deleting ID: ${id}`);
        await deleteBookmark(id);
        // 삭제 후, 현재 그룹의 북마크를 다시 불러오기
        const selectedGroupId = bmGSelect.value;
        if (selectedGroupId) {
            fetchBookmarksByGroupId(selectedGroupId);
        }
    }

    // 북마크 삭제 함수
    async function deleteBookmark(id) {
        try {
            const response = await fetch(`/be1_java_web_study01/deleteBookmark`, {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: `id=${id}`
            });
            if (!response.ok) throw new Error(`Failed to delete bookmark with ID ${id}`);
            console.log(`Deleted bookmark with ID ${id}`);
        } catch (error) {
            console.error("Failed to delete bookmark:", error);
        }
    }
});
