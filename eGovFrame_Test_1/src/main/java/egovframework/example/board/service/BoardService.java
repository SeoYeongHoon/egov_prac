package egovframework.example.board.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
	
	// 글 개수
	int selectBoardCount(BoardSearchVO boardSearchVO) throws Exception;
	
	// 글 단건 조회
	BoardsVO selectBoardInfo(int no) throws Exception;
	
	// 글 파일들 조회
	List<FileVO> selectFilesInfo(int no) throws Exception;
	
	// 첨부파일 업로드
	void insertFiles(FileVO fileVO) throws Exception;

	// 첨부파일 수정
	void updateFiles(FileVO fileVO) throws Exception;
	
	// 글 삭제
	void deletePost(int no) throws Exception;
	
	// 글 조회수 증가
	int updateView(int no) throws Exception;
	
	
//	기능들을 Controller로부터 ServiceImpl 단으로 빼는 작업중
	void insertBoard(BoardsVO vo, List<MultipartFile> multipartFiles) throws Exception;

	void insertAnswer(BoardsVO boardsVO, int parentId, List<MultipartFile> multipartFiles) throws Exception;

	boolean updateBoard(BoardsVO vo, int id, String pw, List<MultipartFile> multipartFiles, List<Integer> deleteNo) throws Exception;

	void downloadFiles(String extendedName, HttpServletResponse res) throws Exception;

	List<BoardsVO> selectBoardLists(BoardSearchVO searchVO) throws Exception;
	
}
