package egovframework.example.board.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.service.AnswerVO;
import egovframework.example.board.service.BoardSearchVO;
import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardVO;
import egovframework.example.board.service.FileVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class BoardController {
	
	@Resource(name = "boardService")
	private BoardService boardService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 글 목록(첫 페이지)
	@RequestMapping(value = "/boardList.do")
	public String boardList(
	                        ModelMap model,
	                        @ModelAttribute("searchVO") BoardSearchVO searchVO,
	                        @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
	                        @RequestParam(required = false, defaultValue = "1") String searchCondition,
	                        @RequestParam(required = false) String searchKeyword,
	                        @RequestParam(required = false) String isAnswered
	                        ) throws Exception {
	    // 페이지 번호와 검색 조건 설정
	    int pageIndex = Integer.parseInt(pageNo);
	    searchVO.setSearchKeyword(searchKeyword);
	    searchVO.setSearchCondition(searchCondition);
	    
	    searchVO.setPageIndex(pageIndex); // 페이지 인덱스 설정
	    
	    // context-properties.xml 파일에서 설정한 페이징 속성값
	    searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
	    searchVO.setPageSize(propertiesService.getInt("pageSize"));

	    // 페이징 설정
	    PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(searchVO.getPageIndex()); // 현재 페이지 번호
	    paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); // 한 페이지에 있는 게시글 수
	    paginationInfo.setPageSize(searchVO.getPageSize()); // 페이징 네비게이션에 나타날 페이지 수

	    searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	    searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

	    
	    // 게시글 목록 조회
	    List<BoardVO> boardList = boardService.selectBoardList(searchVO);
	    System.out.println(boardList.toString());
	    List<AnswerVO> answerList = boardService.selectAnswer();
	    
	    // 전체 게시글 수 조회
//	    int totalCnt = boardService.selectBoardCount(searchVO);
//	    paginationInfo.setTotalRecordCount(totalCnt); // 전체 게시글 수 설정
	    int totalCnt = boardService.selectBoardCount(searchVO);
	    totalCnt += boardService.selectAnswerCount(); // 답글 수를 포함하여 전체 게시물 수를 반영
	    paginationInfo.setTotalRecordCount(totalCnt); // 전체 게시글 수 설정
	   
	    model.addAttribute("paginationInfo", paginationInfo);
	    model.addAttribute("boardList", boardList);
	    model.addAttribute("answerList", answerList);

	    return "boardList";
	}

	
	// 글 쓰기 페이지이동
	@RequestMapping(value = "/boardPost.do")
	public String addPostPage() {
		return "boardPost";
	}
	
	// 글 쓰기 기능
	@RequestMapping(value = "/boardPost.do", method = RequestMethod.POST)
	public String addPost(@ModelAttribute("vo") BoardVO vo,
						  @ModelAttribute("fileVO") FileVO fileVO,
						  @RequestParam("multiFile") List<MultipartFile> multipartFiles
						 ) throws Exception {
		
		boardService.insertBoard(vo);	
		
		for (MultipartFile multipartFile : multipartFiles) {
	        if (multipartFile.isEmpty()) {
	        	// 파일 추가가 없을 땐 밑 로직 생략 
	        	System.out.println("추가되거나 삭제된 파일이 없음 \t");
	            continue;
	        }
	        
	        // 파일 정보 설정
	        String originName = multipartFile.getOriginalFilename();
	        String extensionName = FilenameUtils.getExtension(originName);
	        UUID uuid = UUID.randomUUID();
	        String extendedName = uuid + "." + extensionName;
	        Long fileSize = multipartFile.getSize();
	        
	        // 파일 저장 경로
	        String filePath = "D:\\upload\\" + extendedName;

<<<<<<< HEAD
		boardService.insertBoard(vo);
		
		List<Map<String, String>> fileList = new ArrayList<>();
				
		for (int i = 0; i < multipartFiles.size(); i++) {
			String extendedName = null;
			Long fileSize = null;
			
			String originName = multipartFiles.get(i).getOriginalFilename();
			String extentionName = FilenameUtils.getExtension(originName);
			UUID uuid = UUID.randomUUID();
			extendedName = uuid + "." + extentionName;
			fileSize = multipartFiles.get(i).getSize();
			
			extendedName = new String(extendedName.getBytes("UTF-8"), "8859_1");
			// originName = URLEncoder.encode(originName, "UTF-8");
			
			Map<String, String> map = new HashMap<>();
			map.put("originName", originName);
			map.put("extendedName", extendedName);
			
			fileList.add(map);
			
			fileVO.setFileName(originName);
			fileVO.setExtendedName(extendedName);
			fileVO.setFileSize(fileSize);
			fileVO.setNo(vo.getNo());
			
			boardService.insertFiles(fileVO);
			
			multipartFiles.get(i).transferTo(new File("D:\\upload\\" + fileList.get(i).get("extendedName")));
=======
	        // FileVO 인스턴스 생성
	        // FileVO fileVO = new FileVO();
	        fileVO.setFileName(originName);
	        fileVO.setExtendedName(extendedName);
	        fileVO.setFileSize(fileSize);
	        fileVO.setNo(vo.getNo());
	        
	        // DB에 파일 정보 저장
	        boardService.insertFiles(fileVO);
	        
	        // 파일 저장
	        try {
	            multipartFile.transferTo(new File(filePath));
	        } catch (IOException e) {
	            // 예외 처리 (예: 로그 기록, 사용자에게 오류 메시지 표시 등)
	            e.printStackTrace();
	            // 필요시 특정 페이지로 리다이렉트할 수 있음
	        }
>>>>>>> branch 'main' of https://github.com/SeoYeongHoon/egov_prac.git
		
		}
		
		return "redirect:boardList.do";
	}
	
	// 답변 글 작성 페이지 이동
	@RequestMapping(value = "/boardAnswerPage.do")
	public String answerPage(@RequestParam("no") int no, Model model) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.selectBoardInfo(no);
		
		model.addAttribute("boardInfo", boardVO);
		return "answerPost";
	}
	
	// 답변 글 작성 기능
	@RequestMapping(value = "/answerPost.do", method = RequestMethod.POST)
	public String addAnswerPost(@ModelAttribute("answerVO") AnswerVO answerVO,
								@ModelAttribute("vo") BoardVO vo,
							    @ModelAttribute("fileVO") FileVO fileVO,
							    @RequestParam("no") int no,
							    @RequestParam("multiFile") List<MultipartFile> multipartFiles
							    ) throws Exception {
		
		boardService.insertAnswer(answerVO);
		boardService.updateAnswerStatus(no);
		
		List<Map<String, String>> fileList = new ArrayList<>();
		
		System.out.println("파일들: " + multipartFiles);
		
		
		for (int i = 0; i < multipartFiles.size(); i++) {
			String extendedName = null;
			Long fileSize = null;
			
			String originName = multipartFiles.get(i).getOriginalFilename();
			String extentionName = FilenameUtils.getExtension(originName);
			UUID uuid = UUID.randomUUID();
			extendedName = uuid + "." + extentionName;
			fileSize = multipartFiles.get(i).getSize();
			
			extendedName = new String(extendedName.getBytes("UTF-8"), "8859_1");
			// originName = URLEncoder.encode(originName, "UTF-8");
			
			Map<String, String> map = new HashMap<>();
			map.put("originName", originName);
			map.put("extendedName", extendedName);
			
			fileList.add(map);
			
			fileVO.setFileName(originName);
			fileVO.setExtendedName(extendedName);
			fileVO.setFileSize(fileSize);
			fileVO.setAnswerNo(answerVO.getAnswerNo());
			
			boardService.insertAnsweredFiles(fileVO);
			
			multipartFiles.get(i).transferTo(new File("D:\\upload\\" + fileList.get(i).get("extendedName")));
		
		}
		
		return "redirect:boardList.do";
	}
	
	// 글 단건 조회
	@RequestMapping(value = "/boardInfo.do")
	public String boardInfo(@RequestParam("selectedBoardId") int boardId, Model model) throws Exception {
		boardService.updateView(boardId); // 조회수 증가
		BoardVO boardVO = boardService.selectBoardInfo(boardId);
		List<FileVO> fileVO = boardService.selectFilesInfo(boardId);
		
		model.addAttribute("boardInfo", boardVO);
		model.addAttribute("fileInfo", fileVO);
		System.out.println("파일 정보: " + fileVO.toString());
		
		
		return "boardInfo";
	}
	
	// 답변글 단건 조회
	@RequestMapping(value = "/answerInfo.do")
	public String answerInfo(@RequestParam("selectedAnswerNo") int answerNo, Model model) throws Exception {
		boardService.updateView(answerNo); // 조회수 증가
		AnswerVO answerVO = boardService.selectAnswerInfo(answerNo);
		List<FileVO> fileVO = boardService.selectAnswerFilesInfo(answerNo);
		
		model.addAttribute("answerInfo", answerVO);
		model.addAttribute("fileInfo", fileVO);
		
		return "answerInfo";
	}
	
	// 답변글 수정 페이지이동
	@RequestMapping(value = "/answerUpdatePage.do")
	public String updateAnswerPage(@RequestParam("no") int no, 
								   @RequestParam("originPw") String pw,
								   Model model) throws Exception {
		
		AnswerVO answerVO = new AnswerVO();
		answerVO = boardService.selectAnswerInfo(no);
		Boolean isPwCorrect = passwordEncoder.matches(pw, answerVO.getPassword());
		
		if (isPwCorrect) {
			List<FileVO> fileVO = boardService.selectAnswerFilesInfo(no);
			
			model.addAttribute("answerInfo", answerVO);
			model.addAttribute("fileInfo", fileVO);
			
			return "answerUpdate";
		} else {
			return "redirect:boardList.do";
		}
		
		
	}
	
	// 답변글 수정 기능
	@RequestMapping(value = "/answerUpdate.do", method = RequestMethod.POST)
	public String updateAnswer(BoardVO vo,
							 @RequestParam("answerNo") int no, 
							 @RequestParam("originPw") String pw,
							 @RequestParam("multiFile") List<MultipartFile> multipartFiles,
							 @RequestParam("deletedFileNo") List<Integer> deleteNo
							 ) throws Exception {
		
		AnswerVO answerVO = new AnswerVO();
		answerVO = boardService.selectAnswerInfo(no);
		Boolean isPwCorrect = passwordEncoder.matches(pw, answerVO.getPassword());
		
		for (int i = 0; i < deleteNo.size(); i++) {
			System.out.println("삭제할 번호: " + deleteNo.get(i));	
			boardService.deleteFiles(deleteNo.get(i));
		}
		
		if (isPwCorrect) {
			boardService.updateBoard(vo);
			
			for (MultipartFile multipartFile : multipartFiles) {
				System.out.println("파일 이름들: " + multipartFile.getOriginalFilename());
				
		        if (multipartFile.isEmpty()) {
		        	// 파일 추가가 없을 땐 밑 로직 생략 
		        	System.out.println("추가되된 파일이 없음 \t");
		            continue;
		        }
		        
		        // 파일 정보 설정
		        String originName = multipartFile.getOriginalFilename();
		        String extensionName = FilenameUtils.getExtension(originName);
		        UUID uuid = UUID.randomUUID();
		        String extendedName = uuid + "." + extensionName;
		        Long fileSize = multipartFile.getSize();
		        
		        // 파일 저장 경로
		        String filePath = "D:\\upload\\" + extendedName;

		        // FileVO 인스턴스 생성
		        FileVO fileVO = new FileVO();
		        fileVO.setFileName(originName);
		        fileVO.setExtendedName(extendedName);
		        fileVO.setFileSize(fileSize);
		        fileVO.setNo(vo.getNo());
		        
		        // DB에 파일 정보 저장      
		        boardService.insertFiles(fileVO);
		        
		        // 파일 저장
		        try {
		            multipartFile.transferTo(new File(filePath));
		        } catch (IOException e) {
		            // 예외 처리 (예: 로그 기록, 사용자에게 오류 메시지 표시 등)
		            e.printStackTrace();
		            // 필요시 특정 페이지로 리다이렉트할 수 있음
		        }
			
			}
			return "redirect:boardList.do";
		} else {
			return "redirect:boardList.do";
		}
	}
	
	// 글 수정 페이지이동
	@RequestMapping(value = "/boardUpdatePage.do")
	public String updatePostPage(@RequestParam("no") int no, 
								 @RequestParam("originPw") String pw,
								 Model model) throws Exception {
		
		
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.selectBoardInfo(no);
		Boolean isPwCorrect = passwordEncoder.matches(pw, boardVO.getPassword());
		
		if (isPwCorrect) {
			List<FileVO> fileVO = boardService.selectFilesInfo(no);
			
			model.addAttribute("boardInfo", boardVO);
			model.addAttribute("fileInfo", fileVO);
			
			return "boardUpdate";
		} else {
			return "redirect:boardList.do";
		}
		
		
	}
	
	// 글 수정 기능
	@RequestMapping(value = "/boardUpdate.do", method = RequestMethod.POST)
