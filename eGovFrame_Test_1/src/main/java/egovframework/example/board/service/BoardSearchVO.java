package egovframework.example.board.service;

public class BoardSearchVO {

    private String searchKeyword;    // 검색어
    private String searchCondition;  // 검색 조건 (제목/작성자)
    private int pageIndex = 1;       // 현재 페이지 번호
    private int pageUnit;            // 한 페이지에 보여줄 게시물 수
    private int pageSize;            // 페이지 네비게이션에 표시할 페이지 수
    private int firstIndex;          // 첫 게시물 인덱스
    private int lastIndex;           // 마지막 게시물 인덱스
    private int recordCountPerPage;  // 한 페이지에 표시할 게시물 수
    
    private int isAnswered;

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageUnit() {
        return pageUnit;
    }

    public void setPageUnit(int pageUnit) {
        this.pageUnit = pageUnit;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getRecordCountPerPage() {
        return recordCountPerPage;
    }

    public void setRecordCountPerPage(int recordCountPerPage) {
        this.recordCountPerPage = recordCountPerPage;
    }

	public int getIsAnswered() {
		return isAnswered;
	}

	public void setIsAnswered(int isAnswered) {
		this.isAnswered = isAnswered;
	}
}

