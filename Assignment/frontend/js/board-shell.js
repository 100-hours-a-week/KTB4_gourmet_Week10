(function initBoardShell() {
    const layout = document.querySelector("#layout");
    const navToggle = document.querySelector("#nav-toggle");
    const headerSearch = document.querySelector("#header-search");
    const currentBoard = document.body.dataset.board;

    if (currentBoard) {
        document.querySelectorAll(".board-link").forEach(function (link) {
            link.classList.toggle("is-active", link.dataset.board === currentBoard);
        });
    }

    if (navToggle && layout) {
        navToggle.addEventListener("click", function () {
            const collapsed = layout.classList.toggle("sidebar-collapsed");
            navToggle.setAttribute("aria-expanded", collapsed ? "false" : "true");
            navToggle.setAttribute(
                "aria-label",
                collapsed ? "게시판 메뉴 열기" : "게시판 메뉴 닫기"
            );
        });
    }

    if (headerSearch) {
        headerSearch.addEventListener("submit", function (event) {
            event.preventDefault();
            alert("통합 검색은 곧 연결될 예정입니다.");
        });
    }
})();
