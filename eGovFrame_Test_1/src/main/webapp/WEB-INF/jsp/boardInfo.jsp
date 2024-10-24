<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>"/>
</head>
<script>
	// 파일다운 기능
	function downloadFile(exName) {
		document.boardInfoForm.action = "<c:url value='fileDownload.do?extendedName=" + exName + "' />";
		document.boardInfoForm.submit();
	}

	// 수정 기능
	function updateFormBtn() {
		/* let pwCheck = document.querySelector('#passwordCheck');
		
		if (pwCheck.value === pw) {
			document.boardInfoForm.action = "<c:url value='boardUpdatePage.do' />";
			document.boardInfoForm.submit();
		} else {
			alert("잘못된 비밀번호");
			pwCheck.value = '';
			pwCheck.focus();
		} */
		
		
		document.boardInfoForm.action = "<c:url value='boardUpdatePage.do' />";
		document.boardInfoForm.submit();
	}
	
	// 삭제 기능
	function deleteFormBtn(pw) {
		document.boardInfoForm.action = "<c:url value='boardDelete.do' />";
		document.boardInfoForm.submit();
	}
	
	// 답변 이동
	function answerFormBtn(no) {
		document.boardInfoForm.action = "<c:url value='boardAnswerPage.do?no=" + no + "'  />";
		document.boardInfoForm.submit();
	}
	
	// 1000단위 콤마
	/* function addCommas() {
		let fileSize = document.querySelector(".postFileSize").innerText;
		if (fileSize != null) {
			return fileSize.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");			
		}
	}
	
	window.onload = function() {
		addCommas();
	} */
	
</script>
<body>
	<div id="boardInfo">
		<form id="boardInfoForm" name="boardInfoForm" method="POST" encType="multipart/form-data">
			<input type="hidden" name="no" value="${boardInfo.no }" />
			<!-- 제목 -->
			<h2 class="post-title">${boardInfo.title }</h2>
			<!-- 작성자, 등록일, 조회수 -->
			<div class="post-info-container">
				<span class="post-info">${boardInfo.writer }</span>
				<span class="post-info">등록일&nbsp ${boardInfo.date }</span>
				<span class="post-info">조회수&nbsp ${boardInfo.view }</span>
			</div>
			
			<!-- 내용 -->
			<div class="post-content-container">
				<p class="post-content">${boardInfo.content }</p>
			</div>
			
			<!-- 첨부파일 -->
			<div class="post-file-container" style="display: flex;">
				<span style="margin-right: 30px;">첨부파일</span>
				<div>
					<c:forEach var="files" items="${fileInfo}" varStatus="status">
						<c:set var="isFileExist" value="${files.fileSize }" />
						<c:if test="${isFileExist ne 0 }">
							<input type="hidden" name="fileName" value="${files.fileName }" />
							<a style="display: block;" class="post-file" href="#" onClick="downloadFile('<c:out value="${files.extendedName }" />')">
								<span>${files.fileName }</span>
								<span class="postFileSize" style="color: darkgray;">[${files.fileSize } byte]</span>	
							</a>
						</c:if>
					</c:forEach>
				</div>
			</div>

		
			<!-- 하단 버튼들(목록, 비밀번호 입력란, 수정, 삭제, 답변등록) -->
			<div style="display: flex; position: relative;">
				<button class="to-list-btn" type="button" onClick="location.href='boardList.do'">목록</button>				
				<div class="option-btns">
					<input style="width: 250px; height: 35px; padding-left: 10px; border: 1px solid rgb(0, 0, 0, 0.1);" name="originPw" type="password" id="passwordCheck" placeholder="비밀번호" />
					<button class="to-update-btn" type="button" onClick="updateFormBtn()">수정</button>
					<button class="to-delete-btn" type="button" onClick="deleteFormBtn('<c:out value="${boardInfo.password }" />')">삭제</button>
					<button class="to-answer-btn" type="button" onClick="answerFormBtn('<c:out value="${boardInfo.no }" />')">답변등록</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>