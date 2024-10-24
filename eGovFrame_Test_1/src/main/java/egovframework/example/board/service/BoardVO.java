package egovframework.example.board.service;

public class BoardVO {

	private int no;
	private String writer;
	private String password;
	private String title;
	private String content;
	private String date;
	private String view;
	private int isAnswer;
	private int parentNo;
	
	private int pageSize;
	private int offset;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	
	@Override
	public String toString() {
		return "BoardVO [no=" + no + ", writer=" + writer + ", password=" + password + ", title=" + title + ", content="
				+ content + ", date=" + date + ", view=" + view + ", pageSize=" + pageSize
				+ ", offset=" + offset + "]";
	}
	public int getIsAnswer() {
		return isAnswer;
	}
	public void setIsAnswer(int isAnswer) {
		this.isAnswer = isAnswer;
	}
	public int getParentNo() {
		return parentNo;
	}
	public void setParentNo(int parentNo) {
		this.parentNo = parentNo;
	}
}
