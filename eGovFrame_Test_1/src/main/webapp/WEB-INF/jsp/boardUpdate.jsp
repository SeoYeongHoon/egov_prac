<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>" />
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<title>게시글 수정</title>
</head>
<style>
	.delete {
		cursor: pointer;
		display: inline-flex; 
		align-items: center;
		height: 100%;
		padding: 5px;
	}
	.delete:hover {
		background-color: lightgray;
		border-radius: 5px;
	}
</style>
<body>
	<div id="boardUpdate">
		<form id="boardUpdateForm" name="boardUpdateForm" method="POST" encType="multipart/form-data">
			<div class="writer-area">
				<input type="hidden" name="no" value="${boardInfo.no }" />
				<div class="writer-content">				
					<label style="width: 165.4px;">작성자 <span style="color: red;">&#10004;</span></label>
					<input class="info-input" type="text" name="writer" value="${boardInfo.writer }" required/>
				</div>
				<div class="password-content">
					<label class="post-area-label">비밀번호 <span style="color: red;">&#10004;</span></label>
					<input class="info-input" type="password" name="originPw" id="passwordCheck" required />
				</div>
			</div>
			<div class="title-area">
				<label class="post-area-label">제목 <span style="color: red;">&#10004;</span></label>
				<input class="title-input" type="text" name="title" value="${boardInfo.title }" required />		
			</div>
			<div class="content-area">
				<label class="post-area-label">내용</label>
				<textarea class="content-input" name="content">${boardInfo.content }</textarea>
			</div>
			<div>
				<div class="file-area">
					<label style="width: 165.4px;" class="post-area-label">첨부파일</label>
					<input class="upload-name" readonly />
					<label class="upload-btn" for="file">파일선택</label>
					<input type="file" onchange="addFile(this);" id="file" name="multiFile" multiple />
				</div>
				<div class="file-update-list" style="margin-left: 165.4px;">
					<!-- <input type="hidden" name="deletedFileNo" class="deletedFiles" /> -->
					<c:forEach var="files" items="${fileInfo }" varStatus="status">
						<c:set var="isFileExist" value="${files.fileName }" />
						<c:choose>
							<c:when test="${isFileExist eq ''}">
							</c:when>
							<c:otherwise>
								<div id="file${status.index }" class="filebox"  style="height:35px; display: flex; align-items: center;">
									<input type="hidden" class="fileNum" name="fileId" value="${files.fileNo }" />
									<a style="display: flex; align-items: center; cursor: pointer;" class="delete" onClick="deleteFile('<c:out value="${status.index }" />')">
										<img class="remove-btn" src="<c:url value='/images/egovframework/board/removebtn.png' />" />
										<span style="margin-left: 10px;" class="name">${files.fileName }</span>
									</a>
								</div>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>
			
			<div class="save-btn-area">		
				<button class="post-save-btn" type="button" onClick="updateBoard()">저장</button>
				<button class="post-cancel-btn" type="button" onClick="javascript:history.back()">취소</button>
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
	let orgFiles = document.querySelectorAll('.filebox');
	
	var fileNo = orgFiles.length;
	var filesArr = new Array();
	
	for (var i = 0; i < orgFiles.length; i++) {
		// 기존에 있는 파일들 배열에 넣기
		filesArr.push(orgFiles[i]);
	}
	
	function addFile(obj) {
	    let curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수
	    
	    for (var i = 0; i < curFileCnt; i++) {
	
	        const file = obj.files[i];
	        filesArr.push(file);
	
	        // 목록 추가
	        let htmlData = '';
	        htmlData += '<div id="file' + fileNo + '" class="filebox" style="height:35px; display: flex; align-items: center;">';
	        htmlData += '<input type="hidden" class="fileNum" name="fileId" value="${files.fileNo }" />';
	        htmlData += '<a class="delete" onclick="deleteFile(' + fileNo + ');"><img class="remove-btn" src="<c:url value='/images/egovframework/board/removebtn.png'/>"</a>';
	        htmlData += '<p style="margin-left: 10px;" class="name">' + file.name + '</p>';
	        htmlData += '</div>';
	        
	        $('.file-update-list').append(htmlData);
	        fileNo++;
	    }
	}
	
	function deleteFile(num) {
		
		if (confirm("삭제 하시겠습니까?")) {
			let fileIdVal = document.querySelector("#file" + num).children.fileId.value;
			console.log(fileIdVal);
			
			
			document.querySelector("#file" + num).remove(); // div 삭제
			
			let inputData = ``;
			inputData += '<input type="hidden" name="deletedFileNo" value="' + fileIdVal + '"class="deletedFiles" />';
			$('.file-update-list').append(inputData);
			
			const dataTransfer = new DataTransfer();
			
			
			let files = $('#file')[0].files; // 입력한 파일 변수에 할당
			
			let dataArray = Array.from(files); // 변수에 할당된 파일을 배열로 변환
			
			dataArray.splice(num, 1); // 해당하는 index의 파일을 배열에서 제거
			
			dataArray.forEach(function(file) {
				dataTransfer.items.add(file);
				// 남은 배열을 dataTransfer로 처리(Array -> FileList)
			});
			
			$('#file')[0].files = dataTransfer.files; // 제거 처리된 FileList를 돌려줌
			
		} else {
			return;	
		}
	}
	
	function updateBoard() {
	    var password = document.getElementById('passwordCheck').value;
	    if (password === '') {
	        alert('비밀번호를 입력하세요.');
	        return;
	    }
	    // 비밀번호를 POST 방식으로 전송하여 수정 페이지로 이동
	    var form = document.getElementById('boardUpdateForm');
	    form.action = 'boardUpdate.do';  // 수정 페이지로 POST 요청
	    form.submit();
	}
</script>
</html>