package egovframework.example.board.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.service.AnswerVO;
import egovframework.example.board.service.BoardSearchVO;
import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardVO;
import egovframework.example.board.service.BoardsVO;
import egovframework.example.board.service.FileVO;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
	
	@Resource(name = "boardMapper")
	private BoardMapper boardMapper;
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 글 목록
//	@Override
//	public List<?> selectBoardList(BoardSearchVO searchVO) throws Exception {
//		return boardMapper.selectBoardList(searchVO);
//	}
	
	@Override
	public List<BoardVO> selectBoardList(BoardSearchVO searchVO) throws Exception {
//		Map<String, Object> params = new HashMap<>();
//		params.put("pageSize", searchVO.getPageSize());
//		params.put("offset", searchVO.getFirstIndex());
		
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
		String originPw = vo.getPassword();
		String encodedPw = passwordEncoder.encode(originPw);
		
		vo.setPassword(encodedPw);
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

	// 답변글 수정
	@Override
	public void updateAnswer(AnswerVO answerVO) throws Exception {
		boardMapper.updateAnswer(answerVO);
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
	
	// 답변글 삭제
	@Override
	public void deleteAnswer(int answerNo) throws Exception {
		boardMapper.deleteAnswer(answerNo);
	}

	// 글 조회수 증가
	@Override
	public int updateView(int no) throws Exception {
		return boardMapper.updateAnswerView(no);
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
		String originPw = answerVO.getPassword();
		String encodedPw = passwordEncoder.encode(originPw);
		
		answerVO.setPassword(encodedPw);
		
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
	public int selectAnswerCount() throws Exception {
		return boardMapper.selectAnswerCount();
	}

	@Override
	public FileVO getFileInfo(int fileNo) throws Exception {
		return boardMapper.getFileInfo(fileNo);
	}
	
	
	
//	기능들을 Controller로부터 ServiceImpl 단으로 빼는 작업중
	@Transactional
	@Override
    public void insertBoardWithFiles(BoardVO vo, 
    							     List<MultipartFile> multipartFiles
    							     ) throws Exception {
		// 비밀번호 암호화
		String originPw = vo.getPassword();
		String encodedPw = passwordEncoder.encode(originPw);
		
		vo.setPassword(encodedPw);
		
        // 게시글 등록
        boardMapper.insertBoard(vo);

        // 파일 처리
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                // 파일 정보 설정
                String originName = multipartFile.getOriginalFilename();
                String extensionName = FilenameUtils.getExtension(originName);
                UUID uuid = UUID.randomUUID();
                String extendedName = uuid + "." + extensionName;
                Long fileSize = multipartFile.getSize();

                // 파일 저장 경로
                String filePath = "D:\\upload\\" + extendedName;

                FileVO fileVO = new FileVO();
                fileVO.setFileName(originName);
                fileVO.setExtendedName(extendedName);
                fileVO.setFileSize(fileSize);
                fileVO.setNo(vo.getNo());

                // 파일 정보 DB에 저장
                boardMapper.insertFiles(fileVO);

                // 파일 실제 저장
                try {
                    multipartFile.transferTo(new File(filePath));
                } catch (IOException e) {
                	throw new RuntimeException("파일 저장 중 오류 발생", e);  // 예외를 던져 트랜잭션 롤백
                }
            }
        }
    }
	
	@Override
    public void insertAnswerWithFiles(AnswerVO answerVO, 
    								  int no, 
    								  List<MultipartFile> multipartFiles
    								  ) throws Exception {
		// 비밀번호 암호화
		String originPw = answerVO.getPassword();
		String encodedPw = passwordEncoder.encode(originPw);
		
		answerVO.setPassword(encodedPw);
		
		// 답변 등록
        boardMapper.insertAnswer(answerVO);

        // 답변 상태 업데이트
        boardMapper.updateAnswerStatus(no);

        // 파일 처리
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                String originName = multipartFile.getOriginalFilename();
                String extentionName = FilenameUtils.getExtension(originName);
                UUID uuid = UUID.randomUUID();
                String extendedName = uuid + "." + extentionName;
                Long fileSize = multipartFile.getSize();

                // 파일 저장 경로 설정
                String filePath = "D:\\upload\\" + extendedName;

                // 파일 정보 설정
                FileVO fileVO = new FileVO();
                fileVO.setFileName(originName);
                fileVO.setExtendedName(extendedName);
                fileVO.setFileSize(fileSize);
                fileVO.setAnswerNo(answerVO.getAnswerNo());

                // 파일 정보 DB에 저장
                boardMapper.insertAnsweredFiles(fileVO);

                // 파일 저장 처리
                try {
                    multipartFile.transferTo(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	@Override
    public boolean updatePostWithFiles(BoardVO vo, 
    								   int no, 
    								   String pw, 
    								   List<MultipartFile> multipartFiles, 
    								   List<Integer> deleteNo
    								   ) throws Exception {
		
        // 게시글 정보 가져오기
        BoardVO boardVO = boardMapper.selectBoardInfo(no);
        boolean isPwCorrect = passwordEncoder.matches(pw, boardVO.getPassword());

        if (!isPwCorrect) {
            return false;  // 비밀번호가 틀린 경우
        }

        // 파일 삭제 처리
        if (deleteNo != null) {
            for (int deleteFileId : deleteNo) {
                boardMapper.deleteFiles(deleteFileId);
            }
        }

        // 게시글 수정
        boardMapper.updateBoard(vo);

        // 파일 추가 처리
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    String originName = multipartFile.getOriginalFilename();
                    String extensionName = FilenameUtils.getExtension(originName);
                    UUID uuid = UUID.randomUUID();
                    String extendedName = uuid + "." + extensionName;
                    Long fileSize = multipartFile.getSize();

                    // 파일 저장 경로
                    String filePath = "D:\\upload\\" + extendedName;

                    // 파일 정보 저장
                    FileVO fileVO = new FileVO();
                    fileVO.setFileName(originName);
                    fileVO.setExtendedName(extendedName);
                    fileVO.setFileSize(fileSize);
                    fileVO.setNo(vo.getNo());

                    boardMapper.insertFiles(fileVO);

                    // 파일 실제 저장
                    try {
                        multipartFile.transferTo(new File(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;  // 수정 성공
    }
	
	@Override
    public boolean updateAnswerWithFiles(AnswerVO vo, 
    									 int answerNo, 
    									 String pw, 
    									 List<MultipartFile> multipartFiles, 
    									 List<Integer> deleteNo
    									 ) throws Exception {
        // 답변 정보 가져오기
        AnswerVO answerVO = boardMapper.selectAnswerInfo(answerNo);
        Boolean isPwCorrect = passwordEncoder.matches(pw, answerVO.getPassword());

        if (!isPwCorrect) {
            return false;  // 비밀번호가 틀린 경우
        }

        // 파일 삭제 처리
        if (deleteNo != null) {
            for (int deleteFileId : deleteNo) {
                boardMapper.deleteFiles(deleteFileId);
            }
        }

        // 답변 업데이트 처리
        boardMapper.updateAnswer(vo);

        // 파일 추가 처리
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    String originName = multipartFile.getOriginalFilename();
                    String extensionName = FilenameUtils.getExtension(originName);
                    UUID uuid = UUID.randomUUID();
                    String extendedName = uuid + "." + extensionName;
                    Long fileSize = multipartFile.getSize();

                    // 파일 저장 경로
                    String filePath = "D:\\upload\\" + extendedName;

                    // 파일 정보 저장
                    FileVO fileVO = new FileVO();
                    fileVO.setFileName(originName);
                    fileVO.setExtendedName(extendedName);
                    fileVO.setFileSize(fileSize);
                    fileVO.setAnswerNo(answerVO.getAnswerNo());

                    boardMapper.insertAnsweredFiles(fileVO);

                    // 실제 파일 저장
                    try {
                        multipartFile.transferTo(new File(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;  // 수정 성공
    }

	@Override
    public void downloadFiles(String extendedName, HttpServletResponse res) throws Exception {
        // 파일의 원래 이름을 가져오기 위해 서비스 호출
        FileVO fileVO = boardMapper.selectOriginalName(extendedName);
        String realName = fileVO.getFileName();

        // 다운로드할 파일의 경로 설정
        String downPathFrom = "D:\\upload\\" + extendedName;

        File file = new File(downPathFrom);

        // 파일이 존재하지 않을 경우 처리
        if (!file.exists()) {
            return; // 파일이 없으면 종료
        }

        // 파일을 읽기 위한 FileInputStream 생성
        FileInputStream fileInputStream = new FileInputStream(downPathFrom);

        // 파일 이름 인코딩 처리 (한글 파일명 지원)
        realName = URLEncoder.encode(realName, "UTF-8");

        // 응답 헤더 설정
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment; filename=" + realName);

        // 파일 전송
        OutputStream outputStream = res.getOutputStream();
        byte[] buffer = new byte[1024];  // 1KB 단위로 읽기
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // 스트림 닫기
        outputStream.flush();
        outputStream.close();
        fileInputStream.close();
    }

	@Override
	public List<BoardsVO> selectBoardListWithAnswers(BoardSearchVO searchVO) throws Exception {
		return boardMapper.selectBoardLists(searchVO);
	}
}
