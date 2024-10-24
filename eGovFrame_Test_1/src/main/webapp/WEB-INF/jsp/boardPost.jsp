<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 등록</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>"/>
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
	<div id="boardAdd">
		<form action="boardPost.do" id="boardPostForm" name="boardPostForm" method="POST" encType="multipart/form-data">
			<div class="writer-area">
				<div class="writer-content">				
					<label style="width: 165.4px;" for="writer">작성자 <span style="color: red;">&#10004;</span></label>
					<input class="info-input" type="text" name="writer" required />
				</div>
				<div class="password-content">
					<label class="post-area-label" for="password">비밀번호 <span style="color: red;">&#10004;</span></label>
					<input class="info-input" type="password" name="password" required />
				</div>
			</div>
			<div class="title-area">
				<label class="post-area-label" for="title">제목 <span style="color: red;">&#10004;</span></label>
				<input class="title-input" type="text" name="title" required />		
			</div>
			<div class="content-area">
				<label class="post-area-label" for="content">내용</label>
				<textarea class="content-input" name="content"></textarea>
			</div>
			<div>
				<div class="file-area">
					<label style="width: 165.4px;" class="post-area-label" for="uploadFile">첨부파일</label>
					<input class="upload-name" readonly />
					<label class="upload-btn" for="file">파일선택</label>
					<input type="file" onchange="addFile(this);" id="file" name="multiFile" multiple />
				</div>
				<div class="file-add-list" style="margin-left: 165.4px; margin-bottom: 20px;"></div>
			</div>
			<div class="save-btn-area">
				<button class="post-save-btn" type="submit">저장</button>
				<button class="post-cancel-btn" type="button" onClick="location.href='boardList.do'">취소</button>
			</div>
		</form>
	</div>
</body>
<script>
	const myFile = document.querySelector("#file");
	const fileNameList = document.querySelector("#file-add-list");
	
	var fileNo = 0;
	var filesArr = new Array();
	
	function addFile(obj) {
	    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수
	    
	    for (var i = 0; i < curFileCnt; i++) {
	
	        const file = obj.files[i];
	        filesArr.push(file);
	
	        // 목록 추가
	        let htmlData = '';
	        htmlData += '<div id="file' + fileNo + '" class="filebox" style="height:35px;">';
	        htmlData += '<a class="delete" onclick="deleteFile(' + fileNo + ');"><img class="remove-btn" src="<c:url value='/images/egovframework/board/removebtn.png'/>"</a>';
	        htmlData += '<p style="margin-left: 10px;" class="name">' + file.name + '</p>';
	        htmlData += '</div>';
	        
	        $('.file-add-list').append(htmlData);
	        fileNo++;
	    }
	}
	
	function deleteFile(num) {
		document.querySelector("#file" + num).remove(); // div 삭제
		
		const dataTransfer = new DataTransfer();
		
		let files = $('#file')[0].files; // 입력한 파일 변수에 할당
		
		let dataArray = Array.from(files); // 변수에 할당된 파일을 배열로 변환
		
		dataArray.splice(num, 1); // 해당하는 index의 파일을 배열에서 제거
		
		dataArray.forEach(function(file) {
			dataTransfer.items.add(file);
			// 남은 배열을 dataTransfer로 처리(Array -> FileList)
		});
		
		$('#file')[0].files = dataTransfer.files; // 제거 처리된 FileList를 돌려줌
		
	}
</script>
</html>