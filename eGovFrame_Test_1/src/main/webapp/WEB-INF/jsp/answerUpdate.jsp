<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 답변</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>"/>
</head>
<script>
function updateBoard() {
	document.answerPostForm.action = "<c:url value='answerUpdate.do' />";
	document.answerPostForm.submit();
}
</script>
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
	<div id="boardAdd">
		<div style="text-align: center; margin-bottom: 40px;">
			<h3>[ ${boardInfo.title } ] 글에 대한 답변글</h3>
		</div>
		<form action="answerPost.do" id="answerPostForm" name="answerPostForm" method="POST" encType="multipart/form-data">
			
			<input type="hidden" name="answerNo" value="${answerInfo.no }" />
			<div class="writer-area">
				<div class="writer-content">				
					<label style="width: 165.4px;" for="writer">작성자 <span style="color: red;">&#10004;</span></label>
					<input class="info-input" type="text" name="writer" value="${answerInfo.writer }" required />
				</div>
				<div class="password-content">
					<label class="post-area-label" for="password">비밀번호 <span style="color: red;">&#10004;</span></label>
					<input class="info-input" type="password" name="password" required />
				</div>
			</div>
			<div class="title-area">
				<label class="post-area-label" for="title">제목 <span style="color: red;">&#10004;</span></label>
				<input class="title-input" type="text" name="title" value="${answerInfo.title }" required />		
			</div>
			<div class="content-area">
				<label class="post-area-label" for="content">내용</label>
				<textarea class="content-input" name="content">${answerInfo.content }</textarea>
			</div>
			<div>
				<div class="file-area">
					<label style="width: 165.4px;" class="post-area-label" for="uploadFile">첨부파일</label>
					<input class="upload-name" readonly />
					<label class="upload-btn" for="file">파일선택</label>
					<input type="file" onchange="addFile(this);" id="file" name="multiFile" multiple />
				</div>
				<div class="file-update-list" style="margin-left: 165.4px;">
					<c:forEach var="files" items="${fileInfo }" varStatus="status">
						<c:set var="isFileExist" value="${files.fileName }" />
						<c:choose>
							<c:when test="${isFileExist eq ''}">
							</c:when>
							<c:otherwise>
								<div id="file${status.index }" class="filebox"  style="height:35px; display: flex; align-items: center;">
									<input type="hidden" name="fileNum" value="${files.fileNo }" />
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
				<button class="post-cancel-btn" type="button" onClick="location.href='boardList.do'">취소</button>
			</div>
		</form>
	</div>
</body>
<script>
	let orgFiles = document.querySelectorAll('.filebox');
	
	var fileNo = 0;
	var filesArr = new Array();
	
	for (var i = 0; i < orgFiles.length; i++) {
		// 기존에 있는 파일들 배열에 넣기
		filesArr.push(orgFiles[i]);
	}
	
	function addFile(obj) {
	    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수
	    
	    for (var i = 0; i < curFileCnt; i++) {
	
	        const file = obj.files[i];
	        filesArr.push(file);
	
	        // 목록 추가
	        let htmlData = '';
	        htmlData += '<div id="file' + fileNo + '" class="filebox" style="height:35px;">';
	        htmlData += '<input type="hidden" class="fileNum" name="fileId" value="${files.fileNo }" />';
	        htmlData += '<a style="display: flex; align-items: center;" class="delete" onclick="deleteFile(' + fileNo + ');"><img class="remove-btn" src="<c:url value='/images/egovframework/board/removebtn.png'/>"</a>';
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
</script>
</html>