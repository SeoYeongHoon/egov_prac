package egovframework.example.board.service;

import java.util.List;

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

	// 첨부파일 수정
	void updateFiles(FileVO fileVO) throws Exception;
	
	// 첨부파일 삭제
	void deleteFiles(int fileNo) throws Exception;
	
	// 글 삭제
	void deletePost(int no) throws Exception;

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
	int selectAnswerCount(BoardSearchVO boardSearchVO) throws Exception;
}
