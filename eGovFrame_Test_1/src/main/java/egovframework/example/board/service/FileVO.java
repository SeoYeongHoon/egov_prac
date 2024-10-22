package egovframework.example.board.service;

import org.springframework.web.multipart.MultipartFile;

public class FileVO {
	
	private int fileNo;
	private String fileName;
	private String extendedName;
	private MultipartFile uploadFile;
	private Long fileSize;
	
	private int no;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
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
}
