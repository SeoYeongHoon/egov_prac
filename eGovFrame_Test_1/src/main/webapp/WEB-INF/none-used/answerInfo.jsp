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
	//파일다운 기능
	function downloadFile(exName) {
		document.boardInfoForm.action = "<c:url value='fileDownload.do?extendedName=" + exName + "' />";
		document.boardInfoForm.submit();
	}
</script>
<body>
	<div id="boardInfo">
		<form id="boardInfoForm" name="boardInfoForm" method="POST" encType="multipart/form-data">
			<input type="hidden" name="no" value="${answerInfo.answerNo }" />
			<!-- 제목 -->
			<h2 class="post-title">${answerInfo.title }</h2>
			<!-- 작성자, 등록일, 조회수 -->
			<div class="post-info-container">
				<span class="post-info">${answerInfo.writer }</span>
				<span class="post-info">등록일&nbsp ${answerInfo.date }</span>
				<span class="post-info">조회수&nbsp ${answerInfo.view }</span>
			</div>
			
			<!-- 내용 -->
			<div class="post-content-container">
				<p class="post-content">${answerInfo.content }</p>
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
					<button class="to-delete-btn" type="button" onClick="deleteFormBtn()">삭제</button>
				</div>
			</div>
		</form>
	</div>
	
	<!-- 비밀번호 틀렸을 경우 alert창 띄우기 -->
	<c:if test="${not empty errorMsg}">
	    <script>
	        alert("${errorMsg}");
	    </script>
	</c:if>
</body>
<script>
	function updateFormBtn() {
	    var password = document.getElementById('passwordCheck').value;
	    if (password === '') {
	        alert('비밀번호를 입력하세요.');
	        return;
	    }
	    // 비밀번호를 POST 방식으로 전송하여 수정 페이지로 이동
	    var form = document.getElementById('boardInfoForm');
	    form.action = 'answerUpdatePage.do';  // 수정 페이지로 POST 요청
	    form.submit();
	}
	
	function deleteFormBtn() {
        var password = document.getElementById('passwordCheck').value;
        if (password === '') {
            alert('비밀번호를 입력하세요.');
            return;
        }
        // 비밀번호를 POST 방식으로 전송하여 삭제 처리
        var form = document.getElementById('boardInfoForm');
        form.action = 'answerDelete.do';  // 삭제 요청
        form.submit();
    }
</script>
</html>