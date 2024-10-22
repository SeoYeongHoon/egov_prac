<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 답변</title>
</head>
<body>
	<form action="answerPost.do" id="answerPostForm" name="answerPostForm" method="POST" encType="multipart/form-data">
		<div>
			<p>[ ${boardInfo.title } ] 글에 대한 답변</p>
		</div>	
		<div>
			<label for="writer">작성자</label>
			<input type="text" name="writer" required />
			<label for="password">비밀번호</label>
			<input type="password" name="password" required />
		</div>
		<div>
			<label for="title">제목</label>
			<input type="text" name="title" required />		
		</div>
		<div>
			<label for="content">내용</label>
			<input type="text" name="content" />
		</div>
		<div>
			<label for="uploadFile">첨부파일</label>
			<input type="file" name="uploadFile" />
		</div>
		<button type="submit">저장</button>
		<button type="button" onClick="javascript:history.back()">취소</button>
	</form>
</body>
</html>