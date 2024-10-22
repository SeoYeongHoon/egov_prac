package egovframework.example.board.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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
	public int selectBoardCount() throws Exception {
		return boardMapper.selectBoardCount();
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
	
	// 글 수정
	@Override
	public void updateBoard(BoardVO vo) throws Exception {
		boardMapper.updateBoard(vo);
	}

	// 첨부파일 수정
	@Override
	public void updateFiles(int no) throws Exception {
		boardMapper.updateFiles(no);		
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

	// 다운로드 시 원래 이름 선택
	@Override
	public FileVO selectOriginalName(String exName) throws Exception {
		return boardMapper.selectOriginalName(exName);
	}

}
