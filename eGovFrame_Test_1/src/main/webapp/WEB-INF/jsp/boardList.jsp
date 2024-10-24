<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" 
href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" 
integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" 
crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link type="text/css" rel="stylesheet" href="/eGovFrame_Test_1/css/egovframework/board.css" />
</head>
<script>    
    function searchKeywordBtn() {
        let url = "boardList.do";
        url += "?searchCondition=" + $('#searchCondition').val();
        url += "&searchKeyword=" + $('#searchKeyword').val();
        location.href = url;
        console.log(url);
    }
</script>
<style>
	#boardContainer {
		max-width: 1000px;
		margin: 30px auto;
	}
	
	.paging {
		text-align: center;
	    margin-top: 30px;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	}
	
	.paging-number-on {
		background-color: darkgray;
	    display: inline-block;
	    color: white;
	    font-size: 18pt;
	    width: 40px;
	    height: 40px;
	    margin: 5px;
	    border-radius: 5px;
	}
	
	.paging-number-off {
		display: inline-block;
	    color: black;
	    font-size: 18pt;
	    width: 40px;
	    height: 40px;
	    margin: 5px;
	    border-radius: 5px;
	    border: 1px solid darkgray;
	}
	.paging-number-off:hover {
		text-decoration: none;
		background-color: darkgray;
	    color: white;
	}
	
	.paging-move-btn {
		display: inline-block;
	    color: black;
	    font-size: 17pt;
	    width: 40px;
	    height: 40px;
	    margin: 5px;
	    border-radius: 5px;
	    border: 1px solid darkgray;
	}
	.paging-move-btn:hover {
		text-decoration: none;
		background-color: darkgray;
	    color: white;
	}
