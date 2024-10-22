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
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>"/>
<link rel="stylesheet" 
href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" 
integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" 
crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<script>
	/* function fn_egov_link_page(pageNo){
		location.href = "boardList.do?pageNo=" + pageNo;
	} */
	function pagination(pageNo) {
    	document.boardListForm.pageIndex.value = pageNo;
    	document.boardListForm.action = "<c:url value='/boardList.do'/>";
       	document.boardListForm.submit();
    }
	
	function updateBoardList(pageNo) {
		let listArea = $("#boardListBody");
        
		listArea.html('');
	}
	
	/* $(document).ready(function() {
        goPage(1); // 첫 페이지를 로드
    });

    // 게시글 목록과 페이징 UI 업데이트 함수는 위에서 설명한 대로 포함
    function goPage(pageIndex) {
        var pageSize = 10; // 한 페이지당 게시글 수
        var pageIndex = 1;

        $.ajax({
            url: "boardList.do",  // 서버에서 처리할 URL로 변경
            type: "POST",
            data: {
                pageIndex: pageIndex,
                pageSize: pageSize
            },
            success: function(data) {
                updateBoardList(data.boardList);
                updatePagination(data.paginationInfo);
            },
            error: function(xhr, status, error) {
                console.log("ERROR: ", error);
            }
        });
    }

    // 게시글 목록 업데이트 함수
    function updateBoardList(boardList) {
        var listArea = $("#listArea");
        var html = '<table class="table table-bordered">';
        html += '<thead class="thead-light" style="text-align: center;">';
        html += '<tr>';
        html += '<th>순번</th><th>제목</th><th>작성자</th><th>등록일</th><th>조회수</th>';
        html += '</tr></thead><tbody>';

        $.each(boardList, function(index, item) {
            html += '<tr>';
            html += '<td style="text-align: center;">' + (item.rowNum) + '</td>';
            html += '<td class="info-select" onClick="selectBoardInfo(\'' + item.no + '\')">' + item.title + '</td>';
            html += '<td style="text-align: center;">' + item.writer + '</td>';
            html += '<td style="text-align: center;">' + item.date + '</td>';
            html += '<td style="text-align: center;">' + item.view + '</td>';
            html += '</tr>';
        });

        html += '</tbody></table>';
        listArea.html(html);  // 리스트 업데이트
    }

    // 페이징 UI 업데이트 함수
    function updatePagination(paginationInfo) {
        var paginationArea = $("#paginationArea");
        var html = '<ul class="pagination">';

        if (paginationInfo.firstPageNoOnPageList > 1) {
            html += '<li class="page-item"><a class="page-link" href="javascript:goPage(1)">처음</a></li>';
            html += '<li class="page-item"><a class="page-link" href="javascript:goPage(' + (paginationInfo.firstPageNoOnPageList - 1) + ')">이전</a></li>';
        }

        for (var i = paginationInfo.firstPageNoOnPageList; i <= paginationInfo.lastPageNoOnPageList; i++) {
            html += '<li class="page-item ' + (paginationInfo.currentPageNo == i ? 'active' : '') + '">';
            html += '<a class="page-link" href="javascript:goPage(' + i + ')">' + i + '</a></li>';
        }

        if (paginationInfo.lastPageNoOnPageList < paginationInfo.totalPageCount) {
            html += '<li class="page-item"><a class="page-link" href="javascript:goPage(' + (paginationInfo.firstPageNoOnPageList + 1) + ')">다음</a></li>';
            html += '<li class="page-item"><a class="page-link" href="javascript:goPage(' + paginationInfo.totalPageCount + ')">끝</a></li>';
        }

        html += '</ul>';
        paginationArea.html(html);  // 페이징 UI 업데이트
    } */
    
    function searchKeywordBtn() {
        let url = "boardList.do";
        url += "?searchCondition=" + $('#searchCondition').val();
        url += "&searchKeyword=" + $('#searchKeyword').val();
        location.href = url;
        console.log(url);
    }
