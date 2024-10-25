package egovframework.example.board.service;

public class BoardsVO {
	private int id;
    private Integer parentId;
    private String title;
    private String content;
    private String writer;
    private String password;
    private String date;
    private String type;
    private int view;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

	@Override
	public String toString() {
		return "BoardsVO [id=" + id + ", parentId=" + parentId + ", title=" + title + ", content=" + content
				+ ", writer=" + writer + ", password=" + password + ", date=" + date + ", type=" + type + ", view="
				+ view + "]";
	}
    
}
