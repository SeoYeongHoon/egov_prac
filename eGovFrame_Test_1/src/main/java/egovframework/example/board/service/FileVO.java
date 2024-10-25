package egovframework.example.board.service;

import org.springframework.web.multipart.MultipartFile;

public class FileVO {
	
	private int fileNo;
	private String fileName;
	private String extendedName;
	private MultipartFile uploadFile;
	private Long fileSize;
	
	private int id;
	private int answerNo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFileNo() {
		return fileNo;
	}
	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExtendedName() {
		return extendedName;
	}
	public void setExtendedName(String extendedName) {
		this.extendedName = extendedName;
	}
	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	@Override
	public String toString() {
		return "FileVO [fileNo=" + fileNo + ", fileName=" + fileName + ", extendedName=" + extendedName
				+ ", uploadFile=" + uploadFile + ", fileSize=" + fileSize + "]";
	}
	public int getAnswerNo() {
		return answerNo;
	}
	public void setAnswerNo(int answerNo) {
		this.answerNo = answerNo;
	}
}