<<<<<<< HEAD
	public String updatePost(BoardVO vo, 
	                         @RequestParam(value = "fileId", required = false) List<Integer> fileNo,
	                         @RequestParam("multiFile") List<MultipartFile> multipartFiles) throws Exception {
	    
	    // 게시판 글 수정
	    boardService.updateBoard(vo);
	    
	    if (multipartFiles.isEmpty()) {
	        return "redirect:boardList.do"; // 파일이 없으면 바로 목록으로 리다이렉트
	    }
	    
	    for (MultipartFile multipartFile : multipartFiles) {
	        if (multipartFile.isEmpty()) {
	            continue; // 비어있는 파일은 무시
	        }
	        
	        // 파일 정보 설정
	        String originName = multipartFile.getOriginalFilename();
	        String extensionName = FilenameUtils.getExtension(originName);
	        UUID uuid = UUID.randomUUID();
	        String extendedName = uuid + "." + extensionName;
	        Long fileSize = multipartFile.getSize();
	        
	        // 파일 저장 경로
	        String filePath = "D:\\upload\\" + extendedName;

	        // FileVO 인스턴스 생성
	        FileVO fileVO = new FileVO();
	        fileVO.setFileName(originName);
	        fileVO.setExtendedName(extendedName);
	        fileVO.setFileSize(fileSize);
	        fileVO.setNo(vo.getNo());
	        
	        // DB에 파일 정보 저장
	        boardService.insertFiles(fileVO);
	        
	        // 파일 저장
	        try {
	            multipartFile.transferTo(new File(filePath));
	        } catch (IOException e) {
	            // 예외 처리 (예: 로그 기록, 사용자에게 오류 메시지 표시 등)
	            e.printStackTrace();
	            // 필요시 특정 페이지로 리다이렉트할 수 있음
	        }
	    }
	    
	    return "redirect:boardList.do";
=======
	public String updatePost(BoardVO vo,
							 @RequestParam("no") int no, 
							 @RequestParam("originPw") String pw,
							 @RequestParam("multiFile") List<MultipartFile> multipartFiles,
							 @RequestParam("deletedFileNo") List<Integer> deleteNo
							 ) throws Exception {
		
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.selectBoardInfo(no);
		Boolean isPwCorrect = passwordEncoder.matches(pw, boardVO.getPassword());
		
		for (int i = 0; i < deleteNo.size(); i++) {
			System.out.println("삭제할 번호: " + deleteNo.get(i));	
			boardService.deleteFiles(deleteNo.get(i));
		}
		
		if (isPwCorrect) {
			boardService.updateBoard(vo);
			
			for (MultipartFile multipartFile : multipartFiles) {
				System.out.println("파일 이름들: " + multipartFile.getOriginalFilename());
				
		        if (multipartFile.isEmpty()) {
		        	// 파일 추가가 없을 땐 밑 로직 생략 
		        	System.out.println("추가되된 파일이 없음 \t");
		            continue;
		        }
		        
		        // 파일 정보 설정
		        String originName = multipartFile.getOriginalFilename();
		        String extensionName = FilenameUtils.getExtension(originName);
		        UUID uuid = UUID.randomUUID();
		        String extendedName = uuid + "." + extensionName;
		        Long fileSize = multipartFile.getSize();
		        
		        // 파일 저장 경로
		        String filePath = "D:\\upload\\" + extendedName;

		        // FileVO 인스턴스 생성
		        FileVO fileVO = new FileVO();
		        fileVO.setFileName(originName);
		        fileVO.setExtendedName(extendedName);
		        fileVO.setFileSize(fileSize);
		        fileVO.setNo(vo.getNo());
		        
		        // DB에 파일 정보 저장      
		        boardService.insertFiles(fileVO);
		        
		        // 파일 저장
		        try {
		            multipartFile.transferTo(new File(filePath));
		        } catch (IOException e) {
		            // 예외 처리 (예: 로그 기록, 사용자에게 오류 메시지 표시 등)
		            e.printStackTrace();
		            // 필요시 특정 페이지로 리다이렉트할 수 있음
		        }
			
			}
			return "redirect:boardList.do";
		} else {
			return "redirect:boardList.do";
		}
>>>>>>> branch 'main' of https://github.com/SeoYeongHoon/egov_prac.git
	}

	
	// 글 삭제 기능
	@RequestMapping(value = "/boardDelete.do")
	public String deleteBoard(@RequestParam("no") int no,
							  @RequestParam("originPw") String pw) throws Exception {
		
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.selectBoardInfo(no);
		Boolean isPwCorrect = passwordEncoder.matches(pw, boardVO.getPassword());
		
		if (isPwCorrect) {
			boardService.deletePost(no);
			return "redirect:boardList.do";
		} else {
			return "redirect:boardList.do";
		}
	}
	
	// 답변글 삭제 기능
	@RequestMapping(value = "/answerDelete.do")
	public String deleteAnswer(@RequestParam("no") int answerNo,
							   @RequestParam("originPw") String pw) throws Exception {
		
		AnswerVO answerVO = new AnswerVO();
		answerVO = boardService.selectAnswerInfo(answerNo);
		Boolean isPwCorrect = passwordEncoder.matches(pw, answerVO.getPassword());
		
		if (isPwCorrect) {
			boardService.deleteAnswer(answerNo);
			return "redirect:boardList.do";
		} else {
			return "redirect:boardList.do";
		}
	}
	
	// 파일 다운로드
	@RequestMapping(value = "/fileDownload.do")
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception {
<<<<<<< HEAD
	    
	    // boardInfo.jsp에서 데이터 이름 가져오기
	    String extendedName = req.getParameter("extendedName"); // upload할 때 변경된 파일 이름
	    // String realName = req.getParameter("fileName"); // 원래 파일명(ex: apple.jpg)
	    
	    // 파일의 원래 이름을 가져오기 위해 service 호출
	    FileVO fileVO = boardService.selectOriginalName(extendedName);
	    String realName = fileVO.getFileName();
	    System.out.println("파일이름: " + realName);
	    
	    // 다운로드할 파일의 경로 설정
	    String downPathFrom = "D:\\upload\\" + extendedName;
	    
	    // 파일 객체 생성
	    File file = new File(downPathFrom);
	    
	    // 파일이 존재하지 않을 경우 처리
	    if (!file.exists()) {
	        return; // 파일이 없으면 아무 것도 하지 않고 종료
	    }
	    
	    // 여기서부터 주석 필요
	    // 파일을 읽기 위한 FileInputStream 생성
	    FileInputStream fileInputStream = new FileInputStream(downPathFrom);
=======
		
		// boardInfo.jsp에서 데이터 이름 가져오기
		String extendedName = req.getParameter("extendedName"); // upload할 때 변경된 파일 이름
		
		FileVO fileVO = boardService.selectOriginalName(extendedName);
		String realName = fileVO.getFileName();
		System.out.println("파일이름: " + realName);
		
		// 파일 있는 경로
		String downPathFrom = "D:\\upload\\" + extendedName;
		
		// 에서 추출
		File file = new File(downPathFrom);
		if (!file.exists()) {
			return;
		}
		
		// 여기서부터 주석 필요
		FileInputStream fileInputStream = new FileInputStream(downPathFrom);
>>>>>>> branch 'main' of https://github.com/SeoYeongHoon/egov_prac.git

	    // 파일 이름 인코딩 처리 (한글 파일명 지원)
	    extendedName = new String(extendedName.getBytes("UTF-8"), "8859_1");
	    realName = URLEncoder.encode(realName, "UTF-8"); // 한글 이름 파일 다운 가능하게 인코딩
	    
	    // 응답의 콘텐츠 유형을 설정
	    res.setContentType("application/octet-stream");
	    // 다운로드할 파일 이름을 설정
	    res.setHeader("Content-Disposition", "attachment; filename=" + realName); // 다운로드하는 파일 이름을 원래 이름으로 설정
	    
	    // 응답의 OutputStream을 가져옴
	    OutputStream outputStream = res.getOutputStream();
	    
	    int length;
	    // 파일의 크기에 맞춰 바이트 배열 생성
	    byte[] b = new byte[(int) file.length()];
	    
	    // 파일을 읽어서 클라이언트로 전송
	    while ((length = fileInputStream.read(b)) > 0) {
	        outputStream.write(b, 0, length); // 읽은 데이터만큼 OutputStream에 쓰기
	    }

	    // OutputStream을 비움
	    outputStream.flush();

	    // 스트림 닫기
	    outputStream.close(); // OutputStream 닫기
	    fileInputStream.close(); // FileInputStream 닫기
	}
<<<<<<< HEAD

	
	
	// 글 조회수 기능
	public int postView(int no) throws Exception {
		return boardService.updateView(no);
	}
	
	// 답변글 조회수 기능
	public int answerView(int no) throws Exception {
		return boardService.updateAnswerView(no);
	}
	
	// 첨부파일 삭제 기능
	public void deleteFile(int fileNo) throws Exception {
		boardService.deleteFile(fileNo);
	}
=======
>>>>>>> branch 'main' of https://github.com/SeoYeongHoon/egov_prac.git
}