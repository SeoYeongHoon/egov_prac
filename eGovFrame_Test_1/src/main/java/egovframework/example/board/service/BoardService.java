package egovframework.example.board.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

	// 글 목록 
//	List<?> selectBoardList(BoardSearchVO searchVO) throws Exception;
	List<BoardVO> selectBoardList(BoardSearchVO searchVO) throws Exception;
	
	// 글 개수
	int selectBoardCount(BoardSearchVO boardSearchVO) throws Exception;
	
	// 글 단건 조회
	BoardVO selectBoardInfo(int no) throws Exception;
	
	// 글 파일들 조회
	List<FileVO> selectFilesInfo(int no) throws Exception;
	
	// 파일 단건 조회
	FileVO getFileInfo(int fileNo) throws Exception;
	
	// 답변글 파일들 조회
	List<FileVO> selectAnswerFilesInfo(int answerNo) throws Exception;
	
	// 글 작성
	void insertBoard(BoardVO vo) throws Exception;
	
	// 첨부파일 업로드
	void insertFiles(FileVO fileVO) throws Exception;

	// 답변글 첨부파일 업로드
	void insertAnsweredFiles(FileVO fileVO) throws Exception;
	
	// 글 수정
	void updateBoard(BoardVO vo) throws Exception;
	
	// 답변글 수정
	void updateAnswer(AnswerVO answerVO) throws Exception;

	// 첨부파일 수정
	void updateFiles(FileVO fileVO) throws Exception;
	
	// 첨부파일 삭제
	void deleteFiles(int fileNo) throws Exception;
	
	// 글 삭제
	void deletePost(int no) throws Exception;
	
	// 답변글 삭제
	void deleteAnswer(int answerNo) throws Exception;

	// 글 조회수 증가
	int updateView(int no) throws Exception;
	
	// 답변글 조회수 증가
	int updateAnswerView(int no) throws Exception;
	
	// 다운로드 시 원래 이름 선택
	FileVO selectOriginalName(String exName) throws Exception;
	
	// 답변글 작성
	void insertAnswer(AnswerVO answerVO) throws Exception;
	
	// 답변 작성 시 게시글 상태변화
	void updateAnswerStatus(int no) throws Exception;
	
	// 답변글 단건 조회
	AnswerVO selectAnswerInfo(int answerNo) throws Exception;
	
	// 답변글 목록 출력
	List<AnswerVO> selectAnswer() throws Exception;
	
	// 답변글 개수
	int selectAnswerCount() throws Exception;
	
	
//	기능들을 Controller로부터 ServiceImpl 단으로 빼는 작업중
	void insertBoardWithFiles(BoardVO vo, List<MultipartFile> multipartFiles) throws Exception;

	void insertAnswerWithFiles(AnswerVO answerVO, int no, List<MultipartFile> multipartFiles) throws Exception;

	boolean updatePostWithFiles(BoardVO vo, int no, String pw, List<MultipartFile> multipartFiles, List<Integer> deleteNo) throws Exception;

	boolean updateAnswerWithFiles(AnswerVO vo, int answerNo, String pw, List<MultipartFile> multipartFiles, List<Integer> deleteNo) throws Exception;

	void downloadFiles(String extendedName, HttpServletResponse res) throws Exception;

	List<BoardsVO> selectBoardListWithAnswers(BoardSearchVO searchVO) throws Exception;

	
}
