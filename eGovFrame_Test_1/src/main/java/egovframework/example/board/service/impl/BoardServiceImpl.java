package egovframework.example.board.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.example.board.service.AnswerVO;
import egovframework.example.board.service.BoardSearchVO;
import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardVO;
import egovframework.example.board.service.FileVO;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
	
	@Resource(name = "boardMapper")
	private BoardMapper boardMapper;

	// 글 목록
//	@Override
//	public List<?> selectBoardList(BoardSearchVO searchVO) throws Exception {
//		return boardMapper.selectBoardList(searchVO);
//	}
	
	@Override
	public List<BoardVO> selectBoardList(BoardSearchVO searchVO) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pageSize", searchVO.getPageSize());
		params.put("offset", searchVO.getFirstIndex());
		
		return boardMapper.selectBoardList(searchVO);
	}

	// 글 개수
	@Override
	public int selectBoardCount(BoardSearchVO boardSearchVO) throws Exception {
		return boardMapper.selectBoardCount(boardSearchVO);
	}

	// 글 단건 조회
	@Override
	public BoardVO selectBoardInfo(int no) throws Exception {
		return boardMapper.selectBoardInfo(no);
	}

	// 글 파일들 조회
	@Override
	public List<FileVO> selectFilesInfo(int no) throws Exception {
		return boardMapper.selectFilesInfo(no);
	}

	// 답변글 파일들 조회
	@Override
	public List<FileVO> selectAnswerFilesInfo(int answerNo) throws Exception {
		return boardMapper.selectAnswerFilesInfo(answerNo);
	}

	// 글 작성
	@Override
	public void insertBoard(BoardVO vo) throws Exception {
		boardMapper.insertBoard(vo);
	}

	// 첨부파일 업로드
	@Override
	public void insertFiles(FileVO fileVO) throws Exception {
		boardMapper.insertFiles(fileVO);
	}

	// 답변글 첨부파일 업로드
	@Override
	public void insertAnsweredFiles(FileVO fileVO) throws Exception {
		boardMapper.insertAnsweredFiles(fileVO);
	}
	
	// 글 수정
	@Override
	public void updateBoard(BoardVO vo) throws Exception {
		boardMapper.updateBoard(vo);
	}

	// 첨부파일 수정
	@Override
	public void updateFiles(FileVO fileVO) throws Exception {
		boardMapper.updateFiles(fileVO);		
	}

	// 첨부파일 삭제
	@Override
	public void deleteFiles(int fileNo) throws Exception {
		boardMapper.deleteFiles(fileNo);
	}

	// 글 삭제
	@Override
	public void deletePost(int no) throws Exception {
		boardMapper.deletePost(no);
	}

	// 글 조회수 증가
	@Override
	public int updateView(int no) throws Exception {
		return boardMapper.updateView(no);
	}

	// 답변글 조회수 증가
	@Override
	public int updateAnswerView(int no) throws Exception {
		return boardMapper.updateAnswerView(no);
	}


	// 다운로드 시 원래 이름 선택
	@Override
	public FileVO selectOriginalName(String exName) throws Exception {
		return boardMapper.selectOriginalName(exName);
	}

	// 답변글 작성
	@Override
	public void insertAnswer(AnswerVO answerVO) throws Exception {
		boardMapper.insertAnswer(answerVO);	
	}

	// 답변 작성 시 게시글 상태변화
	@Override
	public void updateAnswerStatus(int no) throws Exception {
		boardMapper.updateAnswerStatus(no);		
	}

	// 답변글 단건 조회
	@Override
	public AnswerVO selectAnswerInfo(int answerNo) throws Exception {
		return boardMapper.selectAnswerInfo(answerNo);
	}

	// 답변글 목록 출력
	@Override
	public List<AnswerVO> selectAnswer() throws Exception {
		return boardMapper.selectAnswer();
	}

	// 답변글 개수
	@Override
	public int selectAnswerCount(BoardSearchVO boardSearchVO) throws Exception {
		return boardMapper.selectAnswerCount(boardSearchVO);
	}
}
