package egovframework.example.board.service;

public class AnswerVO {
	private int answerNo;
	private String writer;
	private String password;
	private String title;
	private String content;
	private String date;
	private String view;
	private int no;
	
	public int getAnswerNo() {
		return answerNo;
	}
	public void setAnswerNo(int answerNo) {
		this.answerNo = answerNo;
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
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	
	@Override
	public String toString() {
		return "AnswerVO [answerNo=" + answerNo + ", writer=" + writer + ", password=" + password + ", title=" + title
				+ ", content=" + content + ", date=" + date + ", view=" + view + ", no=" + no + "]";
	}
}
