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
	var fileNo = 0; // 파일이 있는 태그마다 id 값을 주기 위해 선언
	var filesArr = []; // 파일들을 담을 배열 선언
	
	function addFile(obj) {
	    var curFileCnt = obj.files.length; // 현재 파일들의 크기 담기 
	    
	    for (var i = 0; i < curFileCnt; i++) {
	        const file = obj.files[i];
	        filesArr.push(file); // 파일 배열에 추가
	
	        // 파일 목록 추가
	        let htmlData = '';
	        htmlData += '<div id="file' + fileNo + '" class="filebox" style="height:35px;">';
	        htmlData += '<a class="delete" onclick="deleteFile(' + fileNo + ');"><img class="remove-btn" src="<c:url value='/images/egovframework/board/removebtn.png'/>"</a>';
	        htmlData += '<p style="margin-left: 10px;" class="name">' + file.name + '</p>';
	        htmlData += '</div>';
	        
	        $('.file-add-list').append(htmlData);
	        fileNo++; // 파일 번호 증가
	    }
	}
	
	function deleteFile(num) {
		// 1. 삭제할 파일을 filesArr에서 제거(인덱스만)
		// 2. dataTransfer로 접근해서 실제 FileList에 있는 file 요소 제거.
		// 3. 이 후 FileList에 제거한 파일 요소를 넣음.
		// 4. 파일 목록 리스트 초기화, 파일번호 초기화
		// 5. 목록 재생성
		if (confirm("삭제 하시겠습니까?")) {
			
		    // filesArr에서 해당 파일 삭제
		    filesArr.splice(num, 1);
		
		    // input type="file"의 선택된 파일들의 목록에 접근 가능하기 위해 DataTransfer 객체 선언
		    const dataTransfer = new DataTransfer();
		
		    // 선택된 파일들을 dataTransfer로 처리(Array -> FileList)
		    filesArr.forEach(function(file) {
		        dataTransfer.items.add(file);
		    });
		
		    // input의 FileList에 dataTransfer의 파일들을 넣음
		    $('#file')[0].files = dataTransfer.files;
		
		    // 화면에서 파일 리스트 업데이트
		    $('.file-add-list').empty(); // 기존 리스트 초기화
		    fileNo = 0; // 번호 초기화
		    
		    // 초기화 된 후 목록 생성
		    filesArr.forEach(function(file) {
		    	let htmlData = '';
		        htmlData += '<div id="file' + fileNo + '" class="filebox" style="height:35px;">';
		        htmlData += '<a class="delete" onclick="deleteFile(' + fileNo + ');"><img class="remove-btn" src="<c:url value='/images/egovframework/board/removebtn.png'/>"</a>';
		        htmlData += '<p style="margin-left: 10px;" class="name">' + file.name + '</p>';
		        htmlData += '</div>';
		        
		        $('.file-add-list').append(htmlData);
		        fileNo++;
		    });
		}
	}
</script>
</html>