</script>
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
			   				<th>제목</th>
			   				<th>작성자</th>
			   				<th>등록일</th>
			   				<th>조회수</th>
			   			</tr>
					</thead>
					<tbody id="listBodyArea">
					<c:forEach var="list" items="${boardList}" varStatus="status">
		       			<tr>
		       				<%-- <td><c:out value="${list.no }" /></td>
		       				<td style="text-align: center;">${fn:length(boardList) - status.index}</td> --%>
		       				
	            			<td style="text-align: center;"><c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
		       				<td class="info-select" onClick="selectBoardInfo('<c:out value="${list.no }" />')"><c:out value="${list.title }" /></td>
		       				<td style="text-align: center;"><c:out value="${list.writer }" /></td>
		       				<td style="text-align: center;"><c:out value="${list.date }" /></td>
		       				<td style="text-align: center;"><c:out value="${list.view }" /></td>
		       			</tr>
		       			
		       			<c:set var="isAnswered" value="${list.isAnswered }" />
		       				<c:forEach var="answer" items="${answerList }" varStatus="status">
				       			<c:if test="${isAnswered eq 1 and list.no eq answer.no}">
				       			<tr style="background-color: aliceblue;">
			            			<%-- <td style="text-align: center;"><c:out value="${answer.no }" /></td> --%>
			            			<td style="text-align: center">[답변]</td>
				       				<td class="info-select" onClick="selectAnswerInfo('<c:out value="${answer.answerNo }" />')"> ㄴ <c:out value="${answer.title }" /></td>
				       				<td style="text-align: center;"><c:out value="${answer.writer }" /></td>
				       				<td style="text-align: center;"><c:out value="${answer.date }" /></td>
				       				<td style="text-align: center;"><c:out value="${answer.view }" /></td>
				       			</tr>
				       			</c:if>
				   			</c:forEach>
		   			</c:forEach>
					</tbody>
		   		</table>
			</div>
			
			<%-- <div>
				<table class="table table-bordered">
					<thead class="thead-light" style="text-align: center;">
			   			<tr> 
			   				<th>순번</th>
			   				<th>제목</th>
			   				<th>작성자</th>
			   				<th>등록일</th>
			   				<th>조회수</th>
			   			</tr>
					</thead>
					<tbody id="listBodyArea">
					<c:forEach var="list" items="${answerList }" varStatus="status">
		       			<tr>
	            			<td style="text-align: center;"><c:out value="${list.no }" /></td>
		       				<td class="info-select" onClick="selectBoardInfo('<c:out value="${list.no }" />')"><c:out value="${list.title }" /></td>
		       				<td style="text-align: center;"><c:out value="${list.writer }" /></td>
		       				<td style="text-align: center;"><c:out value="${list.date }" /></td>
		       				<td style="text-align: center;"><c:out value="${list.view }" /></td>
		       			</tr>
		   			</c:forEach>
					</tbody>
		   		</table>
			</div> --%>
			
			
	      	<%-- <div id="pagination" style="text-align: center;">		
				<c:if test="${paging.startPage != 1 }">
					<a href="/boardList?nowPage=${paging.startPage - 1 }&cntPerPage=${paging.cntPerPage}">&lt;</a>
				</c:if>
				<c:forEach begin="${paging.startPage }" end="${paging.endPage }" var="p">
					<c:choose>
						<c:when test="${p == paging.nowPage }">
							<b>${p }</b>
						</c:when>
						<c:when test="${p != paging.nowPage }">
							<a href="/boardList?nowPage=${p }&cntPerPage=${paging.cntPerPage}">${p }</a>
						</c:when>
					</c:choose>
				</c:forEach>
				<c:if test="${paging.endPage != paging.lastPage}">
					<a href="/boardList?nowPage=${paging.endPage+1 }&cntPerPage=${paging.cntPerPage}">&gt;</a>
				</c:if>
			</div> --%>
		</form>
		
		<!-- 페이징 -->
		<!-- <div class="pagination" id="paginationArea">
			js로 데이터 삽입
		</div> -->
		<%-- <div id="paging">
      		<ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fn_egov_link_page" />
      		<form:hidden path="pageIndex" />
      	</div> --%>
      	
      	<div>
      		<span>${paginationInfo.currentPageNo }</span>
      		<a href="javascript:pagination(2)">2</a>
      		<span>${paginationInfo.totalPageCount }</span>
		      	<!-- private int currentPageNo;
				private int recordCountPerPage;
				private int pageSize;
				private int totalRecordCount; -->
      	</div>
		
		<!-- 글쓰기 버튼 -->
		<div class="post-btn-container">
			<button class="post-btn" type="button" onclick="location.href='boardPost.do'">글쓰기</button>
		</div>
	</div>
</body>
<script>
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
	
	function pagination(pageNo) {
		/* document.boardListForm.action = "<c:url value = 'boardList.do?pageNo=" + pageNo + "' />";
		document.boardListForm.submit(); */
		
		$.ajax({
			url: 'boardList.do?pageNo=' + pageNo,
			success: function(data) {
				updateListForm(pageNo);
			},
			error: function(err) {
				console.log("ERROR: ", err);
			}
			
		});
	}
	
	function updateListForm(pageNo) {
		let listArea = document.querySelector('#listBodyArea');
		let listForm = `
						<c:forEach var="list" items="${boardList}" varStatus="status">
				   			<tr>
				    			<td align="center" class="listtd"><c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
				   				<td class="info-select" onClick="selectBoardInfo('<c:out value="${list.no }" />')"><c:out value="${list.title }" /></td>
				   				<td style="text-align: center;"><c:out value="${list.writer }" /></td>
				   				<td style="text-align: center;"><c:out value="${list.date }" /></td>
				   				<td style="text-align: center;"><c:out value="${list.view }" /></td>
				   			</tr>
						</c:forEach>
						`;
		
		listArea.innerHTML = listForm;						
		
						
		console.log(listArea);
	}
</script>
</html>