package egovframework.example.board.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.service.BoardSearchVO;
import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardsVO;
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
	                        @RequestParam(required = false) String searchKeyword
	                        // @RequestParam(required = false) String isAnswered
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

	    // 게시글 목록 조회 (답변 포함)
	    List<BoardsVO> boardList = boardService.selectBoardLists(searchVO);

	    // 전체 게시글 수 조회
	    int totalCnt = boardService.selectBoardCount(searchVO);
	    paginationInfo.setTotalRecordCount(totalCnt); // 전체 게시글 수 설정

	    model.addAttribute("paginationInfo", paginationInfo);
	    model.addAttribute("boardList", boardList);

	    return "boardList";
	}

	
	/* 
	 * 
	 * 게시글 
	 *  
	 *  */ 
	// 글 쓰기 페이지이동
	@RequestMapping(value = "/boardPost.do")
	public String addPostPage() {
		return "boardPost";
	}
	
	// 글 쓰기 기능
	@RequestMapping(value = "/boardPost.do", method = RequestMethod.POST)
    public String addPost(@ModelAttribute("vo") BoardsVO vo,
                          @RequestParam("multiFile") List<MultipartFile> multipartFiles
                          ) throws Exception {

        boardService.insertBoard(vo, multipartFiles);
        
        return "redirect:boardList.do";
    }
	
	// 글 단건 조회
	@RequestMapping(value = "/boardInfo.do")
	public String boardInfo(@RequestParam("selectedBoardId") int boardId, Model model) throws Exception {
		boardService.updateView(boardId); // 조회수 증가
		BoardsVO boardsVO = boardService.selectBoardInfo(boardId);
		List<FileVO> fileVO = boardService.selectFilesInfo(boardId);
		
		model.addAttribute("boardInfo", boardsVO);
		model.addAttribute("fileInfo", fileVO);
		
		return "boardInfo";
	}
	
	// 글 수정 페이지이동
	@RequestMapping(value = "/boardUpdatePage.do")
	public String updatePostPage(@RequestParam("id") int id, 
								 @RequestParam("originPw") String pw,
								 Model model) throws Exception {
		
		BoardsVO boardsVO = boardService.selectBoardInfo(id);
		List<FileVO> fileVO = boardService.selectFilesInfo(id);
		Boolean isPwCorrect = passwordEncoder.matches(pw, boardsVO.getPassword());
		
		if (isPwCorrect) {
			model.addAttribute("boardInfo", boardsVO);
			model.addAttribute("fileInfo", fileVO);
			
			return "boardUpdate";
		} else {
			model.addAttribute("boardInfo", boardsVO);
			model.addAttribute("fileInfo", fileVO);
	        model.addAttribute("errorMsg", "비밀번호를 확인하세요.");
	        
	        return "boardInfo";
		}
	}
	
	// 글 수정 기능
	@RequestMapping(value = "/boardUpdate.do", method = RequestMethod.POST)
	public String updatePost(BoardsVO vo,
	                         @RequestParam("id") int id,
	                         @RequestParam("originPw") String pw,
	                         @RequestParam(value = "multiFile", required = false) List<MultipartFile> multipartFiles,
	                         @RequestParam(value = "deletedFileNo", required = false) List<Integer> deleteNo,
	                         Model model
							 ) throws Exception {
	    
	    boolean isUpdated = boardService.updateBoard(vo, id, pw, multipartFiles, deleteNo);
		List<FileVO> fileVO = boardService.selectFilesInfo(id);
		
	    // 수정이 성공했으면 목록으로 이동, 실패 시 원래 페이지로 리다이렉트
	    if (!isUpdated) {
	    	model.addAttribute("boardInfo", vo);
			model.addAttribute("fileInfo", fileVO);
	        model.addAttribute("errorMsg", "비밀번호가 틀렸습니다.");
	        return "boardUpdate";
	    }

	    return "redirect:/boardList.do";
	}	
	
	
	/* 
	 * 
	 * 답변글
	 *  
	 *  */ 
	// 답변 글 작성 페이지 이동
	@RequestMapping(value = "/boardAnswerPage.do")
	public String answerPage(@RequestParam("id") int id, Model model) throws Exception {
		BoardsVO boardsVO = boardService.selectBoardInfo(id);
		
		model.addAttribute("boardInfo", boardsVO);
		return "answerPost";
	}
	
	// 답변 글 작성 기능
	@RequestMapping(value = "/answerPost.do", method = RequestMethod.POST)
	public String addAnswerPost(BoardsVO boardsVO,
	                            @RequestParam("parentId") int parentId, // parentId
	                            @RequestParam("multiFile") List<MultipartFile> multipartFiles) throws Exception {

	    boardService.insertAnswer(boardsVO, parentId, multipartFiles);
	    
	    System.out.println("부모 id: " + parentId);
	    
	    return "redirect:boardList.do";
	}
	
	
	/* 
	 * 
	 * 공통
	 *  
	 *  */ 
	// 파일 다운로드
	@RequestMapping(value = "/fileDownload.do")
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception {
	    
	    // boardInfo.jsp에서 데이터 이름 가져오기
	    String extendedName = req.getParameter("extendedName"); // upload할 때 변경된 파일 이름
	    
	    boardService.downloadFiles(extendedName, res);
	}
	

	// 글 삭제 기능
	@RequestMapping(value = "/boardDelete.do")
	public String deleteBoard(@RequestParam("id") int id,
							  @RequestParam("originPw") String pw,
							  Model model) throws Exception {
		
		BoardsVO boardsVO = boardService.selectBoardInfo(id);
		List<FileVO> fileVO = boardService.selectFilesInfo(id);
		Boolean isPwCorrect = passwordEncoder.matches(pw, boardsVO.getPassword());
		
		if (isPwCorrect) {
			boardService.deletePost(id);
			
			return "redirect:boardList.do";
		} else {
			model.addAttribute("boardInfo", boardsVO);
			model.addAttribute("fileInfo", fileVO);
	        model.addAttribute("errorMsg", "비밀번호를 확인하세요.");
	        
	        return "boardInfo";
		}
	}
}