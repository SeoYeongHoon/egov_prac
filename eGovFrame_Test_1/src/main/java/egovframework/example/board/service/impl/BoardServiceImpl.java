package egovframework.example.board.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.service.BoardSearchVO;
import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardsVO;
import egovframework.example.board.service.FileVO;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
	
	@Resource(name = "boardMapper")
	private BoardMapper boardMapper;
	
	@Resource(name = "txManager")
	protected DataSourceTransactionManager txManager;
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 글 개수
	@Override
	public int selectBoardCount(BoardSearchVO boardSearchVO) throws Exception {
		return boardMapper.selectBoardCount(boardSearchVO);
	}

	// 글 단건 조회
	@Override
	public BoardsVO selectBoardInfo(int no) throws Exception {
		return boardMapper.selectBoardInfo(no);
	}

	// 글 파일들 조회
	@Override
	public List<FileVO> selectFilesInfo(int no) throws Exception {
		return boardMapper.selectFilesInfo(no);
	}
	
	// 첨부파일 업로드
	@Override
	public void insertFiles(FileVO fileVO) throws Exception {
		boardMapper.insertFiles(fileVO);
	}

	// 첨부파일 수정
	@Override
	public void updateFiles(FileVO fileVO) throws Exception {
		boardMapper.updateFiles(fileVO);		
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
	
	
//	기능들을 Controller로부터 ServiceImpl 단으로 빼는 작업중
	// 글쓰기
	@Override
	@Transactional
    public void insertBoard(BoardsVO vo, 
    							     List<MultipartFile> multipartFiles
    							     ) throws Exception {
		
//		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		TransactionStatus txStatus = txManager.getTransaction(def);
		
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
				fileVO.setId(vo.getId());
				
				// 파일 정보 DB에 저장
				boardMapper.insertFiles(fileVO);
				
				// 파일 실제 저장
				multipartFile.transferTo(new File(filePath));
			}
		}
    }
	
	// 답변글쓰기
	@Override
	@Transactional
    public void insertAnswer(BoardsVO boardsVO, 
    								  int parentId, // parentId
    								  List<MultipartFile> multipartFiles
    								  ) throws Exception {
		
		// 비밀번호 암호화
		String originPw = boardsVO.getPassword();
		String encodedPw = passwordEncoder.encode(originPw);
		
		boardsVO.setPassword(encodedPw);
		
		// 답변글 등록
		boardMapper.insertBoard(boardsVO);
		boardsVO.setParentId(parentId);
		
		int boardId = boardsVO.getId();        
		
		// 글 type을 answer로 변경
		boardMapper.updateBoardType(boardId);
		boardMapper.updateParentId(boardsVO);
		
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
				fileVO.setId(boardId);
				
				// 파일 정보 DB에 저장
				boardMapper.insertFiles(fileVO);
				
				// 파일 저장 처리
				multipartFile.transferTo(new File(filePath));
			}
		}
		
    }

	// 게시글 수정(답변글 포함)
	@Override
	@Transactional
    public boolean updateBoard(BoardsVO vo, 
    								   int id, 
    								   String pw, 
    								   List<MultipartFile> multipartFiles, 
    								   List<Integer> deleteNo
    								   ) throws Exception {
		
        // 게시글 정보 가져오기
        BoardsVO boardsVO = boardMapper.selectBoardInfo(id);
        boolean isPwCorrect = passwordEncoder.matches(pw, boardsVO.getPassword());

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
                    fileVO.setId(vo.getId());

                    boardMapper.insertFiles(fileVO);

                    // 파일 실제 저장
                    multipartFile.transferTo(new File(filePath));
                }
            }
        }
        
        return true;  // 수정 성공
    }

	@Override
    public void downloadFiles(String extendedName, 
    						  HttpServletResponse res
    						  ) throws Exception {
		
        // 파일의 원래 이름을 가져오기 위해 서비스 호출
        FileVO fileVO = boardMapper.selectOriginalName(extendedName);
        String realName = fileVO.getFileName();

        // 다운로드할 파일을 가져올 경로
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
	public List<BoardsVO> selectBoardLists(BoardSearchVO searchVO) throws Exception {
		return boardMapper.selectBoardLists(searchVO);
	}
}