</style>
<body>
	<div id="boardContainer">
		
		<!-- 검색 구역 -->
		<div id="searchArea">
			<ul class="search-content">
				<li class="search-list">
					<label class="search-label" for="searchCondition">검색조건</label>
					<select id="searchCondition" name="searchCondition" style="width: 250px; height: 35px; border: 1px solid rgb(0, 0, 0, 0.1);">
						<option value="1">작성자</option>
						<option value="0">제목</option>
					</select>
				</li>
				
				<li class="search-list">
					<label class="search-label" for="searchKeyword">검색어</label>
					<input id="searchKeyword" name="searchKeyword" style="width: 250px; height: 35px; border: 1px solid rgb(0, 0, 0, 0.1);" placeholder="검색어를 입력하세요." />
				</li>
	        </ul>
	        <button type="button" onClick="searchKeywordBtn()" class="search-btn">검색</button>
		</div>
		
		<!-- 전체 게시글 수 -->
		<div class="total-posts">
			<p style="margin-bottom: 5px;">전체: &nbsp<span style="color: red; font-weight: bold;">${paginationInfo.totalRecordCount }</span>건</p>		
		</div>
		
		<!-- 게시판 리스트 폼 -->
		<%-- <form id="boardListForm" name="boardListForm" method="POST">
		    <input type="hidden" name="pageIndex" value="1">
		    <input type="hidden" name="pageSize" value="10">  <!-- 한 페이지당 게시글 수 -->
		</form> --%>
		
		
		<!-- 목록 구역 -->
		<form id="boardListForm" name="boardListForm" method="POST">
		    <input type="hidden" name="selectedBoardId" />
		    <input type="hidden" name="selectedAnswerNo" />
		    <div id="listArea">
		        <table class="table table-bordered">
		            <thead class="thead-light" style="text-align: center;">
		                <tr>
		                    <th>순번</th>
		                    <th style="width: 440px;">제목</th>
		                    <th>작성자</th>
		                    <th style="width: 160px;">등록일</th>
		                    <th style="width: 100px;">조회수</th>
		                </tr>
		            </thead>
		            <tbody id="listBodyArea">
		                <c:forEach var="list" items="${boardList}" varStatus="status">
	                    	<input type="hidden" value="${list.isAnswer }" />
		                    <tr>
		                        <td style="text-align: center;">
		                            <c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/>
		                        </td>
		                        <td class="info-select" onClick="selectBoardInfo('<c:out value="${list.no }" />')">
		                            <c:out value="${list.title }"/>
		                        </td>
		                        <td style="text-align: center;"><c:out value="${list.writer }"/></td>
		                        <td style="text-align: center;"><c:out value="${list.date }"/></td>
		                        <td style="text-align: center;"><c:out value="${list.view }"/></td>
		                    </tr>
		                    
		                    <c:set var="isAnswer" value="${list.isAnswer }" />
		                    <c:forEach var="answer" items="${answerList }" varStatus="status">
		                    	<c:if test="${isAnswer eq 1 and list.no eq answer.no}">
		                            <tr style="background-color: aliceblue;">
		                                <td style="text-align: center">[답변]</td>
		                                <td class="info-select" onClick="selectAnswerInfo('<c:out value="${answer.answerNo }" />')">
											ㄴ &nbsp<c:out value="${answer.title }"/>
		                                </td>
		                                <td style="text-align: center;"><c:out value="${answer.writer }"/></td>
		                                <td style="text-align: center;"><c:out value="${answer.date }"/></td>
		                                <td style="text-align: center;"><c:out value="${answer.view }"/></td>
		                            </tr>
		                        </c:if>
		                    </c:forEach>
		                </c:forEach>
		            </tbody>
		        </table>
		    </div>
		</form>
		
		<!-- 페이징 -->
		<div class="paging">
			<c:choose>
				<c:when test="${paginationInfo.currentPageNo > 1}">
					<a class="paging-move-btn" href="javascript:goToPage(1)">&laquo;</a>
		        	<a class="paging-move-btn" href="javascript:goToPage(${paginationInfo.currentPageNo - 1})">&lt;</a>
				</c:when>
				<c:otherwise>
					<a class="paging-move-btn" href="#">&laquo;</a>
		        	<a class="paging-move-btn" href="#">&lt;</a>
				</c:otherwise>
			</c:choose>
		    
		    <c:forEach var="num" begin="${paginationInfo.firstPageNoOnPageList}" end="${paginationInfo.lastPageNoOnPageList}">
		        <c:choose>
		            <c:when test="${num == paginationInfo.currentPageNo}">
		                <span class="paging-number-on">${num}</span>
		            </c:when>
		            <c:otherwise>
		                <a class="paging-number-off" href="javascript:goToPage(${num})">${num}</a>
		            </c:otherwise>
		        </c:choose>
		    </c:forEach>
		    
		    <c:choose>
		    	<c:when test="${paginationInfo.currentPageNo < paginationInfo.totalPageCount}">
		    		<a class="paging-move-btn" href="javascript:goToPage(${paginationInfo.currentPageNo + 1})">&gt;</a>
		        	<a class="paging-move-btn" href="javascript:goToPage(${paginationInfo.totalPageCount})">&raquo;</a>
		    	</c:when>
		    	<c:otherwise>
		    		<a class="paging-move-btn" href="#">&gt;</a>
		        	<a class="paging-move-btn" href="#">&raquo;</a>
		    	</c:otherwise>
		    </c:choose>
		</div>
		
		<!-- 글쓰기 버튼 -->
		<div class="post-btn-container">
			<button class="post-btn" type="button" onclick="location.href='boardPost.do'">글쓰기</button>
		</div>
	</div>
</body>
<script>
	function goToPage(pageNo) {
	    var form = document.getElementById('boardListForm');
	    var existingInput = form.querySelector('input[name="pageNo"]');
	    if (existingInput) {
	        form.removeChild(existingInput);
	    }
	    
	    var pageNoInput = document.createElement('input');
	    pageNoInput.setAttribute('type', 'hidden');
	    pageNoInput.setAttribute('name', 'pageNo');
	    pageNoInput.setAttribute('value', pageNo);
	    form.appendChild(pageNoInput);
	    
	    form.submit();
	}

	function selectBoardInfo(boardId) {		
		document.boardListForm.selectedBoardId.value = boardId;
		document.boardListForm.action = "<c:url value = 'boardInfo.do' />";
		document.boardListForm.submit();
	}
	
	function selectAnswerInfo(answerId) {
		document.boardListForm.selectedAnswerNo.value = answerId;
		document.boardListForm.action = "<c:url value = 'answerInfo.do' />";
		document.boardListForm.submit();
	}
</script>
</html>