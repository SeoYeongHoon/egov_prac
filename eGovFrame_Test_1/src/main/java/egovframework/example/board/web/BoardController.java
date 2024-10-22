package egovframework.example.board.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
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

	// 글 목록(첫 페이지)
	@RequestMapping(value = "/boardList.do")
	public String boardList(
	                        ModelMap model,
	                        @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
	                        @ModelAttribute("searchVO") BoardSearchVO searchVO,
	                        @RequestParam(required = false, defaultValue = "1") String searchCondition,
	                        @RequestParam(required = false) String searchKeyword
	                        ) throws Exception {

	    // 페이지 번호와 검색 조건 설정
	    int pageIndex = Integer.parseInt(pageNo);
	    searchVO.setSearchKeyword(searchKeyword);
	    searchVO.setSearchCondition(searchCondition);
	    
	    searchVO.setPageIndex(pageIndex); // 페이지 인덱스 설정
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
	    List<AnswerVO> answerList = boardService.selectAnswer();
	    
	    // 전체 게시글 수 조회
	    int totalCnt = boardService.selectBoardCount(searchVO);
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
			fileVO.setNo(vo.getNo());
			
			boardService.insertFiles(fileVO);
			
			multipartFiles.get(i).transferTo(new File("D:\\upload\\" + fileList.get(i).get("extendedName")));
		
		}
		
		return "redirect:boardList.do";
	}
	
	// 답변 글 작성 페이지 이동
	@RequestMapping(value = "/boardAnswerPage.do")
	public String answerPage(@RequestParam("no") int no, Model model) throws Exception {
		System.out.println("번호: " + no);
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.selectBoardInfo(no);
		
		model.addAttribute("boardInfo", boardVO);
		return "boardAnswer";
	}
	
	// 답변 글 작성 기능
	@RequestMapping(value = "/answerPost.do", method = RequestMethod.POST)
	public String addAnswerPost(@ModelAttribute("answerVO") AnswerVO answerVO,
							    @ModelAttribute("fileVO") FileVO fileVO,
							    @RequestParam("no") int no,
							    @RequestParam("multiFile") List<MultipartFile> multipartFiles
							    ) throws Exception {
		
		boardService.insertAnswer(answerVO);
		boardService.updateAnswerStatus(no);
		
//		List<Map<String, String>> fileList = new ArrayList<>();
//		
//		for (int i = 0; i < multipartFiles.size(); i++) {
//			String extendedName = null;
//			Long fileSize = null;
//			
//			String originName = multipartFiles.get(i).getOriginalFilename();
//			String extentionName = FilenameUtils.getExtension(originName);
//			UUID uuid = UUID.randomUUID();
//			extendedName = uuid + "." + extentionName;
//			fileSize = multipartFiles.get(i).getSize();
//			
//			extendedName = new String(extendedName.getBytes("UTF-8"), "8859_1");
//			// originName = URLEncoder.encode(originName, "UTF-8");
//			
//			Map<String, String> map = new HashMap<>();
//			map.put("originName", originName);
//			map.put("extendedName", extendedName);
//			
//			fileList.add(map);
//			
//			fileVO.setFileName(originName);
//			fileVO.setExtendedName(extendedName);
//			fileVO.setFileSize(fileSize);
//			fileVO.setNo(answerVO.getNo());
//			
//			boardService.insertFiles(fileVO);
//			
//			multipartFiles.get(i).transferTo(new File("D:\\upload\\" + fileList.get(i).get("extendedName")));
//		
//		}
		
		return "redirect:boardList.do";
	}
	
	// 글 단건 조회
	@RequestMapping(value = "/boardInfo.do")
	public String boardInfo(@RequestParam("selectedBoardId") int boardId, Model model) throws Exception {
		BoardVO boardVO = boardService.selectBoardInfo(boardId);
		List<FileVO> fileVO = boardService.selectFilesInfo(boardId);
		
		System.out.println("파일 정보: " + fileVO.toString());
		
		model.addAttribute("boardInfo", boardVO);
		model.addAttribute("fileInfo", fileVO);
		
		postView(boardId); // 조회수 증가
		
		return "boardInfo";
	}
	
	// 답변글 단건 조회
	@RequestMapping(value = "/answerInfo.do")
	public String answerInfo(@RequestParam("selectedAnswerNo") int answerNo, Model model) throws Exception {
		AnswerVO answerVO = boardService.selectAnswerInfo(answerNo);
		
		model.addAttribute("answerInfo", answerVO);
		
		return "answerInfo";
	}
	
	// 글 수정 페이지이동
	@RequestMapping(value = "/boardUpdatePage.do")
	public String updatePostPage(@RequestParam("no") int no, Model model) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.selectBoardInfo(no);
		
		List<FileVO> fileVO = boardService.selectFilesInfo(no);
		
		model.addAttribute("boardInfo", boardVO);
		model.addAttribute("fileInfo", fileVO);
		
		return "boardUpdate";
	}
	
	// 글 수정 기능
	@RequestMapping(value = "/boardUpdate.do", method = RequestMethod.POST)
	public String updatePost(BoardVO vo, FileVO fileVO) throws Exception {
//		// 파일 업로드
//		String fileName = null;
//		String extendedName = null;
//		Long fileSize = null;
//		MultipartFile uploadFile = fileVO.getUploadFile();
//		
//		if (!uploadFile.isEmpty()) {
//			String originName = uploadFile.getOriginalFilename(); // 파일명
//			String extensionName = FilenameUtils.getExtension(originName); // 확장자
//			UUID uuid = UUID.randomUUID(); // 고유 식별자
//			fileName = originName; // 첨부파일 파일 이름 표시용
//			extendedName = uuid + "." + extensionName; // 파일 저장용 파일명
//			
//			fileSize = uploadFile.getSize();
//			
//			uploadFile.transferTo(new File("D:\\upload\\" + extendedName)); // 파일 내보내기
//		}
//		
//		fileVO.setFileName(fileName);
//		fileVO.setExtendedName(extendedName);
//		fileVO.setFileSize(fileSize);
		
		boardService.updateBoard(vo);
				
		return "redirect:boardList.do";
	}
	
	// 글 삭제 기능
	@RequestMapping(value = "/boardDelete.do")
	public String deletePost(@RequestParam("no") int no) throws Exception {
		boardService.deletePost(no);
		
		return "redirect:boardList.do";
	}
	
	// 파일 다운로드
	@RequestMapping(value = "/fileDownload.do")
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// boardInfo.jsp에서 데이터 이름 가져오기
		String extendedName = req.getParameter("extendedName"); // upload할 때 변경된 파일 이름
		// String realName = req.getParameter("fileName"); // 원래 파일명(ex: apple.jpg)
		
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

		extendedName = new String(extendedName.getBytes("UTF-8"), "8859_1");
		realName = URLEncoder.encode(realName, "UTF-8"); // 한글 이름 파일 다운가능하게 인코딩
		
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment; filename=" + realName); // 다운로드 하는 파일 이름 원래 이름으로 설정
		
		OutputStream outputStream = res.getOutputStream();
		
		int length;
		byte[] b = new byte[(int) file.length()];
		while ((length = fileInputStream.read(b)) > 0) {
			outputStream.write(b, 0, length);
		}

		outputStream.flush();

		outputStream.close();
		fileInputStream.close();
	}
	
	
	// 글 조회수 기능
	public int postView(int no) throws Exception {
		return boardService.updateView(no);
	}
	
}