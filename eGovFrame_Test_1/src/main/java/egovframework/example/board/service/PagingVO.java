package egovframework.example.board.service;

public class PagingVO {
	private int pageNum;	//페이지 번호
	
	private int size;	//한 페이지당 출력 DATA 개수
	
	public PagingVO(int pageNum,int size) {
		this.pageNum = 1;
		this.size = 10;
	}
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
}
